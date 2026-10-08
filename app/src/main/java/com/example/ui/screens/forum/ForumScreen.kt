package com.example.ui.screens.forum

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.data.local.entity.ForumCommentEntity
import com.example.data.local.entity.ForumPostEntity
import com.example.ui.theme.BearishRed
import com.example.ui.theme.BullishGreen
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.NavyLight
import com.example.ui.theme.NavyPrimary
import com.example.ui.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForumScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val posts by viewModel.forumPosts.collectAsState()
    val selectedPost by viewModel.selectedPost.collectAsState()
    val comments by viewModel.selectedPostComments.collectAsState()

    var selectedTag by remember { mutableStateOf("Semua") }
    var showCreateDialog by remember { mutableStateOf(false) }

    val tags = listOf("Semua", "#AnalisisSaham", "#AnalisisTeknikal", "#Rekomendasi", "#TanyaSenior", "#Pengumuman")

    val filteredPosts = if (selectedTag == "Semua") {
        posts
    } else {
        posts.filter { it.tag == selectedTag }
    }

    if (selectedPost != null) {
        BackHandler {
            viewModel.selectPost(null)
        }
        ForumDetailView(
            post = selectedPost!!,
            comments = comments,
            onBack = { viewModel.selectPost(null) },
            onLikeToggle = { viewModel.toggleLike(selectedPost!!.id) },
            onAddComment = { content -> viewModel.addComment(selectedPost!!.id, content) }
        )
    } else {
        Box(modifier = modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .testTag("forum_screen_list"),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))

                    Column {
                        Text(
                            text = "Forum Diskusi KSPM",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Ruang tukar pikiran, analisa saham, dan tanya jawab antaranggota pasar modal.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Tag Filter Chips
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(tags) { tag ->
                            FilterChip(
                                selected = selectedTag == tag,
                                onClick = { selectedTag = tag },
                                label = { Text(tag) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = NavyLight,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }

                items(filteredPosts) { post ->
                    ForumPostCard(
                        post = post,
                        onClick = { viewModel.selectPost(post) },
                        onLikeClick = { viewModel.toggleLike(post.id) }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(80.dp)) // Padding for FAB
                }
            }

            // FAB: Buat Diskusi Baru
            FloatingActionButton(
                onClick = { showCreateDialog = true },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(20.dp)
                    .testTag("fab_create_post"),
                containerColor = NavyPrimary,
                contentColor = Color.White
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Buat Diskusi")
            }
        }
    }

    if (showCreateDialog) {
        CreatePostDialog(
            onDismiss = { showCreateDialog = false },
            onPostSubmitted = { title, content, tag, ticker ->
                viewModel.createForumPost(title, content, tag, ticker)
                showCreateDialog = false
            }
        )
    }
}

