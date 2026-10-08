package com.example.data

import com.example.data.model.LearningModule
import com.example.data.model.LessonSection
import com.example.data.model.QuizQuestion

object CurriculumData {
    val modules = listOf(
        LearningModule(
            id = "mod-1",
            title = "Pengenalan Pasar Modal & Mekanisme BEI",
            category = "Dasar Pasar Modal",
            level = "Pemula",
            readTimeMinutes = 6,
            summary = "Pahami struktur ekosistem pasar modal Indonesia (OJK, BEI, KPEI, KSEI), satuan lot, fraksi harga, dan aturan ARA/ARB.",
            sections = listOf(
                LessonSection(
                    title = "1. Struktur Pasar Modal Indonesia",
                    content = "Pasar modal Indonesia diawasi oleh Otoritas Jasa Keuangan (OJK). Di bawahnya terdapat Self-Regulatory Organizations (SRO) yang terdiri dari:\n" +
                            "• PT Bursa Efek Indonesia (BEI/IDX): Penyelenggara perdagangan efek.\n" +
                            "• PT Kliring Penjaminan Efek Indonesia (KPEI): Pengelola kliring dan penjaminan transaksi bursa.\n" +
                            "• PT Kustodian Sentral Efek Indonesia (KSEI): Penyimpan dan pencatat kepemilikan efek dan dana investor.",
                    keyPoints = listOf(
                        "OJK sebagai regulator tertinggi sektor jasa keuangan",
                        "SRO terdiri dari 3 pilar: BEI, KPEI, dan KSEI",
                        "Investor dilindungi oleh SID (Single Investor Identification) dari KSEI"
                    ),
                    tip = "Sebagai mahasiswa KSPM, kamu wajib memiliki SID resmi untuk berinvestasi riil di masa depan!"
                ),
                LessonSection(
                    title = "2. Satuan Perdagangan & Fraksi Harga IDX",
                    content = "Di Bursa Efek Indonesia, perdagangan saham dilakukan dalam satuan Lot.\n" +
                            "• 1 Lot = 100 lembar saham.\n" +
                            "Perubahan harga saham diatur berdasarkan Fraksi Harga (Tick Size):\n" +
                            "• < Rp 200: Fraksi Rp 1 (maksimal perubahan Rp 10)\n" +
                            "• Rp 200 - Rp 500: Fraksi Rp 2 (maksimal perubahan Rp 20)\n" +
                            "• Rp 500 - Rp 2.000: Fraksi Rp 5 (maksimal perubahan Rp 50)\n" +
                            "• Rp 2.000 - Rp 5.000: Fraksi Rp 10 (maksimal perubahan Rp 100)\n" +
                            "• ≥ Rp 5.000: Fraksi Rp 25 (maksimal perubahan Rp 250)",
                    keyPoints = listOf(
                        "1 Lot standar = 100 lembar saham",
                        "Fraksi harga menjaga likuiditas dan ketertiban antrian bid-offer",
                        "Settlement transaksi saham menggunakan sistem T+2 (hari bursa kedua)"
                    ),
                    tip = "Contoh: Jika harga saham BBCA adalah Rp 10.000, kelipatan naik-turunnya adalah Rp 25 per tick."
                ),
                LessonSection(
                    title = "3. Auto Rejection Asimetris (ARA & ARB)",
                    content = "Auto Rejection adalah mekanisme pembatasan kenaikan dan penurunan harga saham maksimum dalam 1 hari bursa untuk melindungi investor dari volatilitas ekstrem:\n" +
                            "• Batas ARA (Kenaikan) dan ARB (Penurunan) bervariasi sesuai rentang harga (20% - 35%).\n" +
                            "Jika pesanan beli atau jual melebihi persentase batas tersebut, sistem JATS akan otomatis menolak order.",
                    keyPoints = listOf(
                        "ARA mencegah manipulasi pompa harga berlebihan",
                        "ARB membatasi kerugian harian investor",
                        "Saham yang menyentuh batas disebut terkunci ARA atau ARB"
                    ),
                    tip = "Waspadai saham dengan kapitalisasi pasar kecil yang rentan menyentuh batas auto rejection!"
                )
            ),
            quiz = listOf(
                QuizQuestion(
                    id = 1,
                    question = "Berapa jumlah lembar saham dalam 1 lot di Bursa Efek Indonesia?",
                    options = listOf("50 lembar", "100 lembar", "500 lembar", "1.000 lembar"),
                    correctIndex = 1,
                    explanation = "Sesuai regulasi BEI, 1 lot saham sama dengan 100 lembar saham."
                ),
                QuizQuestion(
                    id = 2,
                    question = "Lembaga yang bertindak sebagai kustodian sentral dan pencatat kepemilikan efek investor adalah...",
                    options = listOf("BEI", "KPEI", "KSEI", "OJK"),
                    correctIndex = 2,
                    explanation = "KSEI (Kustodian Sentral Efek Indonesia) bertugas menyimpan dan mengadministrasikan efek milik investor."
                ),
                QuizQuestion(
                    id = 3,
                    question = "Jika harga saham BBRI berada di level Rp 4.500, berapa fraksi harga (tick size) perubahannya?",
                    options = listOf("Rp 2", "Rp 5", "Rp 10", "Rp 25"),
                    correctIndex = 2,
                    explanation = "Rentang harga Rp 2.000 hingga Rp 5.000 memiliki fraksi harga Rp 10."
                ),
                QuizQuestion(
                    id = 4,
                    question = "Berapa lama siklus penyelesaian transaksi efek di bursa Indonesia saat ini?",
                    options = listOf("T+0 (Hari itu juga)", "T+1", "T+2", "T+3"),
                    correctIndex = 2,
                    explanation = "Bursa Efek Indonesia menerapkan siklus settlement T+2 (dua hari bursa setelah transaksi)."
                )
            )
        ),
        LearningModule(
            id = "mod-2",
            title = "Analisis Fundamental & Rasio Keuangan",
            category = "Analisis Fundamental",
            level = "Menengah",
            readTimeMinutes = 8,
            summary = "Kuasai cara membaca laporan keuangan emiten dan kalkulasi rasio kunci: PER, PBV, ROE, DER, serta Dividend Yield.",
            sections = listOf(
                LessonSection(
                    title = "1. Tiga Laporan Keuangan Utama",
                    content = "Dalam analisa fundamental, kita mempelajari kinerja bisnis riil emiten melalui:\n" +
                            "1. Neraca (Balance Sheet): Posisi Aset, Liabilitas (Hutang), dan Ekuitas (Modal bersih).\n" +
                            "2. Laporan Laba Rugi (Income Statement): Pendapatan/Revenue, Laba Kotor, Laba Usaha, dan Laba Bersih (Net Income).\n" +
                            "3. Laporan Arus Kas (Cash Flow): Arus kas operasi (CFO), investasi (CFI), dan pendanaan (CFF).",
                    keyPoints = listOf(
                        "Rumus dasar neraca: Aset = Liabilitas + Ekuitas",
                        "Laba bersih yang riil tercermin pada Arus Kas Operasi yang positif",
                        "Pertumbuhan laba konsisten (CAGR) adalah indikator emiten unggul"
                    ),
                    tip = "Fokus pada laba bersih dari kegiatan operasional utama, bukan keuntungan sesaat dari penjualan aset!"
                ),
                LessonSection(
                    title = "2. Rasio Valuasi: PER & PBV",
                    content = "• PER (Price to Earnings Ratio) = Harga Saham / Laba Per Saham (EPS). Mengukur berapa tahun modal investor kembali berdasarkan laba per lembar saham.\n" +
                            "• PBV (Price to Book Value) = Harga Saham / Nilai Buku per Saham (BVPS). Menunjukkan berapa kali lipat harga saham dibandingkan modal bersih perusahaan.",
                    keyPoints = listOf(
                        "PER rendah di sektornya mengindikasikan valuasi relatif murah (undervalued)",
                        "PBV < 1 berarti harga saham di bawah modal bersih pembukuan",
                        "Bandingkan PER dan PBV dengan peers dalam sektor industri yang sama"
                    ),
                    tip = "Saham bank besar seperti BBCA wajar memiliki PBV tinggi karena kualitas profitabilitas (ROE) yang sangat prima."
                ),
                LessonSection(
                    title = "3. Rasio Profitabilitas & Kesehatan Finansial",
                    content = "• ROE (Return on Equity) = (Laba Bersih / Ekuitas) × 100%. Menilai efektivitas manajemen mencetak profit dari modal pemegang saham. KSPM acuan sehat: ROE > 15%.\n" +
                            "• DER (Debt to Equity Ratio) = Total Hutang / Total Ekuitas. Mengukur tingkat leverage/risiko hutang. Idealnya DER < 1 (atau < 100%).\n" +
                            "• Dividend Yield = (Dividen per Lembar / Harga Saham) × 100%. Imbal hasil dividen tunai tahunan.",
                    keyPoints = listOf(
                        "ROE tinggi mencerminkan keunggulan bersaing (economic moat)",
                        "DER yang terlalu tinggi berbahaya saat terjadi kenaikan suku bunga",
                        "Dividend yield > 5% menjadi bantalan aman untuk investor defensif"
                    ),
                    tip = "Kombinasikan ROE tinggi (>15%) dengan DER sehat (<1) untuk menemukan calon saham multi-bagger berfundamental kokoh."
                )
            ),
            quiz = listOf(
                QuizQuestion(
                    id = 1,
                    question = "Rasio yang membandingkan laba bersih perusahaan dengan total modal pemegang saham adalah...",
                    options = listOf("Debt to Equity Ratio (DER)", "Return on Equity (ROE)", "Price to Earnings Ratio (PER)", "Current Ratio"),
                    correctIndex = 1,
                    explanation = "ROE (Return on Equity) mengukur seberapa efisien manajemen menghasilkan keuntungan dari ekuitas pemilik saham."
                ),
                QuizQuestion(
                    id = 2,
                    question = "Jika harga saham Rp 5.000 dan Laba per Saham (EPS) adalah Rp 500, berapa nilai PER saham tersebut?",
                    options = listOf("5x", "10x", "15x", "25x"),
                    correctIndex = 1,
                    explanation = "PER = Harga Saham / EPS = 5.000 / 500 = 10x."
                ),
                QuizQuestion(
                    id = 3,
                    question = "Apa makna rasio DER (Debt to Equity Ratio) bernilai lebih dari 2.0 pada perusahaan non-finansial?",
                    options = listOf("Perusahaan tidak memiliki hutang", "Jumlah hutang 2x lebih besar daripada modal bersih", "Laba bersih meningkat dua kali lipat", "Perusahaan sangat aman dari risiko kredit"),
                    correctIndex = 1,
                    explanation = "DER > 2.0 berarti total liabilitas bernilai 2 kali lipat dari modal sendiri, menandakan tingkat beban hutang yang tinggi."
                ),
                QuizQuestion(
                    id = 4,
                    question = "Dividen tahunan per lembar adalah Rp 300, dan harga saham saat ini Rp 6.000. Berapa Dividend Yield-nya?",
                    options = listOf("3%", "5%", "8%", "10%"),
                    correctIndex = 1,
                    explanation = "Dividend Yield = (300 / 6.000) * 100% = 5%."
                )
            )
        ),
        LearningModule(
            id = "mod-3",
            title = "Analisis Teknikal: Candlestick, Trend & Indikator",
            category = "Analisis Teknikal",
            level = "Menengah",
            readTimeMinutes = 7,
            summary = "Pelajari psikologi candlestick, identifikasi trend utama, horizontal support-resistance, serta sinyal RSI & MACD.",
            sections = listOf(
                LessonSection(
                    title = "1. Membaca Anatomi Candlestick",
                    content = "Satu candlestick merangkum 4 informasi harga dalam rentang waktu tertentu: Open, High, Low, Close (OHLC).\n" +
                            "• Bullish Candle (Hijau): Close > Open. Pembeli mendominasi.\n" +
                            "• Bearish Candle (Merah): Close < Open. Penjual mendominasi.\n" +
                            "Pola Penting:\n" +
                            "• Hammer / Inverted Hammer: Indikasi penolakan harga bawah di area support (bullish reversal).\n" +
                            "• Bullish Engulfing: Badan candle hijau menelan penuh candle merah sebelumnya.",
                    keyPoints = listOf(
                        "Sumbu bawah yang panjang menandakan tekanan beli (rejection level)",
                        "Pola candlestick wajib dikonfirmasi dengan volume perdagangan",
                        "Hindari mengambil posisi hanya dari 1 candle tanpa melihat konteks trend"
                    ),
                    tip = "Volume tinggi yang menyertai candle breakout menandakan partisipasi institusi besar (smart money)!"
                ),
                LessonSection(
                    title = "2. Identifikasi Trend & Garis S/R",
                    content = "Prinsip utama: The trend is your friend!\n" +
                            "• Uptrend: Terbentuk Higher High (HH) dan Higher Low (HL).\n" +
                            "• Downtrend: Terbentuk Lower High (LH) dan Lower Low (LL).\n" +
                            "• Sideways: Harga berkonsolidasi dalam rentang batas atas (Resistance) dan batas bawah (Support).\n" +
                            "Support adalah lantai harga di mana minat beli cukup kuat menghentikan penurunan.",
                    keyPoints = listOf(
                        "Jangan melawan trend utama (Don't fight the primary trend)",
                        "Resistance yang berhasil ditembus berubah fungsi menjadi Support (RBS)",
                        "Support yang jebol berubah menjadi Resistance baru (SBR)"
                    ),
                    tip = "Beli di dekat area support kuat dan jual saat mendekati resistance untuk memaksimalkan risk-reward ratio."
                ),
                LessonSection(
                    title = "3. Indikator Momentum: RSI & MA",
                    content = "• Moving Average (MA): Garis rata-rata harga (MA20 untuk short-term, MA50 mid-term, MA200 long-term). Golden Cross terjadi saat MA jangka pendek memotong MA jangka panjang ke atas.\n" +
                            "• Relative Strength Index (RSI): Skala 0-100.\n" +
                            "  - RSI < 30: Kondisi Jenuh Jual (Oversold), potensi pantulan teknikal.\n" +
                            "  - RSI > 70: Kondisi Jenuh Beli (Overbought), rawan aksi profit taking.",
                    keyPoints = listOf(
                        "Gunakan MA sebagai support dinamis saat harga uptrend",
                        "RSI Divergence merupakan sinyal awal pembalikan arah harga yang akurat",
                        "Jangan gunakan indikator secara berlebihan (analisis paralysis)"
                    ),
                    tip = "Kombinasi MA20 + RSI + Volume adalah senjata ampuh trader swing mahasiswa!"
                )
            ),
            quiz = listOf(
                QuizQuestion(
                    id = 1,
                    question = "Apa arti pola 'Hammer' dengan sumbu bawah panjang yang muncul di dekat garis support?",
                    options = listOf("Tekanan jual bertambah kuat", "Potensi pembalikan arah naik (Bullish Reversal)", "Harga pasti akan turun terus", "Bursa akan ditutup lebih awal"),
                    correctIndex = 1,
                    explanation = "Hammer dengan lower shadow panjang menunjukkan bahwa penjual sempat menekan namun pembeli berhasil mendorong harga kembali naik (buyer rejection)."
                ),
                QuizQuestion(
                    id = 2,
                    question = "Kondisi RSI berada di bawah angka 30 lazim diinterpretasikan sebagai...",
                    options = listOf("Overbought (Jenuh Beli)", "Oversold (Jenuh Jual)", "Normal Sideways", "Volume Meledak"),
                    correctIndex = 1,
                    explanation = "RSI di bawah 30 menunjukkan fase oversold di mana penurunan sudah cukup jenuh dan berpotensi memicu rebound."
                ),
                QuizQuestion(
                    id = 3,
                    question = "Fenomena ketika Moving Average jangka pendek (misal MA20) memotong ke atas MA jangka panjang (misal MA50) disebut...",
                    options = listOf("Death Cross", "Golden Cross", "Dead Cat Bounce", "Breakdown"),
                    correctIndex = 1,
                    explanation = "Golden Cross adalah sinyal teknikal bullish yang mengindikasikan momentum kenaikan jangka menengah mulai dominan."
                ),
                QuizQuestion(
                    id = 4,
                    question = "Konsep Support Become Resistance (SBR) terjadi ketika...",
                    options = listOf("Harga menembus resistance ke atas", "Level support lama berhasil dijebol ke bawah dan kini menjadi batas penahan kenaikan baru", "Harga saham tidak bergerak sama sekali", "Volume perdagangan nol"),
                    correctIndex = 1,
                    explanation = "Ketika lantai support breakdown, level tersebut secara psikologis beralih fungsi menjadi resistance baru bagi pantulan harga."
                )
            )
        ),
        LearningModule(
            id = "mod-4",
            title = "Risk Management, Money Management & Psikologi",
            category = "Manajemen Risiko",
            level = "Lanjutan",
            readTimeMinutes = 6,
            summary = "Kunci sukses bertahan di pasar modal: Aturan risiko 2%, kalkulasi posisi lot, disiplin cutloss, dan mitigasi FOMO.",
            sections = listOf(
                LessonSection(
                    title = "1. Aturan Emas 1-2% Maximum Risk",
                    content = "Banyak pemula gagal bukan karena salah analisa, melainkan karena salah mengatur ukuran posisi (position sizing).\n" +
                            "Aturan baku: Jangan pernah merisikokan lebih dari 1-2% total portofolio pada satu transaksi tunggal!\n" +
                            "Contoh Modal Rp 100 Juta:\n" +
                            "• Maksimal risiko kerugian (1%) = Rp 1.000.000.\n" +
                            "• Jika beli saham di Rp 5.000 dengan cutloss di Rp 4.750 (rugi Rp 250/lembar atau Rp 25.000/lot),\n" +
                            "• Maka kapasitas pembelian = Rp 1.000.000 / Rp 25.000 = 40 Lot.",
                    keyPoints = listOf(
                        "Batas risiko membatasi kerugian beruntun dari menghabiskan modal",
                        "Hitung risiko dulu sebelum memimpikan potensi keuntungan",
                        "Sesuaikan jumlah lot dengan jarak level cutloss"
                    ),
                    tip = "Dengan aturan 1%, kamu bisa salah 10 kali berturut-turut dan portofolio kamu masih tersisa lebih dari 90% modal!"
                ),
                LessonSection(
                    title = "2. Risk to Reward Ratio (RRR)",
                    content = "Selalu bidik transaksi dengan minimal Risk to Reward 1:2 atau 1:3.\n" +
                            "• Jika risiko kerugian adalah 3%, target profit minimal harus 6-9%.\n" +
                            "Dengan win-rate 40% sekalipun, portofolio kamu tetap mencatatkan keuntungan bersih berkat rasio reward yang jauh lebih besar dibanding nominal risiko.",
                    keyPoints = listOf(
                        "Minimal RRR 1:2 sebelum mengeksekusi order buy",
                        "Tentukan Titik Entry, Stop Loss, dan Target Take Profit di trading plan",
                        "Disiplin mengeksekusi cutloss saat rencana tidak berjalan sesuai prediksi"
                    ),
                    tip = "Jangan pernah mengubah level stop loss menjadi lebih jauh ke bawah saat harga saham mulai turun!"
                ),
                LessonSection(
                    title = "3. Mengendalikan Emosi & Menghindari FOMO",
                    content = "Dua musuh terbesar investor mahasiswa adalah Keserakahan (Greed) dan Ketakutan (Fear).\n" +
                            "• FOMO (Fear of Missing Out): Tergoda mengejar saham yang sudah naik 20% di pucuk karena pom-pom di medsos.\n" +
                            "• Revenge Trading: Membeli ugal-ugalan untuk membalas dendam setelah baru saja cutloss.\n" +
                            "Ingat moto KSPM: 'Pasar modal buka setiap hari bursa, peluang baru akan selalu ada besok!'.",
                    keyPoints = listOf(
                        "Hindari membeli saham hanya karena ramai di grup chat atau media sosial",
                        "Catat setiap transaksi dalam Trading Journal untuk evaluasi berkala",
                        "Konsistensi jangka panjang mengalahkan keberuntungan satu malam"
                    ),
                    tip = "KSPM Invest dirancang agar kamu berani bereksperimen di simulator sebelum mengelola uang sungguhan."
                )
            ),
            quiz = listOf(
                QuizQuestion(
                    id = 1,
                    question = "Dengan modal Rp 100 Juta dan aturan max risk 1%, berapa rupiah toleransi kerugian maksimal per transaksi?",
                    options = listOf("Rp 500.000", "Rp 1.000.000", "Rp 5.000.000", "Rp 10.000.000"),
                    correctIndex = 1,
                    explanation = "1% dari modal Rp 100.000.000 adalah Rp 1.000.000."
                ),
                QuizQuestion(
                    id = 2,
                    question = "Jika potensi cutloss adalah Rp 200 per lembar, berapa target profit minimal agar memenuhi Risk to Reward 1:2?",
                    options = listOf("Rp 100", "Rp 200", "Rp 400", "Rp 600"),
                    correctIndex = 2,
                    explanation = "Pada rasio 1:2, target reward minimal dua kali lipat risiko, yaitu 2 x Rp 200 = Rp 400."
                ),
                QuizQuestion(
                    id = 3,
                    question = "Tindakan langsung membeli secara tergesa-gesa tanpa analisa hanya karena melihat saham naik kencang disebut...",
                    options = listOf("Value Investing", "FOMO (Fear of Missing Out)", "Dollar Cost Averaging", "Rebalancing"),
                    correctIndex = 1,
                    explanation = "FOMO adalah rasa takut ketinggalan momen yang memicu keputusan impulsif membeli di harga pucuk."
                ),
                QuizQuestion(
                    id = 4,
                    question = "Apa fungsi utama membuat Trading Journal dalam aktivitas investasi mahasiswa?",
                    options = listOf("Untuk pamer di media sosial", "Mengevaluasi kesalahan, kepatuhan trading plan, dan melacak win rate", "Menghindari pembayaran pajak dividen", "Mempercepat persetujuan pinjaman bank"),
                    correctIndex = 1,
                    explanation = "Trading journal berguna untuk mendokumentasikan alasan entry/exit, mengukur kepatuhan aturan risiko, dan mengasah insting analisa."
                )
            )
        )
    )
}
