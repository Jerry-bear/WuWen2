package com.jerry.wuwen.logic.network

import android.util.Log
import com.jerry.wuwen.logic.model.LoginRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object WuWen2Network {



    private val loginService=ServiceCreator.create(LoginService::class.java)
    suspend fun askLogin(loginRequest: LoginRequest)= loginService.askLogin(loginRequest).await()


    private suspend fun <T> Call<T>.await():T{
        //Log.d("执行挂起函数","挂起")
        return suspendCoroutine {continuation ->
            //Log.d("返回数据前","调用回调函数")
            enqueue(object:Callback<T>{
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    Log.d("返回数据前","调用回调函数")
                    val body=response.body()
                    Log.d("返回数据","${body.toString()}")
                    if (body!=null)continuation.resume(body)
                    else continuation.resumeWithException(
                        RuntimeException("response body is null"))
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }

}