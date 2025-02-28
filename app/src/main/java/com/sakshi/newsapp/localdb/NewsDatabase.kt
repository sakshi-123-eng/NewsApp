package com.sakshi.newsapp.localdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sakshi.newsapp.model.NewsArticle

@Database(entities = [NewsArticle::class], version = 1, exportSchema = false)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun newsArticleDao(): NewsDao
}
