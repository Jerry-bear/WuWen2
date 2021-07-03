package com.jerry.wuwen.logic.network


import com.jerry.wuwen.logic.model.LoginRequest
import com.jerry.wuwen.logic.model.LoginResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST("api/LoginUserPass/")
    fun askLogin(@Body loginRequest:LoginRequest): Call<LoginResponse>
}