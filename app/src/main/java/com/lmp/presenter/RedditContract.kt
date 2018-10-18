package com.lmp.presenter

import android.content.Context
import com.lmp.model.EntriesResponse
import com.lmp.model.Entry
import java.util.concurrent.TimeUnit

interface RedditContract {

    interface IRedditModel {
        fun loadEntries(onSuccess: (EntriesResponse) -> Unit, onError: (Throwable) -> Unit)

        fun isLoading() : Boolean

        fun clear()
    }

    interface IRedditView {

        fun getContext() : Context

        fun addDataToView(newItems: List<Entry>)

        fun showLostConnectionError()

        fun hideLostConnectionError()

        fun showErrorScreen(errorMessage: String?)

        fun hideInitialLoader()
    }

    interface IRedditPresenter {

        fun attachView(view: IRedditView)

        fun detachView()

        fun onScrolledToBottom()

        fun onReconnectClicked()

        fun onClickedItem(permalink: String)
    }
}