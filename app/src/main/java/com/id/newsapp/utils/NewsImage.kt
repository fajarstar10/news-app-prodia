package com.id.newsapp.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.id.newsapp.R

@Composable
fun NewsImage(
    imageUrl: String?,
    contentDescription: String? = null,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    val safeUrl = remember(imageUrl) {
        imageUrl?.replace("http://", "https://")
    }

    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current).data(safeUrl).crossfade(true).build(),
        contentDescription = contentDescription,
        placeholder = painterResource(R.drawable.placeholder),
        error = painterResource(R.drawable.image_error),
        contentScale = contentScale,
        modifier = modifier
    )
}
