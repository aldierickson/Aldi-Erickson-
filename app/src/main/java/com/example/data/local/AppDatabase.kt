package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.local.dao.ForumDao
import com.example.data.local.dao.ModuleDao
import com.example.data.local.dao.PortfolioDao
import com.example.data.local.dao.TradeDao
import com.example.data.local.entity.ForumCommentEntity
import com.example.data.local.entity.ForumPostEntity
import com.example.data.local.entity.ModuleProgressEntity
import com.example.data.local.entity.StockHoldingEntity
import com.example.data.local.entity.TradeTransactionEntity
import com.example.data.local.entity.UserPortfolioEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserPortfolioEntity::class,
        StockHoldingEntity::class,
        TradeTransactionEntity::class,
        ModuleProgressEntity::class,
        ForumPostEntity::class,
        ForumCommentEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun portfolioDao(): PortfolioDao
    abstract fun tradeDao(): TradeDao
    abstract fun moduleDao(): ModuleDao
    abstract fun forumDao(): ForumDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "kspm_invest_db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Populate default user portfolio and seed forum
                            CoroutineScope(Dispatchers.IO).launch {
                                getInstance(context).populateInitialData()
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    suspend fun populateInitialData() {
        val pDao = portfolioDao()
        if (pDao.getPortfolio() == null) {
            pDao.insertOrUpdatePortfolio(
                UserPortfolioEntity(
                    id = 1,
                    cashBalance = 100_000_000L,
                    initialCapital = 100_000_000L,
                    userName = "Dimas Arya (KSPM UI)",
                    university = "Universitas Indonesia"
                )
            )
        }

        val fDao = forumDao()
        val defaultPosts = listOf(
            ForumPostEntity(
                id = "post-pin-1",
                authorName = "Aditya Pratama",
                authorBadge = "Ketua Umum KSPM",
                university = "Universitas Indonesia",
                tag = "#Pengumuman",
                stockTicker = null,
                title = "Selamat Datang di Simulasi Portofolio KSPM Q4 2026!",
                content = "Halo rekan-rekan mahasiswa dan investor muda! Kompetisi simulasi trading bulanan resmi dibuka dengan modal virtual Rp 100 Juta. Terapkan money management yang bijak, analisa teknikal & fundamental secara disiplin. Peringkat 1-3 di akhir bulan akan mendapatkan sertifikat resmi KSPM dan subsidi pembukaan RDN riil!",
                likesCount = 42,
                commentsCount = 6,
                timestamp = System.currentTimeMillis() - 86400000L * 2,
                isPinned = true,
                isLikedByMe = true
            ),
            ForumPostEntity(
                id = "post-2",
                authorName = "Nabila Putri",
                authorBadge = "Kepala Divisi Riset",
                university = "Universitas Gadjah Mada",
                tag = "#AnalisisSaham",
                stockTicker = "BBCA",
                title = "Analisa Fundamental BBCA: Kredit Tumbuh 14.5% YoY, Rasio CASA Kokoh",
                content = "Berdasarkan laporan keuangan kuartal terbaru, BBCA kembali membukukan laba bersih solid didorong pertumbuhan kredit korporasi dan konsumer. CASA ratio di atas 80% membuat CoF tetap terjaga di tengah era suku bunga tinggi. Target price konsensus analis berada di kisaran Rp 11.200. Bagaimana menurut kawan-kawan KSPM?",
                likesCount = 28,
                commentsCount = 4,
                timestamp = System.currentTimeMillis() - 3600000L * 5,
                isPinned = false,
                isLikedByMe = false
            ),
            ForumPostEntity(
                id = "post-3",
                authorName = "Farhan Maulana",
                authorBadge = "Anggota Divisi Pasar Modal",
                university = "Institut Teknologi Bandung",
                tag = "#AnalisisTeknikal",
                stockTicker = "TLKM",
                title = "TLKM Rebound dari Support Kuat Rp 2.850 dengan Golden Cross RSI",
                content = "Secara teknikal daily chart, TLKM telah menguji level horizontal support 2850 dan membentuk candle hammer disertai lonjakan volume akumulasi. Indikator MACD bersiap golden cross di area oversold. Risk/Reward ratio menarik untuk swing trade dengan cutloss ketat di bawah 2800.",
                likesCount = 19,
                commentsCount = 3,
                timestamp = System.currentTimeMillis() - 3600000L * 12,
                isPinned = false,
                isLikedByMe = false
            ),
            ForumPostEntity(
                id = "post-4",
                authorName = "Rizky Ramadhan",
                authorBadge = "Mahasiswa Baru",
                university = "Universitas Diponegoro",
                tag = "#TanyaSenior",
                stockTicker = null,
                title = "Tanya Senior KSPM: Kapan Waktu Terbaik Cutloss bagi Pemula?",
                content = "Izin bertanya untuk para senior, kalau kita beli saham untuk trading swing, biasanya berapa toleransi cutloss yang ideal? Apakah 3-5% atau menunggu penembusan garis support? Mohon tips money management-nya untuk kami mahasiswa pemula.",
                likesCount = 15,
                commentsCount = 5,
                timestamp = System.currentTimeMillis() - 86400000L * 1,
                isPinned = false,
                isLikedByMe = false
            )
        )
        fDao.insertPosts(defaultPosts)

        val defaultComments = listOf(
            ForumCommentEntity(
                id = "c-1",
                postId = "post-pin-1",
                authorName = "Kevin Setiawan",
                authorBadge = "Anggota Aktif",
                university = "Universitas Airlangga",
                content = "Semangat berkompetisi semuanya! Target bulan ini tembus ROI 15% amin!",
                timestamp = System.currentTimeMillis() - 86400000L
            ),
            ForumCommentEntity(
                id = "c-2",
                postId = "post-pin-1",
                authorName = "Siti Aisyah",
                authorBadge = "Sekretaris KSPM",
                university = "Universitas Indonesia",
                content = "Jangan lupa selesaikan juga kuis di modul belajar untuk dapat tambahan sertifikat ya teman-teman!",
                timestamp = System.currentTimeMillis() - 72000000L
            ),
            ForumCommentEntity(
                id = "c-3",
                postId = "post-4",
                authorName = "Nabila Putri",
                authorBadge = "Kepala Divisi Riset",
                university = "Universitas Gadjah Mada",
                content = "Untuk pemula, patuhi aturan max risk 1-2% dari total modal per transaksi. Kalau cutloss jaraknya 4%, sesuaikan lot agar nominal risiko tidak melebihi modal toleransi. Jangan pernah averaging down di saham yang terus downtrend!",
                timestamp = System.currentTimeMillis() - 3600000L * 8
            )
        )
        fDao.insertComments(defaultComments)
    }
}
