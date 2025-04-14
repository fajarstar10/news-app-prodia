package com.id.newsapp.navigation

import com.id.domain.model.NewsCategory

object Route {
    const val LOGIN = "login"
    const val HOME = "home"
    const val REGISTER = "register"
    const val NEWS_DETAIL = "news_detail"
    const val CATEGORY_GRID = "category_grid"

    fun newsDetail(id: Int) = "$NEWS_DETAIL/$id"
    fun categoryGrid(category: NewsCategory) = "$CATEGORY_GRID/${category.name}"
}
