package com.id.newsapp.core.extensions

fun String.extractUsername(): String {
    return this.substringBefore("@").replace(".", " ").replace("_", " ").split(" ")
        .joinToString(" ") { it.replaceFirstChar { char -> char.uppercaseChar() } }
}