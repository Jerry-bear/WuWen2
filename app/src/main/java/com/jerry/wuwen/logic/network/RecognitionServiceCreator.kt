package com.jerry.wuwen.logic.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RecognitionServiceCreator {
    private const val BASE_URL="https://787127e2-3e82-44be-948e-cdb9f6bc5b42.mock.pstmn.io/"

    private val retrofit= Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    fun<T> create (serviceClass: Class<T>):T= retrofit.create(serviceClass)

    inline fun <reified T> create():T=create(T::class.java)
}