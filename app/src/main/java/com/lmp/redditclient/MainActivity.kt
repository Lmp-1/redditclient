package com.lmp.redditclient

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.lmp.model.Entry
import com.lmp.presenter.RedditContract
import com.lmp.presenter.RedditPresenter
import com.lmp.view.PaginationScrollListener
import com.lmp.view.RecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_error_with_message.*
import kotlinx.android.synthetic.main.main_loading_content.*
import kotlinx.android.synthetic.main.toolbar.*


class MainActivity : AppCompatActivity(), RedditContract.IRedditView {

    private lateinit var presenter: RedditContract.IRedditPresenter

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: RecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        initAdapter()
        initScrollListener()

        presenter = RedditPresenter()
        presenter.attachView(this)
    }

    override fun getContext(): Context = this

    override fun addDataToView(newItems: List<Entry>) {
        adapter.addItems(newItems)
    }
    
    override fun showLostConnectionText() {
        toolbar_text.text = resources.getString(R.string.reconnecting_text)
    }

    override fun hideLostConnectionText() {
        toolbar_text.text = resources.getString(R.string.app_header)
    }

    override fun showErrorScreen(errorMessage: String?) {
        if (errorMessage != null && errorMessage.isNotBlank()) {
            main_error_message_text.text = errorMessage
        }
        main_error_with_message.visibility = View.VISIBLE
    }

    override fun hideInitialLoader() {
        main_loading_content.visibility = View.GONE
    }

    private fun initAdapter() {
        recyclerView.layoutManager = LinearLayoutManager(this).apply {
            linearLayoutManager = this
        }
        recyclerView.adapter = RecyclerViewAdapter(this, ::onClickItem).apply {
            adapter = this
        }
    }

    private fun initScrollListener() {
        recyclerView.addOnScrollListener(object : PaginationScrollListener(linearLayoutManager) {
            override fun onScrolledToBottom() {
                presenter.onScrolledToBottom()
            }
        })
    }

    private fun onClickItem(permalink: String) {
        presenter.onClickedItem(permalink)
    }

    override fun onStop() {
        presenter.detachView()
        super.onStop()
    }
}
