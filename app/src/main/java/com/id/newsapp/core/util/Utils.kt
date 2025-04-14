package com.id.newsapp.core.util

import java.util.Calendar

fun getGreetingTime(): String {
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    return when (hour) {
        in 4..10 -> "pagi"
        in 11..14 -> "siang"
        in 15..17 -> "sore"
        else -> "malam"
    }
}