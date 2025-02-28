package com.sakshi.newsapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sakshi.newsapp.model.NewsArticle

import com.sakshi.newsapp.utils.NEWS_ARTICLES_TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLatestNewsArticle(articles: List<NewsArticle>)

    @Query("DELETE FROM $NEWS_ARTICLES_TABLE WHERE isSaved = 0")
    suspend fun clearAllLatestNewsArticles()

    @Query("SELECT * FROM $NEWS_ARTICLES_TABLE WHERE isSaved = 0")
    fun getAllLatestNews(): List<NewsArticle>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveNewsArticle(savedArticle: NewsArticle)
    @Query("SELECT * FROM $NEWS_ARTICLES_TABLE WHERE isSaved = 1")
    fun getSavedNewsArticles(): Flow<List<NewsArticle>>

    @Query("DELETE FROM $NEWS_ARTICLES_TABLE WHERE id = :articleId AND isSaved = 1")
    suspend fun deleteSavedArticle(articleId: Int)

}
