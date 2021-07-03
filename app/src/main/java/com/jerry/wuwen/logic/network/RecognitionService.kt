package com.jerry.wuwen.logic.network

import com.jerry.wuwen.logic.model.RecognitionResponse
import com.jerry.wuwen.logic.model.RegisterResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Call
import retrofit2.http.POST

interface RecognitionService {
    @GET("api/getmessage/")
    fun recognition():Call<RecognitionResponse>
}