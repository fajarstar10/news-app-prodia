package com.id.newsapp.screen.homescreen.viewmodel.event

import com.id.domain.model.NewsCategory

sealed interface HomeEvent {
    data object LoadAll : HomeEvent
    data object LoadEmail : HomeEvent
    data object Logout : HomeEvent
    data class LoadItemByCategory(val category: NewsCategory) : HomeEvent
    data class Retry(val category: NewsCategory) : HomeEvent
    data class GetNewsById(val id: Int) : HomeEvent
    data object ToggleSortOrder : HomeEvent
    data object ToggleSearchMode : HomeEvent
    data class UpdateSearchQuery(val query: String) : HomeEvent
    data class AddToRecentSearches(val query: String) : HomeEvent
    data class RemoveRecentSearch(val query: String) : HomeEvent
    data object ClearRecentSearches : HomeEvent
}