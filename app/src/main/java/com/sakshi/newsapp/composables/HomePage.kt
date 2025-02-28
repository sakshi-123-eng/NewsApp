package com.sakshi.newsapp.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sakshi.newsapp.R
import com.sakshi.newsapp.model.NewsArticle
import com.sakshi.newsapp.model.NewsState
import com.sakshi.newsapp.viewmodel.NewsViewModel

@Composable
fun HomeScreen(
    viewModel: NewsViewModel = hiltViewModel(),
    onReadMoreClicked: (NewsArticle) -> Unit
) {
    val newsState by viewModel.newsState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        when (newsState) {
            is NewsState.Loading -> {
                CircularProgressIndicator()
            }

            is NewsState.Success -> {
                val articles = (newsState as NewsState.Success).articles
                if (articles.isNotEmpty()) {
                    LazyColumn {
                        items(articles) { item ->
                            NewsItem(article = item, onReadMoreClicked = onReadMoreClicked)
                        }
                    }
                } else {
                    Text(text = stringResource(R.string.no_news), color = Color.Gray)
                }
            }

            is NewsState.Error -> {
                val errorMessage = (newsState as NewsState.Error).message
                Text(text = "Error: $errorMessage", color = Color.Red)
            }
        }
    }
}
