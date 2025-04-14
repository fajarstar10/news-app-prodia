package com.id.domain.usecase

import com.id.domain.model.News
import com.id.domain.repository.NewsRepository

class GetArticlesUseCase(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(limit: Int, offset: Int): List<News> {
        return repository.getArticles(limit, offset)
    }
}