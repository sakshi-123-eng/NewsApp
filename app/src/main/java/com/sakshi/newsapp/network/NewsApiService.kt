package com.sakshi.newsapp.network

import com.sakshi.newsapp.model.News
import com.sakshi.newsapp.utils.API_KEY
import com.sakshi.newsapp.utils.API_KEY_QUERY
import com.sakshi.newsapp.utils.CATEGORY_QUERY
import com.sakshi.newsapp.utils.COUNTRY_QUERY
import com.sakshi.newsapp.utils.TOP_HEADLINES
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET(TOP_HEADLINES)
    suspend fun getLatestNews(
        @Query(COUNTRY_QUERY) country: String = "us",
        @Query(CATEGORY_QUERY) category: String = "business",
        @Query(API_KEY_QUERY) apiKey: String = API_KEY
    ): Response<News>
}