package com.jerry.wuwen.logic.network

import com.jerry.wuwen.logic.model.VideoRequest
import com.jerry.wuwen.logic.model.VideoResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Query

interface VideoServise {
    @GET("x/web-interface/search/type")
    fun askVideo(@Query("search_type")type:String,
                 @Query("keyword")key: String,
                 @Query("order")order:String,
                 @Query("page")page:Int): Call<VideoResponse>
}