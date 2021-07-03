package com.jerry.wuwen.ui.gesture

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.jerry.wuwen.logic.Repository
import com.jerry.wuwen.logic.model.LoginRequest

class GestureViewModel:ViewModel() {
    var ifbox_run= MutableLiveData<Boolean>()
    var ifThreadTsTun=false

    var ifok=1
    private val ifrecognitionLiveData=MutableLiveData<Int>()

//    val recognitionLiveData=Transformations.switchMap(ifrecognitionLiveData){
//        ifok->Repository.askrecognition()
//    }

    fun recognition(ifok:Int){
        ifrecognitionLiveData.value=ifok
    }
    //用于访问后端获得的识别的数据
    val requestRecognitionBodyLiveData= MutableLiveData<Int>()
    val responseRecognition=Transformations.switchMap(requestRecognitionBodyLiveData){requestRecognitionBodyLiveData->
        Repository.askrecognition()}
    fun askRecognition(int: Int){
        requestRecognitionBodyLiveData.value=int
    }



}