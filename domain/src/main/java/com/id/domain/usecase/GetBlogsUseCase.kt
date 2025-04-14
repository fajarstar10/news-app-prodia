package com.id.domain.usecase

import com.id.domain.model.News
import com.id.domain.repository.NewsRepository

class GetBlogsUseCase(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(limit: Int, offset: Int): List<News> {
        return repository.getBlogs(limit, offset)
    }
}