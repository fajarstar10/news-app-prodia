package com.id.domain.repository

import com.id.domain.model.News

interface NewsRepository {
    suspend fun getArticles(limit: Int, offset: Int): List<News>
    suspend fun getBlogs(limit: Int, offset: Int): List<News>
    suspend fun getReports(limit: Int, offset: Int): List<News>
    suspend fun getArticleById(id: Int): News
}