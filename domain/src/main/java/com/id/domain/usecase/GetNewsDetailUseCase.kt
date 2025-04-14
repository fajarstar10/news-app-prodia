package com.id.domain.usecase

import com.id.domain.model.News
import com.id.domain.repository.NewsRepository

class GetNewsDetailUseCase(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(id: Int): News {
        return repository.getArticleById(id)
    }
}