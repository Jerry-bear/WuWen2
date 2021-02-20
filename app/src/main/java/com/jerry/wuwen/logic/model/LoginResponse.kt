package com.jerry.wuwen.logic.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(val erros:Int,val data:Data){
    data class Data(val username:String,
                    val password:String,
                    val name:String,
                    val email:String,
                    val tele:String,
                    @SerializedName("error_msg") val msg:String)
}
