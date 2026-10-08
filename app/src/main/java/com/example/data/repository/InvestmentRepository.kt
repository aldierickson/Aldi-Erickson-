package com.example.data.repository

import com.example.data.LeaderboardData
import com.example.data.StockMarketEngine
import com.example.data.local.AppDatabase
import com.example.data.local.entity.ForumCommentEntity
import com.example.data.local.entity.ForumPostEntity
import com.example.data.local.entity.ModuleProgressEntity
import com.example.data.local.entity.StockHoldingEntity
import com.example.data.local.entity.TradeTransactionEntity
import com.example.data.local.entity.UserPortfolioEntity
import com.example.data.model.HoldingDetail
import com.example.data.model.LeaderboardStudent
import com.example.data.model.PortfolioSummary
import com.example.data.model.Stock
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.util.UUID

class InvestmentRepository(private val db: AppDatabase) {

    private val portfolioDao = db.portfolioDao()
    private val tradeDao = db.tradeDao()
    private val moduleDao = db.moduleDao()
    private val forumDao = db.forumDao()

    val stocksFlow = StockMarketEngine.stocks

    val portfolioEntityFlow = portfolioDao.getPortfolioFlow()
    val holdingsFlow = portfolioDao.getAllHoldingsFlow()
    val transactionsFlow = tradeDao.getAllTransactionsFlow()
    val moduleProgressFlow = moduleDao.getAllProgressFlow()
    val forumPostsFlow = forumDao.getAllPostsFlow()

    fun getCommentsForPostFlow(postId: String) = forumDao.getCommentsForPostFlow(postId)

    // Combine cash balance, holdings, and stock prices to calculate dynamic real-time portfolio metrics
    val portfolioSummaryFlow: Flow<PortfolioSummary> = combine(
        portfolioEntityFlow,
        holdingsFlow,
        stocksFlow,
        transactionsFlow
    ) { portfolioEntity, holdings, stocks, transactions ->
        val cash = portfolioEntity?.cashBalance ?: 100_000_000L
        val initialCapital = portfolioEntity?.initialCapital ?: 100_000_000L

        val stockMap = stocks.associateBy { it.ticker }

        var totalStocksValue = 0L
        var totalCost = 0L
        val holdingDetails = mutableListOf<HoldingDetail>()

        for (h in holdings) {
            val stock = stockMap[h.ticker]
            val currentPrice = stock?.price ?: h.averagePrice.toLong()
            val shares = h.lots * 100
            val curVal = shares * currentPrice
            val costVal = (shares * h.averagePrice).toLong()
            val pnl = curVal - costVal
            val pnlPercent = if (costVal > 0) (pnl.toDouble() / costVal) * 100.0 else 0.0

            totalStocksValue += curVal
            totalCost += costVal

            holdingDetails.add(
                HoldingDetail(
                    ticker = h.ticker,
                    stockName = h.stockName,
                    lots = h.lots,
                    shares = shares,
                    averagePrice = h.averagePrice,
                    currentPrice = currentPrice,
                    currentValue = curVal,
                    totalInvestment = costVal,
                    unrealizedPnL = pnl,
                    unrealizedPnLPercent = pnlPercent
                )
            )
        }

        val totalEquity = cash + totalStocksValue
        val totalUnrealizedPnL = totalStocksValue - totalCost
        val totalUnrealizedPnLPercent = if (totalCost > 0) (totalUnrealizedPnL.toDouble() / totalCost) * 100.0 else 0.0

        val totalRealizedPnL = transactions.filter { it.type == "SELL" }.sumOf { it.realizedPnL }
        val totalReturn = totalEquity - initialCapital
        val totalReturnPercent = if (initialCapital > 0) (totalReturn.toDouble() / initialCapital) * 100.0 else 0.0

        PortfolioSummary(
            cashBalance = cash,
            initialCapital = initialCapital,
            stocksValue = totalStocksValue,
            totalEquity = totalEquity,
            totalUnrealizedPnL = totalUnrealizedPnL,
            totalUnrealizedPnLPercent = totalUnrealizedPnLPercent,
            totalRealizedPnL = totalRealizedPnL,
            totalReturnPercent = totalReturnPercent,
            holdingsWithStock = holdingDetails
        )
    }

