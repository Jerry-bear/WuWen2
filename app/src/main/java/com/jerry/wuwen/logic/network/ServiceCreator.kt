package com.jerry.wuwen.logic.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
//创建Retrofit构建器
object ServiceCreator {
    private const val BASE_URL="http://120.27.234.244:8004/"

    private val retrofit=Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    fun<T> create (serviceClass: Class<T>):T= retrofit.create(serviceClass)

    inline fun <reified T> create():T=create(T::class.java)
}