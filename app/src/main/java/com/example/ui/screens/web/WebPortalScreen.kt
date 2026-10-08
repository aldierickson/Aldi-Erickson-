package com.example.ui.screens.web

import android.annotation.SuppressLint
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ui.theme.NavyLight

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebPortalScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var webViewInstance: WebView? = remember { null }

    BackHandler {
        if (webViewInstance?.canGoBack() == true) {
            webViewInstance?.goBack()
        } else {
            onBack()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("web_portal_screen")
    ) {
        // Portal Top Header Bar
        Surface(
            tonalElevation = 3.dp,
            color = MaterialTheme.colorScheme.surface
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Kembali ke Native")
                }
                Spacer(modifier = Modifier.width(4.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Website KSPM STAIMI (HTML/Web)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Portal Web Interaktif • Tailwind & JS",
                        style = MaterialTheme.typography.labelSmall,
                        color = NavyLight
                    )
                }
                IconButton(onClick = { webViewInstance?.reload() }) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = "Muat Ulang Halaman Web")
                }
            }
        }

        // Native Android WebView loading local HTML
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    settings.loadWithOverviewMode = true
                    settings.useWideViewPort = true
                    settings.builtInZoomControls = false
                    settings.displayZoomControls = false

                    webViewClient = WebViewClient()
                    webChromeClient = WebChromeClient()

                    loadUrl("file:///android_asset/web/index.html")
                    webViewInstance = this
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        )
    }
}
