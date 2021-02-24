package com.jerry.wuwen.logic.model

import com.google.gson.annotations.SerializedName

data class RegisterResponse(val username:String,val name:String,@SerializedName("error_msg") val msg:String)