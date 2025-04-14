package com.id.data

import com.id.data.model.NewsDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NewsApiService {

    @GET("articles/")
    suspend fun getArticles(
        @Query("limit") limit: Int = 20, @Query("offset") offset: Int = 0
    ): BaseResponse<NewsDto>

    @GET("blogs/")
    suspend fun getBlogs(
        @Query("limit") limit: Int = 20, @Query("offset") offset: Int = 0
    ): BaseResponse<NewsDto>

    @GET("reports/")
    suspend fun getReports(
        @Query("limit") limit: Int = 20, @Query("offset") offset: Int = 0
    ): BaseResponse<NewsDto>

    @GET("articles/{id}/")
    suspend fun getArticleById(
        @Path("id") id: Int
    ): NewsDto
}
