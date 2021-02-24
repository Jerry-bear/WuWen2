package com.jerry.wuwen.logic.network

import com.jerry.wuwen.logic.model.RegisterRequest
import com.jerry.wuwen.logic.model.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RegisterService {
    @POST("register/register/")
    fun register(@Body registerRequest: RegisterRequest): Call<RegisterResponse>
}