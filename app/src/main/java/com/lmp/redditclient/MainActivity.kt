package com.lmp.redditclient

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.lmp.model.EntryData
import com.lmp.presenter.RedditContract
import com.lmp.view.PaginationScrollListener
import com.lmp.view.RecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_connection_failed.*
import kotlinx.android.synthetic.main.main_error_with_message.*
import kotlinx.android.synthetic.main.main_loading_content.*
import org.koin.android.ext.android.inject


class MainActivity : AppCompatActivity(), RedditContract.IRedditView {

    private val presenter: RedditContract.IRedditPresenter by inject()

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: RecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        initAdapter()
        initScrollListener()

        presenter.attachView(this)

        initReconnectButton()
    }

    override fun getContext(): Context = this

    override fun addDataToView(newItems: List<EntryData>) {
        adapter.addItems(newItems)
    }

    override fun showLostConnectionError() {
        main_connection_failed.visibility = View.VISIBLE
    }

    override fun hideLostConnectionError() {
        main_connection_failed.visibility = View.GONE
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

    private fun initReconnectButton() {
        main_retry_button.setOnClickListener { view -> presenter.onReconnectClicked() }
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }
}
