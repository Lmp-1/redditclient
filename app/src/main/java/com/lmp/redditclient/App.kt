package com.lmp.redditclient

import android.app.Application
import com.lmp.model.*
import com.lmp.presenter.RedditContract
import com.lmp.presenter.RedditPresenter
import io.realm.Realm
import org.koin.android.ext.android.startKoin
import org.koin.dsl.module.Module
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single { RetrofitApiWrapper() }
    single<RedditContract.IRedditModel> { RedditApiWrapper(get()) }
    factory<RedditContract.IRedditPresenter> { RedditPresenter(get()) }
}

val appDebugModule = module {
    single<RedditContract.IRedditModel> { MockRedditApiWrapper() }
    factory<RedditContract.IRedditPresenter> { RedditPresenter(get()) }
}

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin(this, listOf(appModule))
    }
}