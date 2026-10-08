package com.example.data

import com.example.data.model.LeaderboardStudent

object LeaderboardData {
    val defaultStudents = listOf(
        LeaderboardStudent(
            rank = 1,
            name = "Rafi Alamsyah",
            university = "Universitas Indonesia",
            totalEquity = 124850000L,
            monthlyRoiPercent = 24.85,
            winRatePercent = 82.5,
            tradesCount = 28,
            badge = "Juara 1 Bulan Ini",
            isCurrentUser = false
        ),
        LeaderboardStudent(
            rank = 2,
            name = "Anindya Pramesti",
            university = "Universitas Gadjah Mada",
            totalEquity = 119420000L,
            monthlyRoiPercent = 19.42,
            winRatePercent = 75.0,
            tradesCount = 22,
            badge = "Juara 2",
            isCurrentUser = false
        ),
        LeaderboardStudent(
            rank = 3,
            name = "Bintang Wicaksono",
            university = "Institut Teknologi Bandung",
            totalEquity = 116300000L,
            monthlyRoiPercent = 16.30,
            winRatePercent = 71.4,
            tradesCount = 19,
            badge = "Juara 3",
            isCurrentUser = false
        ),
        LeaderboardStudent(
            rank = 4,
            name = "Dian Permata",
            university = "Universitas Diponegoro",
            totalEquity = 112700000L,
            monthlyRoiPercent = 12.70,
            winRatePercent = 68.0,
            tradesCount = 25,
            badge = "Top 5 KSPM",
            isCurrentUser = false
        ),
        LeaderboardStudent(
            rank = 5,
            name = "Fahri Hidayat",
            university = "Universitas Airlangga",
            totalEquity = 109800000L,
            monthlyRoiPercent = 9.80,
            winRatePercent = 64.2,
            tradesCount = 18,
            badge = "Top 5 KSPM",
            isCurrentUser = false
        ),
        LeaderboardStudent(
            rank = 6,
            name = "Sarah Wulandari",
            university = "Universitas Brawijaya",
            totalEquity = 107250000L,
            monthlyRoiPercent = 7.25,
            winRatePercent = 60.0,
            tradesCount = 15,
            badge = "Top 10 KSPM",
            isCurrentUser = false
        ),
        LeaderboardStudent(
            rank = 7,
            name = "Dimas Arya (Anda)",
            university = "Universitas Indonesia",
            totalEquity = 100000000L,
            monthlyRoiPercent = 0.0,
            winRatePercent = 0.0,
            tradesCount = 0,
            badge = "Mahasiswa KSPM",
            isCurrentUser = true
        ),
        LeaderboardStudent(
            rank = 8,
            name = "Gilang Pratama",
            university = "Universitas Padjadjaran",
            totalEquity = 98400000L,
            monthlyRoiPercent = -1.60,
            winRatePercent = 45.0,
            tradesCount = 14,
            badge = "Peserta Aktif",
            isCurrentUser = false
        ),
        LeaderboardStudent(
            rank = 9,
            name = "Tiara Maharani",
            university = "Institut Teknologi Sepuluh Nopember",
            totalEquity = 96200000L,
            monthlyRoiPercent = -3.80,
            winRatePercent = 40.0,
            tradesCount = 16,
            badge = "Peserta Aktif",
            isCurrentUser = false
        ),
        LeaderboardStudent(
            rank = 10,
            name = "Rendi Saputra",
            university = "Universitas Sumatera Utara",
            totalEquity = 92500000L,
            monthlyRoiPercent = -7.50,
            winRatePercent = 35.0,
            tradesCount = 20,
            badge = "Peserta Aktif",
            isCurrentUser = false
        )
    )
}
