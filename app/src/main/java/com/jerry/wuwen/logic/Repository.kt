package com.jerry.wuwen.logic

import android.util.Log
import androidx.lifecycle.liveData
import com.jerry.wuwen.logic.model.LoginRequest
import com.jerry.wuwen.logic.model.LoginResponse
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
                Result.failure(RuntimeException("密码或者账号错误"))
            }
        }catch (e:Exception){
            Log.d("错误信息E：",e.toString())
            Result.failure<LoginResponse>(e)
        }
        emit(result)
    }


}