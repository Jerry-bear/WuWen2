package com.jerry.wuwen

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class WuWen2Application :Application() {
    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var context:Context
        lateinit var USERNAME:String//用户账号
        lateinit var EMAIL:String//邮箱
        lateinit var NAME:String//用户名
        lateinit var TELE:String//电话
        var ifactionmovie=true
    }

    override fun onCreate() {
        super.onCreate()
        context.applicationContext
    }
}