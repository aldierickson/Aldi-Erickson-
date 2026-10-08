package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.Forum
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Newspaper
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.example.ui.screens.forum.ForumScreen
import com.example.ui.screens.home.HomeScreen
import com.example.ui.screens.leaderboard.LeaderboardScreen
import com.example.ui.screens.learn.LearnScreen
import com.example.ui.screens.news.NewsScreen
import com.example.ui.screens.trading.TradingScreen
import com.example.ui.screens.web.WebPortalScreen
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.NavigationTab
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val activeTab by viewModel.activeTab.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Listen to snackbar events
    LaunchedEffect(viewModel) {
        viewModel.snackbarEvent.collectLatest { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    // Handle back press if not on Home
    if (activeTab != NavigationTab.HOME) {
        BackHandler {
            viewModel.setActiveTab(NavigationTab.HOME)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .testTag("main_bottom_nav")
            ) {
                NavigationBarItem(
                    selected = activeTab == NavigationTab.HOME,
                    onClick = { viewModel.setActiveTab(NavigationTab.HOME) },
                    icon = {
                        Icon(
                            imageVector = if (activeTab == NavigationTab.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                            contentDescription = "Beranda"
                        )
                    },
                    label = { Text("Beranda") },
                    modifier = Modifier.testTag("nav_home")
                )

                NavigationBarItem(
                    selected = activeTab == NavigationTab.LEARN,
                    onClick = { viewModel.setActiveTab(NavigationTab.LEARN) },
                    icon = {
                        Icon(
                            imageVector = if (activeTab == NavigationTab.LEARN) Icons.Filled.AutoStories else Icons.Outlined.AutoStories,
                            contentDescription = "Modul"
                        )
                    },
                    label = { Text("Modul") },
                    modifier = Modifier.testTag("nav_learn")
                )

                NavigationBarItem(
                    selected = activeTab == NavigationTab.TRADE,
                    onClick = { viewModel.setActiveTab(NavigationTab.TRADE) },
                    icon = {
                        Icon(
                            imageVector = if (activeTab == NavigationTab.TRADE) Icons.AutoMirrored.Filled.TrendingUp else Icons.AutoMirrored.Outlined.TrendingUp,
                            contentDescription = "Simulasi"
                        )
                    },
                    label = { Text("Simulasi") },
                    modifier = Modifier.testTag("nav_trade")
                )

                NavigationBarItem(
                    selected = activeTab == NavigationTab.NEWS,
                    onClick = { viewModel.setActiveTab(NavigationTab.NEWS) },
                    icon = {
                        Icon(
                            imageVector = if (activeTab == NavigationTab.NEWS) Icons.Filled.Newspaper else Icons.Outlined.Newspaper,
                            contentDescription = "Berita"
                        )
                    },
                    label = { Text("Berita") },
                    modifier = Modifier.testTag("nav_news")
                )

                NavigationBarItem(
                    selected = activeTab == NavigationTab.FORUM || activeTab == NavigationTab.LEADERBOARD,
                    onClick = { viewModel.setActiveTab(NavigationTab.FORUM) },
                    icon = {
                        Icon(
                            imageVector = if (activeTab == NavigationTab.FORUM || activeTab == NavigationTab.LEADERBOARD) Icons.Filled.Forum else Icons.Outlined.Forum,
                            contentDescription = "Forum"
                        )
                    },
                    label = { Text("Forum") },
                    modifier = Modifier.testTag("nav_forum")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (activeTab) {
                NavigationTab.HOME -> HomeScreen(
                    viewModel = viewModel,
                    onNavigateTab = { tab -> viewModel.setActiveTab(tab) },
                    onSelectStock = { stock ->
                        viewModel.selectStock(stock)
                        viewModel.setActiveTab(NavigationTab.TRADE)
                    }
                )
                NavigationTab.LEARN -> LearnScreen(viewModel = viewModel)
                NavigationTab.TRADE -> TradingScreen(
                    viewModel = viewModel,
                    onBack = { viewModel.setActiveTab(NavigationTab.HOME) }
                )
                NavigationTab.NEWS -> NewsScreen(
                    viewModel = viewModel,
                    onBack = { viewModel.setActiveTab(NavigationTab.HOME) }
                )
                NavigationTab.FORUM -> ForumScreen(viewModel = viewModel)
                NavigationTab.LEADERBOARD -> LeaderboardScreen(viewModel = viewModel)
                NavigationTab.WEB -> WebPortalScreen(
                    onBack = { viewModel.setActiveTab(NavigationTab.HOME) }
                )
            }
        }
    }
}
