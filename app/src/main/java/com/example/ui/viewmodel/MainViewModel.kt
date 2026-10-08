package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.CurriculumData
import com.example.data.StockMarketEngine
import com.example.data.gemini.MarketNewsRepository
import com.example.data.gemini.MarketUpdateState
import com.example.data.local.AppDatabase
import com.example.data.local.entity.ForumCommentEntity
import com.example.data.local.entity.ForumPostEntity
import com.example.data.local.entity.ModuleProgressEntity
import com.example.data.local.entity.TradeTransactionEntity
import com.example.data.model.LeaderboardStudent
import com.example.data.model.LearningModule
import com.example.data.model.PortfolioSummary
import com.example.data.model.Stock
import com.example.data.repository.InvestmentRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val repository = InvestmentRepository(db)
    private val marketNewsRepository = MarketNewsRepository()

    private val _marketUpdateState = MutableStateFlow(marketNewsRepository.getCuratedFallbackMarketUpdates())
    val marketUpdateState: StateFlow<MarketUpdateState> = _marketUpdateState.asStateFlow()

    val modules: List<LearningModule> = CurriculumData.modules

    val stocks: StateFlow<List<Stock>> = repository.stocksFlow
    val portfolioSummary: StateFlow<PortfolioSummary> = repository.portfolioSummaryFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PortfolioSummary(
            cashBalance = 100_000_000L,
            initialCapital = 100_000_000L,
            stocksValue = 0L,
            totalEquity = 100_000_000L,
            totalUnrealizedPnL = 0L,
            totalUnrealizedPnLPercent = 0.0,
            totalRealizedPnL = 0L,
            totalReturnPercent = 0.0,
            holdingsWithStock = emptyList()
        )
    )

    val transactions: StateFlow<List<TradeTransactionEntity>> = repository.transactionsFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val moduleProgress: StateFlow<List<ModuleProgressEntity>> = repository.moduleProgressFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val forumPosts: StateFlow<List<ForumPostEntity>> = repository.forumPostsFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _selectedStock = MutableStateFlow<Stock?>(null)
    val selectedStock: StateFlow<Stock?> = _selectedStock.asStateFlow()

    private val _selectedPost = MutableStateFlow<ForumPostEntity?>(null)
    val selectedPost: StateFlow<ForumPostEntity?> = _selectedPost.asStateFlow()

    val selectedPostComments: StateFlow<List<ForumCommentEntity>> = _selectedPost.flatMapLatest { post ->
        if (post != null) repository.getCommentsForPostFlow(post.id) else flowOf(emptyList())
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val leaderboard: StateFlow<List<LeaderboardStudent>> = combine(
        portfolioSummary,
        transactions
    ) { summary, txs ->
        repository.getDynamicLeaderboard(
            currentReturnPercent = summary.totalReturnPercent,
            currentEquity = summary.totalEquity,
            tradesCount = txs.size
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = repository.getDynamicLeaderboard(0.0, 100_000_000L, 0)
    )

    private val _snackbarEvent = MutableSharedFlow<String>()
    val snackbarEvent: SharedFlow<String> = _snackbarEvent.asSharedFlow()

    private val _activeTab = MutableStateFlow(NavigationTab.HOME)
    val activeTab: StateFlow<NavigationTab> = _activeTab.asStateFlow()

    init {
        // Fetch daily market updates with Google Search grounding
        refreshMarketUpdates()

        // Start background simulated price ticks for realistic trading feel
        viewModelScope.launch {
            while (true) {
                delay(3500)
                StockMarketEngine.simulateMarketTick()
                // Update selectedStock reference if active
                _selectedStock.value?.let { current ->
                    StockMarketEngine.getStock(current.ticker)?.let { updated ->
                        _selectedStock.value = updated
                    }
                }
            }
        }
    }

    fun refreshMarketUpdates() {
        viewModelScope.launch {
            _marketUpdateState.value = _marketUpdateState.value.copy(isLoading = true)
            val updated = marketNewsRepository.fetchDailyMarketUpdates()
            _marketUpdateState.value = updated
            if (updated.isGrounded) {
                _snackbarEvent.emit("Berita pasar modal terkini berhasil diperbarui via Google Search!")
            }
        }
    }

    fun setActiveTab(tab: NavigationTab) {
        _activeTab.value = tab
    }

    fun selectStock(stock: Stock?) {
        _selectedStock.value = stock
    }

    fun selectPost(post: ForumPostEntity?) {
        _selectedPost.value = post
    }

    fun buyStock(ticker: String, lots: Int) {
        viewModelScope.launch {
            val result = repository.buyStock(ticker, lots)
            result.onSuccess { msg ->
                _snackbarEvent.emit(msg)
            }.onFailure { err ->
                _snackbarEvent.emit(err.message ?: "Gagal membeli saham")
            }
        }
    }

    fun sellStock(ticker: String, lots: Int) {
        viewModelScope.launch {
            val result = repository.sellStock(ticker, lots)
            result.onSuccess { msg ->
                _snackbarEvent.emit(msg)
            }.onFailure { err ->
                _snackbarEvent.emit(err.message ?: "Gagal menjual saham")
            }
        }
    }

    fun resetPortfolio() {
        viewModelScope.launch {
            val result = repository.resetPortfolio()
            result.onSuccess { msg ->
                _snackbarEvent.emit(msg)
            }
        }
    }

    fun submitQuizScore(moduleId: String, score: Int, maxScore: Int) {
        viewModelScope.launch {
            repository.saveModuleQuizResult(moduleId, score, maxScore)
            _snackbarEvent.emit("Selamat! Kuis modul berhasil diselesaikan. Skor: $score/$maxScore")
        }
    }

    fun createForumPost(title: String, content: String, tag: String, stockTicker: String?) {
        viewModelScope.launch {
            val result = repository.addForumPost(title, content, tag, stockTicker)
            result.onSuccess { msg ->
                _snackbarEvent.emit(msg)
            }.onFailure { err ->
                _snackbarEvent.emit(err.message ?: "Gagal memposting diskusi")
            }
        }
    }

    fun toggleLike(postId: String) {
        viewModelScope.launch {
            repository.toggleLikePost(postId)
            // If current post is open, update its state
            _selectedPost.value?.let { current ->
                if (current.id == postId) {
                    val newLiked = !current.isLikedByMe
                    val newCount = if (newLiked) current.likesCount + 1 else (current.likesCount - 1).coerceAtLeast(0)
                    _selectedPost.value = current.copy(isLikedByMe = newLiked, likesCount = newCount)
                }
            }
        }
    }

    fun addComment(postId: String, content: String) {
        viewModelScope.launch {
            val result = repository.addForumComment(postId, content)
            result.onSuccess { msg ->
                _snackbarEvent.emit(msg)
            }.onFailure { err ->
                _snackbarEvent.emit(err.message ?: "Gagal mengirim komentar")
            }
        }
    }
}

enum class NavigationTab(val title: String) {
    HOME("Beranda"),
    LEARN("Modul"),
    TRADE("Simulasi"),
    NEWS("Berita"),
    FORUM("Forum"),
    LEADERBOARD("Peringkat"),
    WEB("Portal Web")
}