    suspend fun buyStock(ticker: String, lots: Int): Result<String> {
        if (lots <= 0) return Result.failure(Exception("Jumlah lot harus lebih dari 0"))
        val stock = StockMarketEngine.getStock(ticker) ?: return Result.failure(Exception("Saham tidak ditemukan"))

        val portfolio = portfolioDao.getPortfolio() ?: UserPortfolioEntity()
        val shares = lots * 100
        val grossAmount = shares * stock.price
        val fee = (grossAmount * 0.0015).toLong() // 0.15% IDX broker & levy fee
        val totalCost = grossAmount + fee

        if (portfolio.cashBalance < totalCost) {
            val shortFall = totalCost - portfolio.cashBalance
            return Result.failure(Exception("Saldo RDN tidak mencukupi. Kurang Rp $shortFall"))
        }

        val existingHolding = portfolioDao.getHolding(ticker)
        val newHolding = if (existingHolding != null) {
            val totalOldShares = existingHolding.lots * 100
            val oldTotalCost = totalOldShares * existingHolding.averagePrice
            val newTotalShares = totalOldShares + shares
            val newAvgPrice = (oldTotalCost + grossAmount) / newTotalShares
            existingHolding.copy(
                lots = existingHolding.lots + lots,
                averagePrice = newAvgPrice
            )
        } else {
            StockHoldingEntity(
                ticker = ticker,
                stockName = stock.name,
                lots = lots,
                averagePrice = stock.price.toDouble()
            )
        }

        portfolioDao.insertOrUpdateHolding(newHolding)
        portfolioDao.updateCashBalance(portfolio.cashBalance - totalCost)

        tradeDao.insertTransaction(
            TradeTransactionEntity(
                ticker = ticker,
                stockName = stock.name,
                type = "BUY",
                lots = lots,
                pricePerShare = stock.price,
                totalValue = grossAmount,
                fee = fee,
                realizedPnL = 0L,
                timestamp = System.currentTimeMillis()
            )
        )

        return Result.success("Berhasil membeli $lots Lot $ticker @ Rp ${stock.price}")
    }

    suspend fun sellStock(ticker: String, lots: Int): Result<String> {
        if (lots <= 0) return Result.failure(Exception("Jumlah lot harus lebih dari 0"))
        val stock = StockMarketEngine.getStock(ticker) ?: return Result.failure(Exception("Saham tidak ditemukan"))
        val existingHolding = portfolioDao.getHolding(ticker)
            ?: return Result.failure(Exception("Anda tidak memiliki saham $ticker"))

        if (existingHolding.lots < lots) {
            return Result.failure(Exception("Kepemilikan hanya ${existingHolding.lots} Lot. Tidak cukup untuk jual $lots Lot."))
        }

        val portfolio = portfolioDao.getPortfolio() ?: UserPortfolioEntity()
        val shares = lots * 100
        val grossProceeds = shares * stock.price
        val fee = (grossProceeds * 0.0025).toLong() // 0.25% IDX sell fee + 0.1% PPh Final
        val netProceeds = grossProceeds - fee

        val costBasis = (shares * existingHolding.averagePrice).toLong()
        val realizedPnL = netProceeds - costBasis

        if (existingHolding.lots == lots) {
            portfolioDao.deleteHolding(ticker)
        } else {
            portfolioDao.insertOrUpdateHolding(
                existingHolding.copy(lots = existingHolding.lots - lots)
            )
        }

        portfolioDao.updateCashBalance(portfolio.cashBalance + netProceeds)

        tradeDao.insertTransaction(
            TradeTransactionEntity(
                ticker = ticker,
                stockName = stock.name,
                type = "SELL",
                lots = lots,
                pricePerShare = stock.price,
                totalValue = grossProceeds,
                fee = fee,
                realizedPnL = realizedPnL,
                timestamp = System.currentTimeMillis()
            )
        )

        val pnlSign = if (realizedPnL >= 0) "+Rp " else "-Rp "
        val absPnl = Math.abs(realizedPnL)
        return Result.success("Berhasil menjual $lots Lot $ticker! Realized P/L: $pnlSign$absPnl")
    }

