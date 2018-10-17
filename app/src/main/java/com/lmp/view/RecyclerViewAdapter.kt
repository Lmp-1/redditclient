package com.lmp.view

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.lmp.model.Entry

class RecyclerViewAdapter(private val context: Context, private val click: (String) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val ITEM: Int = 0
        private const val FOOTER: Int = 1
    }

    private val items = mutableListOf<Entry>()

    private lateinit var footerViewHolder: FooterViewHolder
    private var isFooterAdded = true

    override fun getItemViewType(position: Int): Int {
        return if (isLastPosition(position) && isFooterAdded) FOOTER else ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM -> ItemViewHolder.newInstance(parent, click)
            else -> FooterViewHolder.newInstance(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            ITEM -> onBindItemViewHolder(holder as ItemViewHolder, position)
            else -> onBindFooterViewHolder(holder as FooterViewHolder)
        }
    }

    private fun onBindItemViewHolder(itemHolder: ItemViewHolder, position: Int) {
        itemHolder.setData(items[position], context)
    }

    private fun onBindFooterViewHolder(footerHolder: FooterViewHolder) {
        footerViewHolder = footerHolder
    }

    override fun getItemCount() = items.size

    fun addItems(newItems: List<Entry>) {
        if (newItems.isNotEmpty()) {
            val lastPosition = items.size
            items.addAll(newItems)
            notifyItemRangeInserted(lastPosition, newItems.size)
        }
    }

    fun isLastPosition(position: Int): Boolean = (position == items.size - 1)
}

