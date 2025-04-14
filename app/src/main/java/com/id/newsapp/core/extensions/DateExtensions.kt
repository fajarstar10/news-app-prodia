package com.id.newsapp.core.extensions

import android.os.Build
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.TimeZone

fun String.toDisplayDate(): String {
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val parsed = ZonedDateTime.parse(this)
            val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale("id"))
            parsed.format(formatter)
        } else {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")
            val date = inputFormat.parse(this)

            val outputFormat = SimpleDateFormat("d MMMM yyyy", Locale("id"))
            outputFormat.format(date ?: return this)
        }
    } catch (e: Exception) {
        this
    }
}