@Composable
fun ForumPostCard(
    post: ForumPostEntity,
    onClick: () -> Unit,
    onLikeClick: () -> Unit
) {
    val timeFormat = SimpleDateFormat("dd MMM, HH:mm", Locale("id", "ID"))
    val dateString = timeFormat.format(Date(post.timestamp))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .testTag("forum_post_${post.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (post.isPinned) GoldAccent.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Author info and Pinned indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(NavyLight.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = post.authorName.take(1),
                            fontWeight = FontWeight.Bold,
                            color = NavyLight
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = post.authorName,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = NavyLight.copy(alpha = 0.12f)
                            ) {
                                Text(
                                    text = post.authorBadge,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 9.sp,
                                    color = NavyLight,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = "${post.university} • $dateString",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                if (post.isPinned) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = GoldAccent.copy(alpha = 0.2f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.PushPin,
                                contentDescription = "Pinned",
                                tint = GoldAccent,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "PIN",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = GoldAccent,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Post Title
            Text(
                text = post.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Content snippet
            Text(
                text = post.content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 3,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Footer: Tag chips + Like & Comment counts
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Text(
                            text = post.tag,
                            style = MaterialTheme.typography.labelSmall,
                            color = NavyLight,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    if (post.stockTicker != null) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = BullishGreen.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "$${post.stockTicker}",
                                style = MaterialTheme.typography.labelSmall,
                                color = BullishGreen,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable(onClick = onLikeClick)
                            .padding(4.dp)
                    ) {
                        Icon(
                            imageVector = if (post.isLikedByMe) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Suka",
                            tint = if (post.isLikedByMe) BearishRed else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${post.likesCount}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ChatBubbleOutline,
                            contentDescription = "Komentar",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${post.commentsCount}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ForumDetailView(
    post: ForumPostEntity,
    comments: List<ForumCommentEntity>,
    onBack: () -> Unit,
    onLikeToggle: () -> Unit,
    onAddComment: (String) -> Unit
) {
    var newCommentText by remember { mutableStateOf("") }
    val timeFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID"))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("forum_detail_view")
    ) {
        // App Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Kembali")
            }
            Text(
                text = "Diskusi KSPM",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Full Post Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(NavyLight.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = post.authorName.take(1),
                                    fontWeight = FontWeight.Bold,
                                    color = NavyLight
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = post.authorName,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = NavyLight.copy(alpha = 0.12f)
                                    ) {
                                        Text(
                                            text = post.authorBadge,
                                            style = MaterialTheme.typography.labelSmall,
                                            fontSize = 9.sp,
                                            color = NavyLight,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "${post.university} • ${timeFormat.format(Date(post.timestamp))}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = post.title,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = post.content,
                            style = MaterialTheme.typography.bodyLarge,
                            lineHeight = 24.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

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
                                    text = post.tag,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = NavyLight,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }

                            Button(
                                onClick = onLikeToggle,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (post.isLikedByMe) BearishRed.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant
                                )
                            ) {
                                Icon(
                                    imageVector = if (post.isLikedByMe) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = null,
                                    tint = if (post.isLikedByMe) BearishRed else MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "${post.likesCount} Suka",
                                    color = if (post.isLikedByMe) BearishRed else MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Komentar Anggota (${comments.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            if (comments.isEmpty()) {
                item {
                    Text(
                        text = "Belum ada komentar. Jadilah yang pertama memberikan pandangan!",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                items(comments) { comment ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = comment.authorName,
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "(${comment.university})",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Text(
                                    text = timeFormat.format(Date(comment.timestamp)),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 10.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = comment.content,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        // Comment Input Bottom Bar
        Surface(
            tonalElevation = 6.dp,
            shadowElevation = 8.dp,
            color = MaterialTheme.colorScheme.surface
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = newCommentText,
                    onValueChange = { newCommentText = it },
                    placeholder = { Text("Tulis pendapat atau analisa Anda...") },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("comment_input_field"),
                    shape = RoundedCornerShape(24.dp),
                    maxLines = 3
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        if (newCommentText.isNotBlank()) {
                            onAddComment(newCommentText.trim())
                            newCommentText = ""
                        }
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .background(NavyPrimary, CircleShape)
                        .testTag("send_comment_button"),
                    enabled = newCommentText.isNotBlank()
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Kirim",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CreatePostDialog(
    onDismiss: () -> Unit,
    onPostSubmitted: (title: String, content: String, tag: String, ticker: String?) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var tag by remember { mutableStateOf("#AnalisisSaham") }
    var ticker by remember { mutableStateOf("") }

    val tags = listOf("#AnalisisSaham", "#AnalisisTeknikal", "#Rekomendasi", "#TanyaSenior")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Buat Diskusi KSPM Baru", fontWeight = FontWeight.Bold)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Judul Diskusi") },
                    placeholder = { Text("cth: Potensi Rebound BBRI di Level Support") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = ticker,
                    onValueChange = { ticker = it.uppercase() },
                    label = { Text("Kode Saham (Opsional)") },
                    placeholder = { Text("cth: BBCA") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Text(
                    text = "Kategori Tag:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold
                )

                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(tags) { t ->
                        FilterChip(
                            selected = tag == t,
                            onClick = { tag = t },
                            label = { Text(t, fontSize = 11.sp) }
                        )
                    }
                }

                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Isi Pembahasan") },
                    placeholder = { Text("Kemukakan alasan analisa, rasio, atau pertanyaan Anda kepada sesama mahasiswa...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    shape = RoundedCornerShape(10.dp),
                    maxLines = 5
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onPostSubmitted(title.trim(), content.trim(), tag, ticker.trim().takeIf { it.isNotBlank() })
                },
                enabled = title.isNotBlank() && content.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary)
            ) {
                Text("Terbitkan")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}
