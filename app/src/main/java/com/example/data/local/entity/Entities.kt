package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_portfolio")
data class UserPortfolioEntity(
    @PrimaryKey val id: Int = 1,
    val cashBalance: Long = 100_000_000L,
    val initialCapital: Long = 100_000_000L,
    val userName: String = "Dimas Arya (KSPM UI)",
    val university: String = "Universitas Indonesia"
)

@Entity(tableName = "stock_holdings")
data class StockHoldingEntity(
    @PrimaryKey val ticker: String,
    val stockName: String,
    val lots: Int,
    val averagePrice: Double
)

@Entity(tableName = "trade_transactions")
data class TradeTransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val ticker: String,
    val stockName: String,
    val type: String, // "BUY" or "SELL"
    val lots: Int,
    val pricePerShare: Long,
    val totalValue: Long,
    val fee: Long,
    val realizedPnL: Long,
    val timestamp: Long
)

@Entity(tableName = "module_progress")
data class ModuleProgressEntity(
    @PrimaryKey val moduleId: String,
    val completed: Boolean,
    val quizScore: Int,
    val maxScore: Int,
    val completedAt: Long
)

@Entity(tableName = "forum_posts")
data class ForumPostEntity(
    @PrimaryKey val id: String,
    val authorName: String,
    val authorBadge: String,
    val university: String,
    val tag: String,
    val stockTicker: String?,
    val title: String,
    val content: String,
    val likesCount: Int,
    val commentsCount: Int,
    val timestamp: Long,
    val isPinned: Boolean = false,
    val isLikedByMe: Boolean = false
)

@Entity(tableName = "forum_comments")
data class ForumCommentEntity(
    @PrimaryKey val id: String,
    val postId: String,
    val authorName: String,
    val authorBadge: String,
    val university: String,
    val content: String,
    val timestamp: Long
)
