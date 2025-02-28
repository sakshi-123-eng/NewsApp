package com.sakshi.newsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.sakshi.newsapp.composables.BottomNavItem
import com.sakshi.newsapp.composables.BottomNavigationBar
import com.sakshi.newsapp.composables.WebPage
import com.sakshi.newsapp.model.NewsArticle
import com.sakshi.newsapp.ui.theme.NewsAppTheme
import com.sakshi.newsapp.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val enableDarkTheme = remember {
                mutableStateOf(false)
            }
            val viewModel: NewsViewModel = hiltViewModel()
            NewsAppTheme(darkTheme = enableDarkTheme.value) {
                val newsArticle = remember {
                    mutableStateOf<NewsArticle?>(null)
                }
                val bottomNavStartDestination = remember {
                    mutableStateOf<BottomNavItem>(BottomNavItem.Home)
                }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    newsArticle.value?.let { article ->
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            if (article.url.isNullOrEmpty()) {
                                Text(text = getString(R.string.article_not_found))
                            } else {
                                WebPage(article = article, viewModel = viewModel) {
                                    newsArticle.value = null
                                    if (it) {
                                        bottomNavStartDestination.value = BottomNavItem.Saved
                                    } else {
                                        bottomNavStartDestination.value = BottomNavItem.Home
                                    }
                                }
                            }
                        }
                    } ?: run {
                        BottomNavigationBar(
                            bottomNavStartDestination.value,
                            viewModel = viewModel,
                            enableDarkTheme = enableDarkTheme.value,
                            onReadMoreClicked = {
                                newsArticle.value = it
                            },
                            onThemeChanged = {
                                enableDarkTheme.value = it
                            }
                        )
                    }

                }
            }
        }
    }
}