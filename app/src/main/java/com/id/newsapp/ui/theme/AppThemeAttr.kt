package com.id.newsapp.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable

object attr {
    val typography: Typography
        @Composable get() = MaterialTheme.typography

    val colors: ColorScheme
        @Composable get() = MaterialTheme.colorScheme

    val dimens: AppDimens
        @Composable get() = AppDimens
}
