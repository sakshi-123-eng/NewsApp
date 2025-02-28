package com.sakshi.newsapp.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sakshi.newsapp.utils.NEWS_ARTICLES_TABLE
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = NEWS_ARTICLES_TABLE)
data class NewsArticle(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val description: String?,
    val title: String?,
    val url: String?,
    val urlToImage: String?,
    val isSaved: Boolean = false
) : Parcelable

sealed class NewsState {
    data object Loading : NewsState()
    data class Success(val articles: List<NewsArticle>) : NewsState()
    data class Error(val message: String) : NewsState()
}
data class News(
    val articles: List<NewsArticle>
)