    suspend fun resetPortfolio(): Result<String> {
        portfolioDao.insertOrUpdatePortfolio(
            UserPortfolioEntity(
                id = 1,
                cashBalance = 100_000_000L,
                initialCapital = 100_000_000L,
                userName = "Dimas Arya (KSPM UI)",
                university = "Universitas Indonesia"
            )
        )
        portfolioDao.clearHoldings()
        tradeDao.clearTransactions()
        return Result.success("Portofolio berhasil di-reset ke modal awal Rp 100.000.000")
    }

    suspend fun saveModuleQuizResult(moduleId: String, score: Int, maxScore: Int) {
        moduleDao.saveProgress(
            ModuleProgressEntity(
                moduleId = moduleId,
                completed = true,
                quizScore = score,
                maxScore = maxScore,
                completedAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun addForumPost(
        title: String,
        content: String,
        tag: String,
        stockTicker: String?
    ): Result<String> {
        val newPost = ForumPostEntity(
            id = "post-${UUID.randomUUID().toString().take(8)}",
            authorName = "Dimas Arya",
            authorBadge = "Mahasiswa KSPM",
            university = "Universitas Indonesia",
            tag = tag,
            stockTicker = stockTicker?.takeIf { it.isNotBlank() },
            title = title,
            content = content,
            likesCount = 1,
            commentsCount = 0,
            timestamp = System.currentTimeMillis(),
            isPinned = false,
            isLikedByMe = true
        )
        forumDao.insertPost(newPost)
        return Result.success("Diskusi berhasil diterbitkan di forum!")
    }

    suspend fun toggleLikePost(postId: String) {
        val post = forumDao.getPostById(postId) ?: return
        val newLiked = !post.isLikedByMe
        val newCount = if (newLiked) post.likesCount + 1 else (post.likesCount - 1).coerceAtLeast(0)
        forumDao.updatePost(
            post.copy(
                isLikedByMe = newLiked,
                likesCount = newCount
            )
        )
    }

    suspend fun addForumComment(postId: String, content: String): Result<String> {
        val comment = ForumCommentEntity(
            id = "c-${UUID.randomUUID().toString().take(8)}",
            postId = postId,
            authorName = "Dimas Arya",
            authorBadge = "Mahasiswa KSPM",
            university = "Universitas Indonesia",
            content = content,
            timestamp = System.currentTimeMillis()
        )
        forumDao.insertComment(comment)
        val post = forumDao.getPostById(postId)
        if (post != null) {
            forumDao.updatePost(post.copy(commentsCount = post.commentsCount + 1))
        }
        return Result.success("Komentar terkirim!")
    }

    // Dynamic leaderboard combining real-time user portfolio ROI with peers
    fun getDynamicLeaderboard(currentReturnPercent: Double, currentEquity: Long, tradesCount: Int): List<LeaderboardStudent> {
        val userItem = LeaderboardStudent(
            rank = 7,
            name = "Dimas Arya (Anda)",
            university = "Universitas Indonesia",
            totalEquity = currentEquity,
            monthlyRoiPercent = currentReturnPercent,
            winRatePercent = if (tradesCount > 0) 66.7 else 0.0,
            tradesCount = tradesCount,
            badge = "Mahasiswa KSPM",
            isCurrentUser = true
        )

        val peers = LeaderboardData.defaultStudents.filter { !it.isCurrentUser }
        val all = (peers + listOf(userItem)).sortedByDescending { it.monthlyRoiPercent }

        return all.mapIndexed { index, student ->
            val rankNum = index + 1
            val assignedBadge = when (rankNum) {
                1 -> "Juara 1 Bulan Ini"
                2 -> "Juara 2"
                3 -> "Juara 3"
                in 4..5 -> "Top 5 KSPM"
                in 6..10 -> "Top 10 KSPM"
                else -> "Peserta KSPM"
            }
            student.copy(
                rank = rankNum,
                badge = if (student.isCurrentUser) "$assignedBadge (Anda)" else assignedBadge
            )
        }
    }
}
