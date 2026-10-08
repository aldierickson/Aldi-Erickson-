package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.ForumCommentEntity
import com.example.data.local.entity.ForumPostEntity
import com.example.data.local.entity.ModuleProgressEntity
import com.example.data.local.entity.StockHoldingEntity
import com.example.data.local.entity.TradeTransactionEntity
import com.example.data.local.entity.UserPortfolioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PortfolioDao {
    @Query("SELECT * FROM user_portfolio WHERE id = 1 LIMIT 1")
    fun getPortfolioFlow(): Flow<UserPortfolioEntity?>

    @Query("SELECT * FROM user_portfolio WHERE id = 1 LIMIT 1")
    suspend fun getPortfolio(): UserPortfolioEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdatePortfolio(portfolio: UserPortfolioEntity)

    @Query("UPDATE user_portfolio SET cashBalance = :newBalance WHERE id = 1")
    suspend fun updateCashBalance(newBalance: Long)

    @Query("SELECT * FROM stock_holdings WHERE lots > 0")
    fun getAllHoldingsFlow(): Flow<List<StockHoldingEntity>>

    @Query("SELECT * FROM stock_holdings WHERE ticker = :ticker LIMIT 1")
    suspend fun getHolding(ticker: String): StockHoldingEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateHolding(holding: StockHoldingEntity)

    @Query("DELETE FROM stock_holdings WHERE ticker = :ticker")
    suspend fun deleteHolding(ticker: String)

    @Query("DELETE FROM stock_holdings")
    suspend fun clearHoldings()
}

@Dao
interface TradeDao {
    @Query("SELECT * FROM trade_transactions ORDER BY timestamp DESC")
    fun getAllTransactionsFlow(): Flow<List<TradeTransactionEntity>>

    @Insert
    suspend fun insertTransaction(transaction: TradeTransactionEntity): Long

    @Query("SELECT COUNT(*) FROM trade_transactions")
    suspend fun getTransactionCount(): Int

    @Query("DELETE FROM trade_transactions")
    suspend fun clearTransactions()
}

@Dao
interface ModuleDao {
    @Query("SELECT * FROM module_progress")
    fun getAllProgressFlow(): Flow<List<ModuleProgressEntity>>

    @Query("SELECT * FROM module_progress WHERE moduleId = :id LIMIT 1")
    suspend fun getProgress(id: String): ModuleProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProgress(progress: ModuleProgressEntity)

    @Query("DELETE FROM module_progress")
    suspend fun clearProgress()
}

@Dao
interface ForumDao {
    @Query("SELECT * FROM forum_posts ORDER BY isPinned DESC, timestamp DESC")
    fun getAllPostsFlow(): Flow<List<ForumPostEntity>>

    @Query("SELECT * FROM forum_posts WHERE tag = :tag ORDER BY isPinned DESC, timestamp DESC")
    fun getPostsByTagFlow(tag: String): Flow<List<ForumPostEntity>>

    @Query("SELECT * FROM forum_posts WHERE id = :id LIMIT 1")
    suspend fun getPostById(id: String): ForumPostEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: ForumPostEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<ForumPostEntity>)

    @Update
    suspend fun updatePost(post: ForumPostEntity)

    @Query("SELECT * FROM forum_comments WHERE postId = :postId ORDER BY timestamp ASC")
    fun getCommentsForPostFlow(postId: String): Flow<List<ForumCommentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(comment: ForumCommentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComments(comments: List<ForumCommentEntity>)
}
