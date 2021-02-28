package com.jerry.wuwen.logic.model

import android.os.Message

data class VideoResponse(val code:Int,val message: String,val data:Data) {
    data class Data(val result:List<Result>){
        data class Result(val type:String,val author:String,val arcurl:String,val bvid:String,val title:String,val pic:String)
    }
}