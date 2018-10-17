package com.lmp.model

import com.google.gson.annotations.SerializedName


data class EntriesResponse (val data: ResponseData)

data class ResponseData(
        val children: List<Entry>,
        val after: String
)

data class Entry (
        val data: EntryData
)

data class EntryData (
        val title: String,
        val author: String,
        @SerializedName("subreddit_name_prefixed")
        val subreddit: String,
        val created: Long,
        val score: Long,
        @SerializedName("num_comments")
        val numComments: Long,
        val thumbnail: String,
        val permalink: String
)