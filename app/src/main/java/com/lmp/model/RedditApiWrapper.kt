package com.lmp.model

import com.lmp.presenter.RedditContract
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class RedditApiWrapper (private val retrofitApi: RetrofitApiWrapper) : RedditContract.IRedditModel {

    private var lastBatchFullname: String? = null
    private var isLoading: Boolean = false
    private val disposables = CompositeDisposable()

    private fun createObservable(lastBatchFullname: String) : Observable<EntriesResponse> {
        return if (lastBatchFullname.isBlank()) {
            retrofitApi.redditService.getFirstBatch()
        } else {
            retrofitApi.redditService.getNextBatch(lastBatchFullname)
        }
    }

    override fun loadEntries(onSuccess: (List<EntryData>) -> Unit, onError: (Throwable) -> Unit) {
        isLoading = true

        val observable = createObservable(lastBatchFullname ?: "")

        val disposable = observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe ({
                    result ->
                    onSuccess.invoke(result.data.children.map { it.data })
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