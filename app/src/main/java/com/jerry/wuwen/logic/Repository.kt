package com.jerry.wuwen.logic

import android.util.Log
import androidx.lifecycle.liveData
import com.jerry.wuwen.logic.model.*
import com.jerry.wuwen.logic.network.WuWen2Network
import kotlinx.coroutines.Dispatchers
import java.lang.RuntimeException

object Repository {

    //登录
    fun askLogin(loginRequest: LoginRequest)= liveData(Dispatchers.IO) {
        val result=try {
            Log.d("仓库层","调用askLogin")
            val loginResponse=WuWen2Network.askLogin(loginRequest)
            Log.d("仓库层","调用askLogin成功")
            if(loginResponse.erros!=null){
                Result.success(loginResponse)
            }else{
                Result.failure(RuntimeException("网络超时"))
            }
        }catch (e:Exception){
            Log.d("错误信息E：",e.toString())
            Result.failure<LoginResponse>(e)
        }
        emit(result)
    }
    //注册发送验证码
    fun register_code(registerCodeRequest: RegisterCodeRequest)= liveData(Dispatchers.IO) {
        val result=try {
            val registerCodeResponse=WuWen2Network.registercode(registerCodeRequest)
            if(registerCodeResponse.msg!=null){
                Result.success(registerCodeResponse)
            }else{
                Result.failure(RuntimeException("网络超时"))
            }
        }catch (e:Exception){
            Result.failure<RegisterCodeResponse>(e)
        }
        emit(result)
    }
    fun register(registerRequest: RegisterRequest)= liveData(Dispatchers.IO) {
        val result=try {
            val registerResponse=WuWen2Network.register(registerRequest)
            Log.d("测试网络",registerResponse.toString())
            if(registerResponse.msg!=null){
                Result.success(registerResponse)
            }else{
                Result.failure(RuntimeException("网络超时"))
            }
        }catch (e:Exception){
            Result.failure<RegisterResponse>(e)
        }
        emit(result)
    }


}