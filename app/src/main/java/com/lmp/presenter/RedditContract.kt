package com.lmp.presenter

import android.content.Context
import com.lmp.model.EntryData

interface RedditContract {

    interface IRedditModel {
        fun loadEntries(onSuccess: (List<EntryData>) -> Unit, onError: (Throwable) -> Unit)

        fun isLoading() : Boolean

        fun clear()
    }

    interface IRedditView {

        fun getContext() : Context

        fun addDataToView(newItems: List<EntryData>)

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