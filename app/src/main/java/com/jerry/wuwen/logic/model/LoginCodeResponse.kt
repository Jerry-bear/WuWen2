package com.jerry.wuwen.logic.model

data class LoginCodeResponse(val code:Int,val data:Data,val msg:String){
    data class Data(val captcha_result:Captcha_result){
        data class Captcha_result(val id:String,val base_64_blob:String,val code:String)
    }
}
//{
//    "code": 0,
//    "data": {
//      "captcha_result": {
//          "id": "feouSM9Ju3aMNC8QvBzc",
//          "base_64_blob": "data:image/png;base64,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",
//          "code": ""
//          }
//      },
//    "msg": "success"
//}