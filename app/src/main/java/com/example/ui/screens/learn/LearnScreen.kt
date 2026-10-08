package com.example.ui.screens.learn

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LearningModule
import com.example.data.model.QuizQuestion
import com.example.ui.theme.BearishRed
import com.example.ui.theme.BullishGreen
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.NavyLight
import com.example.ui.theme.NavyPrimary
import com.example.ui.viewmodel.MainViewModel

@Composable
fun LearnScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val modules = viewModel.modules
    val progressList by viewModel.moduleProgress.collectAsState()

    var activeModule by remember { mutableStateOf<LearningModule?>(null) }
    var inQuizMode by remember { mutableStateOf(false) }

    if (inQuizMode && activeModule != null) {
        BackHandler {
            inQuizMode = false
        }
        QuizFlowView(
            module = activeModule!!,
            onFinishQuiz = { score, maxScore ->
                viewModel.submitQuizScore(activeModule!!.id, score, maxScore)
                inQuizMode = false
            },
            onCancelQuiz = { inQuizMode = false }
        )
    } else if (activeModule != null) {
        BackHandler {
            activeModule = null
        }
        ModuleReaderView(
            module = activeModule!!,
            onBack = { activeModule = null },
            onStartQuiz = { inQuizMode = true }
        )
    } else {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .testTag("learn_screen_list"),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))

                Column {
                    Text(
                        text = "Modul Edukasi Pasar Modal",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Kurikulum resmi KSPM untuk membekali mahasiswa menjadi investor cerdas & rasional.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Progress Banner
                val completedCount = progressList.count { it.completed }
                val totalModules = modules.size
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = NavyLight.copy(alpha = 0.12f)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Progres Belajar: $completedCount dari $totalModules Modul Selesai",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = NavyLight
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            LinearProgressIndicator(
                                progress = { if (totalModules > 0) completedCount.toFloat() / totalModules else 0f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = BullishGreen,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(GoldAccent.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.EmojiEvents,
                                contentDescription = null,
                                tint = GoldAccent,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }

            items(modules) { module ->
                val progress = progressList.find { it.moduleId == module.id }
                val isCompleted = progress?.completed == true

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { activeModule = module }
                        .testTag("module_item_${module.id}"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = NavyLight.copy(alpha = 0.12f)
                            ) {
                                Text(
                                    text = module.category,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = NavyLight,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }

                            if (isCompleted) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = BullishGreen.copy(alpha = 0.15f)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = BullishGreen,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "Skor: ${progress.quizScore}/${progress.maxScore}",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = BullishGreen
                                        )
                                    }
                                }
                            } else {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.AccessTime,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${module.readTimeMinutes} menit",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = module.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = module.summary,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Tingkat: ${module.level} • 4 Soal Kuis",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickable { activeModule = module }
                            ) {
                                Text(
                                    text = if (isCompleted) "Pelajari Ulang" else "Mulai Belajar",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = NavyLight
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.ArrowForward,
                                    contentDescription = null,
                                    tint = NavyLight,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun ModuleReaderView(
    module: LearningModule,
    onBack: () -> Unit,
    onStartQuiz: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("module_reader_view"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Kembali")
                }
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Text(
                        text = module.category,
                        style = MaterialTheme.typography.labelSmall,
                        color = NavyLight,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = module.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Summary Card
        item {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = module.summary,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(14.dp),
                    lineHeight = 20.sp
                )
            }
        }

        // Sections
        items(module.sections) { section ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = section.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = NavyPrimary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = section.content,
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 22.sp
                    )

                    if (section.keyPoints.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Poin Kunci:",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                        section.keyPoints.forEach { point ->
                            Row(
                                modifier = Modifier.padding(vertical = 2.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(text = "✓ ", color = BullishGreen, fontWeight = FontWeight.Bold)
                                Text(
                                    text = point,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }

                    if (section.tip != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = GoldAccent.copy(alpha = 0.15f)
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lightbulb,
                                    contentDescription = null,
                                    tint = GoldAccent,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = section.tip,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Bottom CTA: Start Interactive Quiz
        item {
            Button(
                onClick = onStartQuiz,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("start_quiz_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary)
            ) {
                Icon(imageVector = Icons.Default.Quiz, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Uji Pemahaman: Mulai Kuis Interaktif",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun QuizFlowView(
    module: LearningModule,
    onFinishQuiz: (score: Int, maxScore: Int) -> Unit,
    onCancelQuiz: () -> Unit
) {
    val questions = module.quiz
    var currentIndex by remember { mutableIntStateOf(0) }
    var selectedOptionIndex by remember { mutableStateOf<Int?>(null) }
    var isAnswerSubmitted by remember { mutableStateOf(false) }
    var correctAnswersCount by remember { mutableIntStateOf(0) }
    var isQuizCompleted by remember { mutableStateOf(false) }

    val currentQ = questions.getOrNull(currentIndex)

    if (isQuizCompleted) {
        val maxScore = questions.size * 25 // 100 max
        val finalScore = correctAnswersCount * 25

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .testTag("quiz_completed_view"),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(GoldAccent.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🎖️", fontSize = 42.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Kuis Selesai!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = module.title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "$finalScore / $maxScore",
                        style = MaterialTheme.typography.displayLarge,
                        fontWeight = FontWeight.Bold,
                        color = if (finalScore >= 75) BullishGreen else NavyLight
                    )
                    Text(
                        text = "Benar $correctAnswersCount dari ${questions.size} Pertanyaan",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { onFinishQuiz(finalScore, maxScore) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("finish_and_save_quiz"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BullishGreen)
            ) {
                Text(
                    text = "Simpan Hasil & Selesai",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    } else if (currentQ != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .testTag("quiz_question_view")
        ) {
            // Header: Progress
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onCancelQuiz) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Batal Kuis")
                }
                Text(
                    text = "Soal ${currentIndex + 1} dari ${questions.size}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${(currentIndex + 1) * 100 / questions.size}%",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            LinearProgressIndicator(
                progress = { (currentIndex + 1).toFloat() / questions.size },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = NavyLight
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Question Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Text(
                    text = currentQ.question,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(18.dp),
                    lineHeight = 24.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Options
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                currentQ.options.forEachIndexed { optIndex, optionText ->
                    val isSelected = selectedOptionIndex == optIndex
                    val isCorrect = optIndex == currentQ.correctIndex

                    val borderColor = when {
                        !isAnswerSubmitted && isSelected -> NavyLight
                        isAnswerSubmitted && isCorrect -> BullishGreen
                        isAnswerSubmitted && isSelected && !isCorrect -> BearishRed
                        else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                    }

                    val containerColor = when {
                        !isAnswerSubmitted && isSelected -> NavyLight.copy(alpha = 0.1f)
                        isAnswerSubmitted && isCorrect -> BullishGreen.copy(alpha = 0.15f)
                        isAnswerSubmitted && isSelected && !isCorrect -> BearishRed.copy(alpha = 0.15f)
                        else -> MaterialTheme.colorScheme.surface
                    }

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable(enabled = !isAnswerSubmitted) {
                                selectedOptionIndex = optIndex
                            },
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, borderColor),
                        color = containerColor
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val optionLetter = ('A' + optIndex).toString()
                            Box(
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(CircleShape)
                                    .background(borderColor.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = optionLetter,
                                    fontWeight = FontWeight.Bold,
                                    color = borderColor
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = optionText,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // Explanation Card shown after submitting answer
            AnimatedVisibility(visible = isAnswerSubmitted) {
                Column {
                    Spacer(modifier = Modifier.height(14.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = if (selectedOptionIndex == currentQ.correctIndex) "✓ Jawaban Tepat!" else "✗ Penjelasan:",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (selectedOptionIndex == currentQ.correctIndex) BullishGreen else BearishRed
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = currentQ.explanation,
                                style = MaterialTheme.typography.bodySmall,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Action Button
            if (!isAnswerSubmitted) {
                Button(
                    onClick = {
                        isAnswerSubmitted = true
                        if (selectedOptionIndex == currentQ.correctIndex) {
                            correctAnswersCount++
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("submit_answer_button"),
                    enabled = selectedOptionIndex != null,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary)
                ) {
                    Text("Periksa Jawaban", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            } else {
                Button(
                    onClick = {
                        if (currentIndex + 1 < questions.size) {
                            currentIndex++
                            selectedOptionIndex = null
                            isAnswerSubmitted = false
                        } else {
                            isQuizCompleted = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("next_question_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BullishGreen)
                ) {
                    Text(
                        text = if (currentIndex + 1 < questions.size) "Soal Berikutnya" else "Lihat Skor Akhir",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
