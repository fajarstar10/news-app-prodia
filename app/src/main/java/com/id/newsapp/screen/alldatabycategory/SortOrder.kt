package com.id.newsapp.screen.alldatabycategory

enum class SortOrder {
    ASCENDING, DESCENDING;

    fun toggle(): SortOrder {
        return if (this == ASCENDING) DESCENDING else ASCENDING
    }
}