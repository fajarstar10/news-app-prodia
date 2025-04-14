package com.id.newsapp.screen.homescreen.detailitem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.navigation.NavHostController
import com.id.domain.model.News
import com.id.newsapp.core.components.ScreenWithToolbar
import com.id.newsapp.core.extensions.orDash
import com.id.newsapp.core.extensions.toDisplayDate
import com.id.newsapp.screen.homescreen.viewmodel.HomeViewModel
import com.id.newsapp.screen.homescreen.viewmodel.event.HomeEvent
import com.id.newsapp.ui.theme.AppDimens
import com.id.newsapp.ui.theme.AppTextSize
import com.id.newsapp.ui.theme.attr
import com.id.newsapp.utils.NewsImage
import org.koin.androidx.compose.koinViewModel

@Composable
fun NewsDetailRoute(
    navController: NavHostController,
    newsId: Int, viewModel: HomeViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onEvent(HomeEvent.GetNewsById(newsId))
    }
    if (newsId != 0) {
        ScreenWithToolbar(
            showBackButton = true,
            onBackClick = { navController.popBackStack() },
            toolbarContent = {
                Text(
                    "ID ${state.newsDetail.id}", style = attr.typography.titleSmall
                )
            },
            body = { padding ->
                Column(modifier = Modifier.padding(padding)) {
                    NewsDetailScreen(news = state.newsDetail)
                }
            },
        )
    } else {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            Text("Berita tidak ditemukan", style = attr.typography.bodyMedium)
        }
    }
}

@Composable
fun NewsDetailScreen(news: News) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(AppDimens.space16)
    ) {
        Text(
            text = news.title.orDash(),
            style = attr.typography.headlineSmall,
            modifier = Modifier.padding(bottom = AppDimens.space12)
        )

        NewsImage(
            imageUrl = news.imageUrl,
            contentDescription = news.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(AppDimens.space100 * 2)
                .clip(RoundedCornerShape(AppDimens.space8))
        )

        Spacer(modifier = Modifier.height(AppDimens.space16))

        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val author = news.authors.firstOrNull()?.name.orDash()
            Text(
                text = "By $author",
                style = attr.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = news.publishedAt.toDisplayDate(),
                style = attr.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
        }

        Spacer(modifier = Modifier.height(AppDimens.space12))

        Text(
            text = "Source: ${news.newsSite.orDash()}",
            style = attr.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(AppDimens.space16))

        Text(
            text = news.summary.orDash(),
            style = attr.typography.bodyMedium.copy(lineHeight = AppTextSize.size20)
        )

        Spacer(modifier = Modifier.height(AppDimens.space24))

        if (news.launches.isNotEmpty()) {
            Text(
                text = "Launches",
                style = attr.typography.titleMedium,
                modifier = Modifier.padding(bottom = AppDimens.space8)
            )
            news.launches.forEach {
                Text("🚀 ${it.provider}", style = attr.typography.bodySmall)
            }
            Spacer(modifier = Modifier.height(AppDimens.space12))
        }

        if (news.events.isNotEmpty()) {
            Text(
                text = "Events",
                style = attr.typography.titleMedium,
                modifier = Modifier.padding(bottom = AppDimens.space8)
            )
            news.events.forEach {
                Text("📍 ${it.provider}", style = attr.typography.bodySmall)
            }
        }
    }
}