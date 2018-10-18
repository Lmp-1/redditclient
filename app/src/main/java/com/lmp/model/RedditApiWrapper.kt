package com.lmp.model

import android.util.Log
import com.lmp.presenter.RedditContract
import com.lmp.redditclient.App.ApiManager.redditService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class RedditApiWrapper : RedditContract.IRedditModel {

    private var lastBatchFullname: String? = null
    private var isLoading: Boolean = false
    private val disposables = CompositeDisposable()

    private fun createObservable(lastBatchFullname: String) : Observable<EntriesResponse> {
        return if (lastBatchFullname.isBlank()) {
             redditService.getFirstBatch()
        } else {
            redditService.getNextBatch(lastBatchFullname)
        }
    }

    override fun loadEntries(onSuccess: (EntriesResponse) -> Unit, onError: (Throwable) -> Unit) {
        isLoading = true

        val observable = createObservable(lastBatchFullname ?: "")

        Log.e("ololo", "loadEntries $lastBatchFullname")

        val disposable = observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe ({
                    result ->
                    onSuccess.invoke(result)
                    lastBatchFullname = result.data.after
                    isLoading = false
                }, {
                    error ->
                    onError.invoke(error)
                    isLoading = false
                })

        disposables.add(disposable)
    }

    override fun isLoading(): Boolean = isLoading

    override fun clear() {
        disposables.clear()
    }
}