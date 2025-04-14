package com.id.newsapp.screen.homescreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.id.domain.model.NewsCategory
import com.id.domain.repository.UserRepository
import com.id.domain.usecase.GetArticlesUseCase
import com.id.domain.usecase.GetBlogsUseCase
import com.id.domain.usecase.GetNewsDetailUseCase
import com.id.domain.usecase.GetReportsUseCase
import com.id.newsapp.screen.alldatabycategory.SortOrder
import com.id.newsapp.screen.homescreen.viewmodel.event.HomeEvent
import com.id.newsapp.screen.homescreen.viewmodel.state.HomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getArticlesUseCase: GetArticlesUseCase,
    private val getBlogsUseCase: GetBlogsUseCase,
    private val getReportsUseCase: GetReportsUseCase,
    private val getNewsDetailUseCase: GetNewsDetailUseCase,
    private val repository: UserRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.LoadAll -> {
                loadCategory(NewsCategory.ARTICLES)
                loadCategory(NewsCategory.BLOGS)
                loadCategory(NewsCategory.REPORTS)
            }

            is HomeEvent.LoadItemByCategory -> {
                _state.update { it.copy(lastViewedCategory = event.category) }
                loadCategory(event.category)
            }

            is HomeEvent.Retry -> {
                loadCategory(event.category)
            }

            is HomeEvent.GetNewsById -> {
                loadNewsDetail(event.id)
            }

            is HomeEvent.ToggleSortOrder -> {
                val currentSort = _state.value.sortOrder
                val newSort = currentSort.toggle()

                _state.update { it.copy(sortOrder = newSort) }

                val category = _state.value.lastViewedCategory
                if (category != null) {
                    loadCategory(category)
                }
            }

            is HomeEvent.ToggleSearchMode -> {
                _state.update {
                    it.copy(
                        isSearchMode = !it.isSearchMode,
                        searchQuery = if (it.isSearchMode) "" else it.searchQuery
                    )
                }
            }

            is HomeEvent.UpdateSearchQuery -> {
                _state.update { it.copy(searchQuery = event.query) }
            }

            is HomeEvent.AddToRecentSearches -> {
                val current = _state.value.recentSearches.toMutableList()
                if (event.query.isNotBlank() && !current.contains(event.query)) {
                    current.add(0, event.query)
                    if (current.size > 10) current.removeLast()
                }
                _state.update { it.copy(recentSearches = current) }
            }

            is HomeEvent.RemoveRecentSearch -> {
                val filtered = _state.value.recentSearches.filterNot { it == event.query }
                _state.update { it.copy(recentSearches = filtered) }
            }

            is HomeEvent.ClearRecentSearches -> {
                _state.update { it.copy(recentSearches = emptyList()) }
            }

            is HomeEvent.LoadEmail -> loadEmail()

            is HomeEvent.Logout -> {
                viewModelScope.launch {
                    repository.setLoggedIn(false)
                    _state.update { it.copy(isLoggedOut = true) }
                }
            }
        }
    }

    private fun loadEmail() {
        viewModelScope.launch {
            repository.getEmail().collect { email ->
                _state.update { it.copy(email = email) }
            }
        }
    }

    private fun loadCategory(category: NewsCategory, applySort: Boolean = false) {
        viewModelScope.launch {
            try {
                val result = when (category) {
                    NewsCategory.ARTICLES -> getArticlesUseCase(10, 0)
                    NewsCategory.BLOGS -> getBlogsUseCase(10, 0)
                    NewsCategory.REPORTS -> getReportsUseCase(10, 0)
                }

                val sorted = when (state.value.sortOrder) {
                    SortOrder.ASCENDING -> result.sortedBy { it.title }
                    SortOrder.DESCENDING -> result.sortedByDescending { it.title }
                }

                _state.update {
                    when (category) {
                        NewsCategory.ARTICLES -> it.copy(articles = sorted)
                        NewsCategory.BLOGS -> it.copy(blogs = sorted)
                        NewsCategory.REPORTS -> it.copy(reports = sorted)
                    }
                }

            } catch (e: Exception) {
                _state.update {
                    it.copy(error = "Gagal memuat ${category.displayName.lowercase()}: ${e.message}")
                }
            }
        }
    }

    private fun loadNewsDetail(id: Int) {
        viewModelScope.launch {
            try {
                val result = getNewsDetailUseCase(id)
                _state.update { it.copy(newsDetail = result) }
            } catch (e: Exception) {
                _state.update { it.copy(error = "Gagal memuat detail berita: ${e.message}") }
            }
        }
    }

}