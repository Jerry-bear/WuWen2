package com.jerry.wuwen.ui.register


import android.util.Log
import androidx.lifecycle.Transformations
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.jerry.wuwen.logic.Repository
import com.jerry.wuwen.logic.model.LoginRequest
import com.jerry.wuwen.logic.model.LoginResponse
import com.jerry.wuwen.logic.model.RegisterCodeRequest
import com.jerry.wuwen.logic.model.RegisterRequest

import retrofit2.Response

class RegisterViewModel:ViewModel() {
    val requestCodeBodyLiveData= MutableLiveData<RegisterCodeRequest>()
    val requestBodyLiveData= MutableLiveData<RegisterRequest>()
    var second=0
    var ifcodeable=true

    val responseCodeLiveData=Transformations.switchMap(requestCodeBodyLiveData){requestCodeBodyLiveData->
        Log.d("调用viewmodel","code")
        Repository.register_code(requestCodeBodyLiveData)
    }
    val responseLiveData=Transformations.switchMap(requestBodyLiveData){requestBodyLiveData->
        Log.d("调用viewmodel","register")
        Repository.register(requestBodyLiveData)
    }


    fun registerCode(registerCodeRequest: RegisterCodeRequest){
        requestCodeBodyLiveData.value=registerCodeRequest
    }
    fun register(registerRequest: RegisterRequest){
        Log.d("调用仓库曾","register")
        requestBodyLiveData.value=registerRequest
    }
}