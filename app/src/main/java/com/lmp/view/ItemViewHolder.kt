package com.lmp.view

import android.content.Context
import android.content.res.Resources
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.View
import android.view.ViewGroup
import com.lmp.model.Entry
import com.lmp.model.EntryData
import com.lmp.redditclient.R
import com.lmp.util.formatMillisAsBiggestTimeUnit
import com.lmp.util.formatWithSuffix
import com.lmp.util.inflate
import com.lmp.util.setBold
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.cardview_item.*


class ItemViewHolder(override val containerView: View, private val click: (String) -> Unit) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    companion object {
        fun newInstance(parent: ViewGroup, click: (String) -> Unit) =
                ItemViewHolder(parent.inflate(R.layout.cardview_item), click)
    }

    fun setData(model: Entry, context: Context) {
        val data = model.data
        val resources = context.resources
        title.text = data.title
        score.text = data.score.formatWithSuffix()
        setAuthorAndSubredditText(data, resources)
        setNumberOfComments(data.numComments, resources)
        setImage(data.thumbnail)

        containerView.setOnClickListener {
            click.invoke(data.permalink)
        }
    }

    private fun setAuthorAndSubredditText(data: EntryData, resources: Resources) {
        val authorAndSubredditText = String.format(
                resources.getString(R.string.created_by_string),
                data.author.setBold(),
                data.subreddit.setBold(),
                formatTimeCreated(data.created, resources))

        // We don't want to create separate textviews for each field and make some of them bold
        // by adding textStyle="bold" because we don't want to create excess objects in our app.
        // But also we can't add <b></b> tags to named variables in strings.xml
        // So we are forced to add bold tags here and use Html.fromHtml()
        created_by_text.text = Html.fromHtml(authorAndSubredditText)
    }

    private fun formatTimeCreated(time: Long, resources: Resources) : String =
            (System.currentTimeMillis() - time * 1000L).formatMillisAsBiggestTimeUnit(resources)

    private fun setNumberOfComments(numberOfComments: Long, resources: Resources) {
        if (numberOfComments < 1000) {
            // We need to use 'plurals' feature when number of comments is less than 1000 to
            // make it easier to translate the app to other languages
            num_comments.text = resources.getQuantityString(R.plurals.number_of_comments, numberOfComments.toInt(), numberOfComments)
        } else {
            // If there are lots of comments, we format this to look like '25.3k' or '3.0M' like
            // it is made on reddit and always use plural number, so I just typed '9999' in quantity field.
            // I could use just 999 or 1005 or almost any other plural number, but to point out
            // that this is really a lot, I used 9999 because, you know, it's "OVER NINE THOUSAAAAAAND!" (c)
            num_comments.text = resources.getQuantityString(R.plurals.number_of_comments, 9999, numberOfComments.formatWithSuffix())
        }
    }

    private fun setImage(url: String) {
        Picasso.get().load(url).into(image)
    }
}

class FooterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    companion object {
        fun newInstance(parent: ViewGroup) =
                FooterViewHolder(parent.inflate(R.layout.cardview_footer))
    }
}