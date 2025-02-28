package com.sakshi.newsapp.repo

import com.sakshi.newsapp.localdb.NewsDao
import com.sakshi.newsapp.model.NewsArticle
import com.sakshi.newsapp.network.NewsApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor(
    private val apiService: NewsApiService,
    private val newsDao: NewsDao
) {
    fun getLatestNews(apiKey: String): Flow<List<NewsArticle>> = flow {
        try {
            val response = apiService.getLatestNews(apiKey = apiKey)
            if (response.isSuccessful) {
                response.body()?.let { newsResponse ->
                    emit(newsResponse.articles)
                    newsDao.clearAllLatestNewsArticles()
                    newsDao.insertLatestNewsArticle(newsResponse.articles)
                }
            }
        } catch (ioException: IOException){
            emit(newsDao.getAllNews())
        }
    }

    suspend fun saveNewsArticle(article: NewsArticle) {
        newsDao.saveNewsArticle(article)
    }

    suspend fun deleteSavedArticle(article: NewsArticle) {
        newsDao.deleteSavedArticle(article.id)
    }

    fun getSavedArticles(): Flow<List<NewsArticle>> {
        return newsDao.getSavedNewsArticles()
    }
}

