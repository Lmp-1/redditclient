package com.lmp.gui

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.net.Uri
import android.support.customtabs.CustomTabsIntent
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.lmp.redditclient.R
import com.lmp.redditwrapper.RedditEntryModel
import com.lmp.redditwrapper.RedditEntryModelData
import com.lmp.util.formatMillisAsBiggestTimeUnit
import com.lmp.util.formatWithSuffix
import com.lmp.util.setBold


class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
    private val title: TextView = view.findViewById(R.id.cardview_title)
    private val score: TextView = view.findViewById(R.id.cardview_score)
    private val author: TextView = view.findViewById(R.id.cardview_created_by_text)
    private val numComments: TextView = view.findViewById(R.id.cardview_num_comments)
    private val image: ImageView = view.findViewById(R.id.cardview_image)

    private lateinit var url: String

    init {
        view.setOnClickListener(this)
    }

    fun setData(model: RedditEntryModel, context: Context) {
        val data = model.data
        val resources = context.resources
        title.text = data.title
        score.text = data.score.formatWithSuffix()
        setAuthorAndSubredditText(data, resources)
        setNumberOfComments(data.num_comments, resources)
        setImage(data.thumbnail, context)
        setUrl(data.permalink)
    }

    private fun setAuthorAndSubredditText(data: RedditEntryModelData, resources: Resources) {
        val authorAndSubredditText = String.format(
                resources.getString(R.string.created_by_string),
                data.author.setBold(),
                data.subreddit_name_prefixed.setBold(),
                formatTimeCreated(data.created, resources))

        // We don't want to create separate textviews for each field and make some of them bold
        // by adding textStyle="bold" because we don't want to create excess objects in our app.
        // But also we can't add <b></b> tags to named variables in strings.xml
        // So we are forced to add bold tags here and use Html.fromHtml()
        author.text = Html.fromHtml(authorAndSubredditText)
    }

    private fun formatTimeCreated(time: Long, resources: Resources) : String =
            (System.currentTimeMillis() - time * 1000L).formatMillisAsBiggestTimeUnit(resources)

    private fun setNumberOfComments(numberOfComments: Long, resources: Resources) {
        if (numberOfComments < 1000) {
            // We need to use 'plurals' feature when number of comments is less than 1000 to
            // make it easier to translate the app to other languages
            numComments.text = resources.getQuantityString(R.plurals.number_of_comments, numberOfComments.toInt(), numberOfComments)
        } else {
            // If there are lots of comments, we format this to look like '25.3k' or '3.0M' like
            // it is made on reddit and always use plural number, so I just typed '9999' in quantity field.
            // I could use just 999 or 1005 or almost any other plural number, but to point out
            // that this is really a lot, I used 9999 because, you know, it's "OVER NINE THOUSAAAAAAND!" (c)
            numComments.text = resources.getQuantityString(R.plurals.number_of_comments, 9999, numberOfComments.formatWithSuffix())
        }
    }

    private fun setImage(url: String, context: Context) {
        Glide.with(context).load(url).into(image)
    }

    private fun setUrl(permalink: String) {
        url = "https://www.reddit.com$permalink"
    }

    override fun onClick(v: View) {
        val builder = CustomTabsIntent.Builder()
        builder.setToolbarColor(Color.rgb(255, 69, 0))
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(v.context, Uri.parse(url))
    }
}

class FooterViewHolder(view: View) : RecyclerView.ViewHolder(view)