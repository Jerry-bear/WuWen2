package com.jerry.wuwen.ui.login
import androidx.lifecycle.Transformations
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.jerry.wuwen.logic.Repository
import com.jerry.wuwen.logic.model.LoginRequest
import com.jerry.wuwen.logic.model.LoginResponse
import retrofit2.Response

class LoginViewModel:ViewModel() {
    val requestBodyLiveData= MutableLiveData<LoginRequest>()

    val responseBodyLiveData=Transformations.switchMap(requestBodyLiveData){requestBodyLiveData->
        Repository.askLogin(requestBodyLiveData)

    }


    fun askLogin(loginRequest: LoginRequest){
        requestBodyLiveData.value=loginRequest
    }
}