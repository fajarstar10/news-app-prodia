package com.id.newsapp.screen.alldatabycategory

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.id.domain.model.News
import com.id.domain.model.NewsCategory
import com.id.newsapp.core.components.ScreenWithToolbar
import com.id.newsapp.navigation.Route
import com.id.newsapp.screen.homescreen.NewsItemCard
import com.id.newsapp.screen.homescreen.viewmodel.HomeViewModel
import com.id.newsapp.screen.homescreen.viewmodel.event.HomeEvent
import com.id.newsapp.ui.theme.AppDimens
import com.id.newsapp.ui.theme.attr
import org.koin.androidx.compose.koinViewModel

@Composable
fun NewsGridRoute(
    navController: NavController, category: NewsCategory
) {
    NewsGridScreen(
        navController = navController,
        category = category,
        onItemClick = { news ->
            navController.navigate(Route.newsDetail(news.id))
        },
    )
}

@Composable
fun NewsGridScreen(
    navController: NavController,
    category: NewsCategory,
    viewModel: HomeViewModel = koinViewModel(),
    onItemClick: (News) -> Unit = {}
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(category) {
        viewModel.onEvent(HomeEvent.LoadItemByCategory(category))
    }

    val baseList = when (category) {
        NewsCategory.ARTICLES -> state.articles
        NewsCategory.BLOGS -> state.blogs
        NewsCategory.REPORTS -> state.reports
    }

    val list = baseList.filter {
        state.searchQuery.isBlank() || it.title.contains(state.searchQuery, ignoreCase = true)
    }

    ScreenWithToolbar(
        showBackButton = true,
        onBackClick = { navController.popBackStack() },
        actions = {
            Row {
                IconButton(
                    onClick = {
                        viewModel.onEvent(HomeEvent.ToggleSortOrder)
                    },
                ) {
                    Icon(
                        imageVector = if (state.sortOrder.toggle() == SortOrder.DESCENDING) Icons.Default.ArrowDropDown else Icons.Default.KeyboardArrowUp,
                        contentDescription = "Toggle View"
                    )
                }
                IconButton(
                    onClick = {
                        viewModel.onEvent(HomeEvent.ToggleSearchMode)
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.Search, contentDescription = "Toggle View"
                    )
                }
            }
        },
        toolbarContent = {
            if (state.isSearchMode) {
                OutlinedTextField(
                    value = state.searchQuery, onValueChange = {
                        viewModel.onEvent(HomeEvent.UpdateSearchQuery(it))
                    }, keyboardActions = KeyboardActions(
                        onDone = {
                            viewModel.onEvent(HomeEvent.AddToRecentSearches(state.searchQuery))
                        },
                    ),
                    placeholder = { Text("Cari berita...") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Text(category.displayName, style = attr.typography.titleLarge)
            }
        },
        body = { padding ->
            if (state.isSearchMode && state.searchQuery.isBlank()) {
                LazyColumn(modifier = Modifier.padding(padding)) {
                    items(state.recentSearches) { query ->
                        ListItem(
                            headlineContent = { Text(query) },
                            trailingContent = {
                                IconButton(onClick = {
                                    viewModel.onEvent(HomeEvent.RemoveRecentSearch(query))
                                }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Hapus")
                                }
                            },
                            modifier = Modifier.clickable {
                                viewModel.onEvent(HomeEvent.UpdateSearchQuery(query))
                            },
                        )
                    }

                    if (state.recentSearches.isNotEmpty()) {
                        item {
                            TextButton(
                                onClick = {
                                    viewModel.onEvent(HomeEvent.ClearRecentSearches)
                                }, modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(AppDimens.space16)
                            ) {
                                Text("Hapus Semua Riwayat")
                            }
                        }
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize(),
                    contentPadding = PaddingValues(
                        horizontal = AppDimens.space16, vertical = AppDimens.space16
                    ),
                    verticalArrangement = Arrangement.spacedBy(AppDimens.space16),
                    horizontalArrangement = Arrangement.spacedBy(AppDimens.space16)
                ) {
                    items(list) { item ->
                        NewsItemCard(
                            news = item,
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { onItemClick(item) },
                        )
                    }
                }
            }
        },
    )
}