package com.lmp.model

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitApiWrapper {
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
                .baseUrl("https://reddit.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    val redditService: RedditGetTopEntries by lazy {
        retrofit.create(RedditGetTopEntries::class.java)
    }
}