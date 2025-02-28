package com.sakshi.newsapp.composables

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.View
import android.webkit.*
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.sakshi.newsapp.R
import com.sakshi.newsapp.model.NewsArticle
import com.sakshi.newsapp.viewmodel.NewsViewModel

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebPage(
    viewModel: NewsViewModel = hiltViewModel(),
    article: NewsArticle,
    moveBack: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val isLoading = remember { mutableStateOf(true) }

    BackHandler {
        moveBack(article.isSaved)
    }

    val webView = remember {
        WebView(context).apply {
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                allowFileAccess = true
                allowContentAccess = true
                loadsImagesAutomatically = true
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                useWideViewPort = true
                loadWithOverviewMode = true
            }

            setLayerType(View.LAYER_TYPE_HARDWARE, null)
            webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    isLoading.value = true
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    isLoading.value = false
                }
            }

            webChromeClient = WebChromeClient()
        }
    }
    LaunchedEffect(article.url) {
        article.url?.let { webView.loadUrl(it) }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { webView },
            modifier = Modifier
                .fillMaxSize()
                .alpha(if (isLoading.value) 0f else 1f)
        )
        if (isLoading.value) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
        if (!isLoading.value && article.isSaved.not()) {
            FloatingActionButton(
                onClick = {
                    moveBack(true)
                    viewModel.saveArticle(article.copy(isSaved = true, id = 0))
                },
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomEnd),
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Text(text = stringResource(R.string.save))
            }
        }
    }
}
