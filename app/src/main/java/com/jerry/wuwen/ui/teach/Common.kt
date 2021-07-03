package com.jerry.wuwen.ui.teach

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import com.jerry.wuwen.R
import kotlinx.android.synthetic.main.activity_common.*
import kotlinx.android.synthetic.main.activity_video_web.*

class Common : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //使状态栏和背景融合
        val decorView=window.decorView//拿到当前的Activity的DecorView
        decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE//表示Activity的布局会显示到状态栏上面
        window.statusBarColor= Color.TRANSPARENT//最后调用一下statusBarColor()方法将状态栏设置为透明色
        setContentView(R.layout.activity_common)
        commen_web_webview.settings.javaScriptEnabled=true
        commen_web_webview.webViewClient= WebViewClient()
        commen_web_webview.settings.useWideViewPort=true
        commen_web_webview.settings.loadWithOverviewMode=true
        commen_web_webview.loadUrl("http://shouyu.z12345.com/3-405.html")
    }
}