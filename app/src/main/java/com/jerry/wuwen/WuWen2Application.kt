package com.jerry.wuwen

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class WuWen2Application :Application() {
    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var context:Context

    }

    override fun onCreate() {
        super.onCreate()
        context.applicationContext
    }
}