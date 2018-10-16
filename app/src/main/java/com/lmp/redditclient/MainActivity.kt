package com.lmp.redditclient

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.lmp.gui.EventManager
import com.lmp.gui.PaginationScrollListener
import com.lmp.gui.RecyclerViewAdapter
import com.lmp.redditwrapper.RedditWrapper
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        init()
    }

    private fun init() {
        val recyclerViewAdapter = initAdapter()
        val eventManager = initEventManager()
        val redditWrapper = initRedditWrapper(recyclerViewAdapter, eventManager)
        initScrollListener(redditWrapper)
    }

    private fun initAdapter() : RecyclerViewAdapter {
        linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        var adapter = RecyclerViewAdapter(this)
        recyclerView.adapter = adapter
        return adapter
    }

    private fun initEventManager() : EventManager {
        val eventManager = EventManager(findViewById(R.id.main_view))
        return eventManager
    }

    private fun initRedditWrapper(recyclerViewAdapter: RecyclerViewAdapter, eventManager: EventManager) : RedditWrapper {
        val redditWrapper = RedditWrapper(recyclerViewAdapter, eventManager)
        redditWrapper.getFirstBatch()
        return redditWrapper
    }

    private fun initScrollListener(redditWrapper: RedditWrapper) {
        recyclerView.addOnScrollListener(object : PaginationScrollListener(linearLayoutManager) {
            override fun loadMoreItems() {
                redditWrapper.getNextBatch()
            }

            override fun isLoading() : Boolean = redditWrapper.isLoading
        })
    }
}
