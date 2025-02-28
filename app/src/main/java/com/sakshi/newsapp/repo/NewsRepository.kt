package com.sakshi.newsapp.repo

import com.sakshi.newsapp.localdb.NewsDao
import com.sakshi.newsapp.model.NewsArticle
import com.sakshi.newsapp.model.News
import com.sakshi.newsapp.network.NewsApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor(
    private val apiService: NewsApiService,
    private val newsArticleDao: NewsDao
) {

    fun getLatestNews(apiKey: String): Flow<Response<News>> = flow {
        emit(apiService.getLatestNews(apiKey = apiKey))
    }.flowOn(Dispatchers.IO)

    suspend fun saveArticle(article: NewsArticle) {
        newsArticleDao.insertArticle(article)
    }

    suspend fun deleteArticle(article: NewsArticle) {
        newsArticleDao.deleteArticle(article)
    }

    fun getSavedArticles(): Flow<List<NewsArticle>> {
        return newsArticleDao.getSavedArticles()
    }

}

