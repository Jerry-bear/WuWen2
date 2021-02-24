package com.jerry.wuwen.logic.network

import com.jerry.wuwen.logic.model.RegisterCodeRequest
import com.jerry.wuwen.logic.model.RegisterCodeResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RegisterCodeService {
    @POST("/register/code/")
    fun registerCode(@Body registerCodeRequest: RegisterCodeRequest): Call<RegisterCodeResponse>
}