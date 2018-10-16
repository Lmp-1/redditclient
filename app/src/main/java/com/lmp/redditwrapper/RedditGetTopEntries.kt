package com.lmp.redditwrapper

import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RedditGetTopEntries {

    @GET("top/.json")
    fun getFirstBatch( @Query("limit") limit: Int = RedditWrapper.BATCH_SIZE): Observable<GetEntriesResponseModel>

    @GET("top/.json")
    fun getNextBatch(@Query("after") afterFullname: String, @Query("limit") limit: Int = RedditWrapper.BATCH_SIZE): Observable<GetEntriesResponseModel>
}