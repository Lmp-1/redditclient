package com.lmp.redditwrapper

import com.lmp.gui.EventManager
import com.lmp.gui.RecyclerViewAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.net.UnknownHostException

class RedditWrapper(private val recyclerViewAdapter: RecyclerViewAdapter, private val eventManager: EventManager) {

    private lateinit var lastBatchFullname: String

    companion object{
        const val BATCH_SIZE: Int = 50
    }

    var isLoading: Boolean = false

    private val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://reddit.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

    private lateinit var redditService: RedditGetTopEntries

    fun getFirstBatch() {
        isLoading = true
        redditService = retrofit.create(RedditGetTopEntries::class.java)
        redditService.getFirstBatch()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe ({
                    result -> recyclerViewAdapter.addItems(result.data.children)
                    lastBatchFullname = result.data.after
                    eventManager.hideLoader()
                    isLoading = false
                }, {
                    error ->
                    if (error is UnknownHostException) {
                        eventManager.showConnectionFailError()
                        eventManager.hideLoader()
                    } else {
                        eventManager.showErrorWithMessage(error.message)
                        eventManager.hideLoader()
                    }
                    error.printStackTrace()
                })
    }

    fun getNextBatch() {
        isLoading = true
        redditService.getNextBatch(lastBatchFullname)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe ({
                    result -> recyclerViewAdapter.addItems(result.data.children)
                    lastBatchFullname = result.data.after
                    isLoading = false
                }, {
                    error ->
                    if (error is UnknownHostException) {
                        eventManager.showConnectionFailError()
                    } else {
                        eventManager.showErrorWithMessage(error.message)
                    }
                    error.printStackTrace()
                })
    }
}