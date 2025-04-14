package com.id.data.di

import com.id.data.NewsApiService
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {

    single {
        val certificatePinner = CertificatePinner.Builder()
            .add("api.spaceflightnewsapi.net", "sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=")
            .add("api.spaceflightnewsapi.net", "sha256/BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB=")
            .build()

        OkHttpClient.Builder()
            .certificatePinner(certificatePinner)
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://api.spaceflightnewsapi.net/v4/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<NewsApiService> {
        get<Retrofit>().create(NewsApiService::class.java)
    }
}
