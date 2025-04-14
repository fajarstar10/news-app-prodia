package com.id.newsapp.screen.homescreen

import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavHostController
import com.id.domain.model.News
import com.id.domain.model.NewsCategory
import com.id.newsapp.core.extensions.extractUsername
import com.id.newsapp.core.permission.rememberPermissionState
import com.id.newsapp.core.util.getGreetingTime
import com.id.newsapp.navigation.Route
import com.id.newsapp.screen.common.MainScreenWithInactivityHandler
import com.id.newsapp.screen.common.NotificationHelper
import com.id.newsapp.screen.homescreen.viewmodel.HomeViewModel
import com.id.newsapp.screen.homescreen.viewmodel.event.HomeEvent
import com.id.newsapp.screen.homescreen.viewmodel.state.HomeUiState
import com.id.newsapp.ui.theme.AppDimens
import com.id.newsapp.ui.theme.attr
import com.id.newsapp.utils.NewsImage
import org.koin.compose.koinInject

@Composable
fun HomeRoute(
    navController: NavHostController, viewModel: HomeViewModel = koinInject<HomeViewModel>()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    val permissionState = rememberPermissionState(
        permissions = buildList {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                add(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        },
    )

    LaunchedEffect(Unit) {
        viewModel.onEvent(HomeEvent.LoadEmail)
        NotificationHelper.createChannel(context)
        if (!permissionState.allGranted()) {
            permissionState.requestPermissions()
        } else {
            viewModel.onEvent(HomeEvent.LoadAll)
        }
    }

    MainScreenWithInactivityHandler(
        onTimeout = {
            viewModel.onEvent(HomeEvent.Logout)
            NotificationHelper.showNotification(
                context = context,
                title = "Waktu Habis",
                message = "Anda telah logout otomatis karena tidak aktif."
            )
            navController.navigate(Route.LOGIN) {
                popUpTo(Route.HOME) { inclusive = true }
            }
        },
    ) {
        if (!permissionState.allGranted()) {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Text("Beberapa fitur tidak tersedia karena izin belum diberikan.")
            }
        } else {
            HomeScreen(
                navController = navController,
                userName = state.email.extractUsername(),
                state = state,
                onRetry = { category -> viewModel.onEvent(HomeEvent.Retry(category)) },
            )
        }
    }
}

@Composable
fun HomeScreen(
    navController: NavHostController,
    userName: String = "",
    state: HomeUiState,
    onRetry: (NewsCategory) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(AppDimens.space16)
    ) {
        Text(
            text = "Selamat ${getGreetingTime()}, $userName 👋",
            style = attr.typography.headlineSmall,
            modifier = Modifier.padding(bottom = AppDimens.space16)
        )

        CategorySection(
            title = "Articles", items = state.articles,
            onItemClick = {
                navController.navigate(Route.newsDetail(it.id))
            },
            onSeeAllClick = {
                navController.navigate(Route.categoryGrid(NewsCategory.ARTICLES))
            },
        )
        Spacer(modifier = Modifier.height(AppDimens.space16))

        CategorySection(
            title = "Blogs", items = state.blogs,
            onItemClick = {
                navController.navigate(Route.newsDetail(it.id))
            },
            onSeeAllClick = {
                navController.navigate(Route.categoryGrid(NewsCategory.BLOGS))
            },
        )
        Spacer(modifier = Modifier.height(AppDimens.space16))

        CategorySection(
            title = "Reports", items = state.reports,
            onItemClick = {
                navController.navigate(Route.newsDetail(it.id))
            },
            onSeeAllClick = {
                navController.navigate(Route.categoryGrid(NewsCategory.REPORTS))
            },
        )

        if (state.error != null) {
            Spacer(modifier = Modifier.height(AppDimens.space16))
            Text(
                text = state.error,
                color = MaterialTheme.colorScheme.error,
                style = attr.typography.bodyMedium
            )
        }
    }
}

@Composable
fun CategorySection(
    title: String,
    items: List<News>,
    onItemClick: (News) -> Unit = {},
    onSeeAllClick: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxWidth()) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppDimens.space8),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title, style = attr.typography.titleLarge
            )

            TextButton(onClick = onSeeAllClick) {
                Text(
                    text = "Lihat Semua", style = attr.typography.labelLarge
                )
            }
        }

        if (items.isEmpty()) {
            Text(
                text = "Tidak ada data",
                style = attr.typography.bodyMedium,
                modifier = Modifier
                    .padding(AppDimens.space8)
                    .padding(start = AppDimens.space8)
            )
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(AppDimens.space12),
                contentPadding = PaddingValues(horizontal = AppDimens.space8)
            ) {
                items(items) { item ->
                    NewsItemCard(news = item,
                        modifier = Modifier
                            .width(AppDimens.space200)
                            .height(IntrinsicSize.Min),
                        onClick = { onItemClick(item) })
                }
            }
        }
    }
}

@Composable
fun NewsItemCard(
    news: News, modifier: Modifier = Modifier, onClick: () -> Unit = {}
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(AppDimens.cardCornerRadius12),
        elevation = CardDefaults.cardElevation(AppDimens.cardElevation4),
        modifier = modifier
            .width(AppDimens.space200)
            .height(AppDimens.cardHeight160)
    ) {
        Row(modifier = Modifier.padding(AppDimens.space12)) {

            NewsImage(
                imageUrl = news.imageUrl,
                contentDescription = news.title,
                modifier = Modifier
                    .size(AppDimens.space48)
                    .clip(RoundedCornerShape(AppDimens.space4))
            )

            Spacer(modifier = Modifier.width(AppDimens.space12))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = news.title,
                        style = attr.typography.titleMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = news.authors.firstOrNull()?.name ?: "Unknown Author",
                        style = attr.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = news.publishedAt.take(10),
                        style = attr.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }

                Spacer(modifier = Modifier.height(AppDimens.space4))

                Text(
                    text = news.summary,
                    style = attr.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
