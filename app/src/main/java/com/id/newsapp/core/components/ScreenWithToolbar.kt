package com.id.newsapp.core.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ScreenWithToolbar(
    modifier: Modifier = Modifier,
    showBackButton: Boolean = false,
    onBackClick: () -> Unit = {},
    toolbarContent: @Composable () -> Unit,
    actions: @Composable androidx.compose.foundation.layout.RowScope.() -> Unit = {},
    body: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            AppToolbar(
                showBackButton = showBackButton,
                onBackClick = onBackClick,
                titleContent = toolbarContent,
                actions = actions
            )
        }, modifier = modifier, content = body
    )
}