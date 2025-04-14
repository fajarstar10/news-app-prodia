package com.id.domain.di

import com.id.domain.usecase.GetArticlesUseCase
import com.id.domain.usecase.GetBlogsUseCase
import com.id.domain.usecase.GetReportsUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { GetArticlesUseCase(get()) }
    factory { GetBlogsUseCase(get()) }
    factory { GetReportsUseCase(get()) }
}