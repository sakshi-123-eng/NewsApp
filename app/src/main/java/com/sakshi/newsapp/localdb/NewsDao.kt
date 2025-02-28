package com.sakshi.newsapp.localdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sakshi.newsapp.model.NewsArticle

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: NewsArticle)

    @Query("SELECT * FROM news_articles")
    fun getSavedArticles(): Flow<List<NewsArticle>>

    @Delete
    suspend fun deleteArticle(article: NewsArticle)
}
