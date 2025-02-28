package com.sakshi.newsapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sakshi.newsapp.model.NewsArticle
import com.sakshi.newsapp.model.NewsState
import com.sakshi.newsapp.repo.NewsRepository
import com.sakshi.newsapp.utils.API_KEY
import com.sakshi.newsapp.utils.SOME_THING_WENT_WRONG
import com.sakshi.newsapp.utils.VM_TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
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
        _newsState.value = NewsState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            repository.getLatestNews(apiKey = API_KEY)
                .catch { e ->
                    _newsState.value = NewsState.Error(SOME_THING_WENT_WRONG)
                    e.message?.let { Log.d(VM_TAG, it) }
                }
                .collect { response ->
                    _newsState.value = NewsState.Success(response)
                }
        }
    }

    fun saveArticle(article: NewsArticle) {
        viewModelScope.launch {
            try {
                repository.saveNewsArticle(article)
            } catch (e: Throwable) {
                e.message?.let { Log.d(VM_TAG, it) }
            }
        }
    }

    fun deleteArticle(article: NewsArticle) {
        viewModelScope.launch {
            try {
                repository.deleteSavedArticle(article)
            } catch (e: Throwable) {
                e.message?.let { Log.d(VM_TAG, it) }
            }
        }
    }
}
