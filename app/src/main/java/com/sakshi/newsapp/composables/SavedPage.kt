package com.sakshi.newsapp.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sakshi.newsapp.model.NewsArticle
import com.sakshi.newsapp.viewmodel.NewsViewModel


@Composable
fun SavedScreen(
    viewModel: NewsViewModel = hiltViewModel(),
    onReadMoreClicked: (NewsArticle) -> Unit,
) {
    val savedNewsList by viewModel.newsSavedState.collectAsState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (savedNewsList.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "No Saved Items Found")
            }
        } else {
            LazyColumn {
                items(savedNewsList) { article ->
                    NewsItem(article = article, onReadMoreClicked = onReadMoreClicked) {
                        viewModel.deleteArticle(article)
                    }
                }
            }
        }
    }

}