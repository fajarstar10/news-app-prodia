package com.id.newsapp.screen.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import com.id.newsapp.core.util.InactivityTimer

@Composable
fun MainScreenWithInactivityHandler(
    onTimeout: () -> Unit, content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    val timer = remember { InactivityTimer(onTimeout = onTimeout) }

    LaunchedEffect(Unit) {
        timer.start(scope)
    }

    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect {
            timer.reset(scope)
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            awaitPointerEventScope {
                while (true) {
                    awaitPointerEvent()
                    timer.reset(scope)
                }
            }
        }
        .clickable(interactionSource = interactionSource, indication = null) {}) {
        content()
    }
}