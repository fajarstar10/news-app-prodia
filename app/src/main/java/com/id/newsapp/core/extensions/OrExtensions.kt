package com.id.newsapp.core.extensions

// String
fun String?.orEmpty(): String = this ?: ""
fun String?.orDash(): String = if (isNullOrBlank()) "-" else this

// Int
fun Int?.orZero(): Int = this ?: 0

// Long
fun Long?.orZero(): Long = this ?: 0L

// Double
fun Double?.orZero(): Double = this ?: 0.0

// Float
fun Float?.orZero(): Float = this ?: 0f

// Boolean
fun Boolean?.orFalse(): Boolean = this ?: false
fun Boolean?.orTrue(): Boolean = this ?: true

// List
fun <T> List<T>?.orEmpty(): List<T> = this ?: emptyList()

// Set
fun <T> Set<T>?.orEmpty(): Set<T> = this ?: emptySet()

// Map
fun <K, V> Map<K, V>?.orEmpty(): Map<K, V> = this ?: emptyMap()