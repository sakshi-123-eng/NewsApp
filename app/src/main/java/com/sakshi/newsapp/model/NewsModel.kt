package com.sakshi.newsapp.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sakshi.newsapp.utils.TABLE_NAME
import kotlinx.parcelize.Parcelize

data class News(
    val articles: List<NewsArticle>
)

@Parcelize
@Entity(tableName = TABLE_NAME)
data class NewsArticle(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val description: String?,
    val title: String?,
    val url: String?,
    val urlToImage: String?,
    var isSaved: Boolean = false
): Parcelable

sealed class NewsState {
    data object Loading : NewsState()
    data class Success(val articles: List<NewsArticle>) : NewsState()
    data class Error(val message: String) : NewsState()
}

