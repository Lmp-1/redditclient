package com.lmp.model

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface RedditGetTopEntries {

    companion object{
        const val BATCH_SIZE: Int = 50
    }

    @GET("top/.json")
    fun getFirstBatch( @Query("limit") limit: Int = BATCH_SIZE): Observable<EntriesResponse>

    @GET("top/.json")
    fun getNextBatch(@Query("after") afterFullname: String, @Query("limit") limit: Int = BATCH_SIZE): Observable<EntriesResponse>
}