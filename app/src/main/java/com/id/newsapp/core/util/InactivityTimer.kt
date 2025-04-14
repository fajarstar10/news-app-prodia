package com.id.newsapp.core.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class InactivityTimer(
    private val timeoutMillis: Long = 1 * 60 * 1000, // 1 menit
    private val onTimeout: () -> Unit
) {
    private var job: Job? = null

    fun start(scope: CoroutineScope) {
        stop()
        job = scope.launch {
            delay(timeoutMillis)
            onTimeout()
        }
    }

    fun reset(scope: CoroutineScope) {
        start(scope)
    }

    private fun stop() {
        job?.cancel()
        job = null
    }
}