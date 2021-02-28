package com.jerry.wuwen.logic.network

import android.util.Log
import com.jerry.wuwen.logic.model.LoginRequest
import com.jerry.wuwen.logic.model.RegisterCodeRequest
import com.jerry.wuwen.logic.model.RegisterRequest
import com.jerry.wuwen.logic.model.VideoRequest
import com.jerry.wuwen.logic.network.WuWen2Network.await
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object WuWen2Network {


    //登录
    private val loginService=ServiceCreator.create(LoginService::class.java)
    suspend fun askLogin(loginRequest: LoginRequest)= loginService.askLogin(loginRequest).await()


    //注册发送验证码
    private val registercodeService=ServiceCreator.create(RegisterCodeService::class.java)
    suspend fun registercode(registerCodeRequest: RegisterCodeRequest)= registercodeService.registerCode(registerCodeRequest).await()

    //注册
    private val registerService=ServiceCreator.create(RegisterService::class.java)
    suspend fun register(registerRequest: RegisterRequest)= registerService.register(registerRequest).await()

    //Bilinili获取数据
    private val videoService=BilibiliServiceCreator.create(VideoServise::class.java)
    suspend fun video( search_type:String, keyword:String, order:String, page:Int)= videoService.askVideo( search_type,keyword,order,page).await()


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