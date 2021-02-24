package com.jerry.wuwen.ui.maininterface

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jerry.wuwen.logic.model.LoginRequest

class MaininterfaceViewModel:ViewModel() {
    var ifviprun= MutableLiveData<Int>()
    var ifvipmove=true
}