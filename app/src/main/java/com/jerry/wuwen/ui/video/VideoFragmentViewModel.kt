package com.jerry.wuwen.ui.video

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.jerry.wuwen.logic.Repository
import com.jerry.wuwen.logic.model.LoginRequest
import com.jerry.wuwen.logic.model.Video
import com.jerry.wuwen.logic.model.VideoRequest

class VideoFragmentViewModel : ViewModel() {
    val requestBodyLiveData= MutableLiveData<VideoRequest>()


    val responseBodyLiveData= Transformations.switchMap(requestBodyLiveData){ requestBodyLiveData->
        Repository.askVideo(requestBodyLiveData.search_type,requestBodyLiveData.keyword,requestBodyLiveData.order,requestBodyLiveData.page)

    }


    fun askVideo(videoRequest: VideoRequest){
        requestBodyLiveData.value=videoRequest
    }
}