package com.id.newsapp.di

import com.id.domain.usecase.GetArticlesUseCase
import com.id.domain.usecase.GetBlogsUseCase
import com.id.domain.usecase.GetNewsDetailUseCase
import com.id.domain.usecase.GetReportsUseCase
import com.id.newsapp.screen.homescreen.viewmodel.HomeViewModel
import com.id.newsapp.screen.login.viewmodel.LoginViewModel
import com.id.newsapp.screen.register.viewmodel.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { HomeViewModel(get(), get(), get(), get(), get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    factory { GetArticlesUseCase(get()) }
    factory { GetBlogsUseCase(get()) }
    factory { GetReportsUseCase(get()) }
    factory { GetNewsDetailUseCase(get()) }
}
