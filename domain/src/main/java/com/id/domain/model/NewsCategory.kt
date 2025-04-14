package com.id.domain.model

enum class NewsCategory(val endpoint: String, val displayName: String) {
    ARTICLES("articles", "Articles"), BLOGS("info", "Blogs"), REPORTS("reports", "Reports");
}