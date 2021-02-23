package com.jerry.wuwen.ui.gesture

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jerry.wuwen.logic.model.LoginRequest

class GestureViewModel:ViewModel() {
    var ifbox_run= MutableLiveData<Boolean>()
    var ifThreadTsTun=false
}