package com.id.data.repository

import com.id.data.NewsApiService
import com.id.data.mapper.toDomain
import com.id.domain.model.News
import com.id.domain.repository.NewsRepository

class NewsRepositoryImpl(
    private val api: NewsApiService
) : NewsRepository {

    override suspend fun getArticles(limit: Int, offset: Int): List<News> {
        return api.getArticles(limit, offset).results.orEmpty().map { it.toDomain() }
    }

    override suspend fun getBlogs(limit: Int, offset: Int): List<News> {
        return api.getBlogs(limit, offset).results.orEmpty().map { it.toDomain() }
    }

    override suspend fun getReports(limit: Int, offset: Int): List<News> {
        return api.getReports(limit, offset).results.orEmpty().map { it.toDomain() }
    }

    override suspend fun getArticleById(id: Int): News {
        return api.getArticleById(id).toDomain()
    }
}
