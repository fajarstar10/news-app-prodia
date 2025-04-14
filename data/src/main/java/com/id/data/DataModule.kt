package com.id.data

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.id.data.repository.AppPreferences
import com.id.data.repository.NewsRepositoryImpl
import com.id.data.repository.UserRepositoryImpl
import com.id.domain.repository.NewsRepository
import com.id.domain.repository.UserRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.CertificatePinner
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit

object NetworkConfig {
    const val ENABLE_SSL_PINNING = false
}

@OptIn(ExperimentalSerializationApi::class)
val dataModule = module {

    single<OkHttpClient> {
        val builder = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            val context = get<Context>()
            val chuckerInterceptor = ChuckerInterceptor.Builder(context).maxContentLength(250_000L)
                .alwaysReadResponseBody(true).build()

            builder.addInterceptor(chuckerInterceptor)
        }

        if (NetworkConfig.ENABLE_SSL_PINNING) {
            val certificatePinner = CertificatePinner.Builder().add(
                "api.spaceflightnewsapi.net", "sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA="
            ).add(
                "api.spaceflightnewsapi.net", "sha256/BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB="
            ).build()

            builder.certificatePinner(certificatePinner)
        }

        builder.build()
    }

    single<Json> {
        Json {
            ignoreUnknownKeys = true
            prettyPrint = false
            isLenient = true
            explicitNulls = false
        }
    }

    single<Retrofit> {
        val contentType = "application/json".toMediaType()
        Retrofit.Builder().baseUrl("https://api.spaceflightnewsapi.net/v4/").client(get())
            .addConverterFactory(get<Json>().asConverterFactory(contentType)).build()
    }

    single<NewsApiService> {
        get<Retrofit>().create(NewsApiService::class.java)
    }

    single<NewsRepository> {
        NewsRepositoryImpl(get())
    }

    single { AppPreferences(get()) }
    single<UserRepository> { UserRepositoryImpl(get()) }
}
