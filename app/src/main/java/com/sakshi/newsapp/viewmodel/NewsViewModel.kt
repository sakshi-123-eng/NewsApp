package com.sakshi.newsapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sakshi.newsapp.model.NewsArticle
import com.sakshi.newsapp.model.NewsState
import com.sakshi.newsapp.repo.NewsRepository
import com.sakshi.newsapp.utils.API_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {

    private val _newsState = MutableStateFlow<NewsState>(NewsState.Loading)
    val newsState: StateFlow<NewsState> = _newsState.asStateFlow()

    val newsSavedState: StateFlow<List<NewsArticle>> = repository.getSavedArticles()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList()
        )

    init {
        fetchNews()
    }

    private fun fetchNews() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getLatestNews(apiKey = API_KEY)
                .onStart { _newsState.value = NewsState.Loading }
                .catch { e ->
                    _newsState.value = NewsState.Error(e.message ?: "Unknown error")
                }
                .collect { response ->
                    if (response.isSuccessful && response.body() != null) {
                        response.body()?.articles?.let {
                            _newsState.value = NewsState.Success(it)
                        }
                    } else {
                        _newsState.value = NewsState.Error("Failed to load news")
                    }
                }
        }
    }

    fun saveArticle(article: NewsArticle) {
        viewModelScope.launch {
            repository.saveArticle(article)
        }
    }

    fun deleteArticle(article: NewsArticle) {
        viewModelScope.launch {
            repository.deleteArticle(article)
        }
    }
}
