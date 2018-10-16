package com.lmp.gui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.lmp.redditclient.R
import com.lmp.redditwrapper.RedditEntryModel
import com.lmp.redditwrapper.RedditWrapper

class RecyclerViewAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val ITEM: Int = 1
    private val FOOTER: Int = 2

    private val items = ArrayList<RedditEntryModel>(RedditWrapper.BATCH_SIZE)

    private lateinit var footerViewHolder: FooterViewHolder
    private var isFooterAdded = true

    override fun getItemViewType(position: Int): Int {
        return if (isLastPosition(position) && isFooterAdded) FOOTER else ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM -> ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cardview_item, parent, false))
            else -> FooterViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cardview_footer, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            ITEM -> onBindItemViewHolder(holder as ItemViewHolder, position)
            else -> onBindFooterViewHolder(holder as FooterViewHolder, position)
        }
    }

    fun onBindItemViewHolder(itemHolder: ItemViewHolder, position: Int) {
        itemHolder.setData(items[position], context)
    }

    fun onBindFooterViewHolder(footerHolder: FooterViewHolder, position: Int) {
        footerViewHolder = footerHolder
    }

    override fun getItemCount() = items.size

    fun addItems(newItems: List<RedditEntryModel>) {
        if (newItems.isNotEmpty()) {
            val lastPosition = items.size
            items.addAll(newItems)
            notifyItemRangeInserted(lastPosition, newItems.size)
        }
    }

    fun isLastPosition(position: Int): Boolean = (position == items.size - 1)
}

