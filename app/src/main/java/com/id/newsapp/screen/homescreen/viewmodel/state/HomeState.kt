package com.id.newsapp.screen.homescreen.viewmodel.state

import com.id.domain.model.News
import com.id.domain.model.NewsCategory
import com.id.newsapp.screen.alldatabycategory.SortOrder

data class HomeUiState(
    val isLoading: Boolean = false,
    val articles: List<News> = emptyList(),
    val blogs: List<News> = emptyList(),
    val reports: List<News> = emptyList(),
    val error: String? = null,
    val newsDetail: News = News(),
    val newsId: Int? = null,
    val sortOrder: SortOrder = SortOrder.DESCENDING,
    val lastViewedCategory: NewsCategory? = null,
    val isSearchMode: Boolean = false,
    val searchQuery: String = "",
    val email: String = "",
    val isLoggedOut: Boolean = false,
    val recentSearches: List<String> = emptyList(),
)