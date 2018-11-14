package com.lmp.presenter

import android.graphics.Color
import android.net.Uri
import android.support.customtabs.CustomTabsIntent
import com.lmp.model.EntryData
import java.net.UnknownHostException

class RedditPresenter(private val model: RedditContract.IRedditModel) : RedditContract.IRedditPresenter {

    private var view: RedditContract.IRedditView? = null

    override fun attachView(view: RedditContract.IRedditView) {
        this.view = view
        model.loadEntries(::onSuccess, ::onError)
    }

    override fun detachView() {
        view = null
        model.clear()
    }

    override fun onScrolledToBottom() {
        loadEntriesSecure()
    }

    override fun onReconnectClicked() {
        loadEntriesSecure()
    }

    override fun onClickedItem(permalink: String) {
        val context = view?.getContext()

        if (context != null) {
            val builder = CustomTabsIntent.Builder()
            builder.setToolbarColor(Color.rgb(255, 69, 0)) // color of reddit logo
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(context, Uri.parse(createFullUrl(permalink)))
        }
    }

    private fun onSuccess(data: List<EntryData>) {
        view?.addDataToView(data)
        view?.hideLostConnectionError()
        view?.hideInitialLoader()
    }

    private fun onError(error: Throwable) {
        if (error is UnknownHostException) {
            view?.showLostConnectionError()
        } else {
            view?.showErrorScreen(error.message)
            view?.hideInitialLoader()
        }
    }

    private fun loadEntriesSecure() {
        if (!model.isLoading()) {
            model.loadEntries(::onSuccess, ::onError)
        }
    }

    private fun createFullUrl(permalink: String) : String = "https://www.reddit.com$permalink"
}