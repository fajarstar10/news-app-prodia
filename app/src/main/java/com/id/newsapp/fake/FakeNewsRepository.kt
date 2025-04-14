package com.id.newsapp.fake

import com.id.domain.model.News
import com.id.domain.repository.NewsRepository

class FakeNewsRepository : NewsRepository {
    override suspend fun getArticles(limit: Int, offset: Int): List<News> {
        return listOf(
            News(id = 1, title = "Zebra"),
            News(id = 2, title = "Apple"),
            News(id = 3, title = "Monkey")
        )
    }

    override suspend fun getBlogs(limit: Int, offset: Int): List<News> {
        return listOf(
            News(id = 4, title = "Delta"), News(id = 5, title = "Alpha")
        )
    }

    override suspend fun getReports(limit: Int, offset: Int): List<News> {
        return listOf(
            News(id = 6, title = "Orange"), News(id = 7, title = "Banana")
        )
    }

    override suspend fun getArticleById(id: Int): News {
        return News(id = id, title = "Detail $id")
    }
}