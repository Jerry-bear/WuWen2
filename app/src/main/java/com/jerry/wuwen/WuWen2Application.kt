package com.jerry.wuwen

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.jerry.wuwen.logic.model.VideoRequest
import com.jerry.wuwen.logic.model.VideoResponse

class WuWen2Application :Application() {
    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var context:Context
        lateinit var USERNAME:String//用户账号
        lateinit var EMAIL:String//邮箱
        lateinit var NAME:String//用户名
        lateinit var TELE:String//电话
        lateinit var Token:String
        var ifactionmovie=true
        lateinit var videoResponse:VideoResponse//访问网络的response对象
    }

    override fun onCreate() {
        super.onCreate()
        context.applicationContext
    }
}