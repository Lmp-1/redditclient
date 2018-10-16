package com.lmp.redditwrapper


data class GetEntriesResponseModel (
        val kind: String,
        val data: GetEntriesResponseModelData
)

data class GetEntriesResponseModelData(
        val children: List<RedditEntryModel>,
        val after: String
)

data class RedditEntryModel (
        val data: RedditEntryModelData
)

data class RedditEntryModelData (
        val title: String,
        val author: String,
        val subreddit_name_prefixed: String,
        val created: Long,
        val score: Long,
        val num_comments: Long,
        val thumbnail: String,
        val permalink: String
)