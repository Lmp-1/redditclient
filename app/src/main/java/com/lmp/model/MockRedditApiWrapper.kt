package com.lmp.model

import android.util.Log
import com.lmp.presenter.RedditContract

class MockRedditApiWrapper : RedditContract.IRedditModel {

    companion object {
        const val MOCK_BATCH_SIZE = 5
    }

    private var lastItemIndex = (-1).toString()
    private var isLoading = false

    override fun loadEntries(onSuccess: (List<EntryData>) -> Unit, onError: (Throwable) -> Unit) {
        isLoading = true

        onSuccess(createMockResponse())
    }

    override fun isLoading(): Boolean {
        // If we loaded some data, we want to emulate a dalay. First call to .isLoading will return
        // true, next call will return false
        return if (isLoading) {
            isLoading = false
            true
        } else {
            false
        }
    }

    override fun clear() {
        Log.d("MockReddit", "model cleared")
    }

    private fun createMockResponse() : List<EntryData> {
        val entryDataList = ArrayList<EntryData>()

        val lastItemIndex = this.lastItemIndex.toInt() + 1

        for (i in lastItemIndex..(lastItemIndex + MOCK_BATCH_SIZE)) {
            entryDataList.add(createMockEntryData(i))
        }

        this.lastItemIndex = (lastItemIndex + MOCK_BATCH_SIZE).toString()

        return entryDataList
    }

    private fun createMockEntryData(index: Int) : EntryData {
        return EntryData(
                "Title $index",
                "username$index",
                "r/mock",
                // random time from now down to one day ago
                System.currentTimeMillis() / 1000 - (Math.random() * 60.0 * 60.0 * 24).toLong(),
                // random score from 1 to 5000
                (Math.random() * 4999.0 + 1.0).toLong(),
                // random number of comments from 1 to 5000
                (Math.random() * 4999.0 + 1.0).toLong(),
                // sad Keanu art
                "https://ih1.redbubble.net/image.569129150.9303/flat,550x550,075,f.u2.jpg",
                "/top"
                )
    }
}