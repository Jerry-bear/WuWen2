package com.jerry.wuwen.ui.video

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.jerry.wuwen.R
import kotlinx.android.synthetic.main.activity_video_web.*

class VideoWebActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //使状态栏和背景融合
        val decorView=window.decorView//拿到当前的Activity的DecorView
        decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE//表示Activity的布局会显示到状态栏上面
        window.statusBarColor= Color.TRANSPARENT//最后调用一下statusBarColor()方法将状态栏设置为透明色
        setContentView(R.layout.activity_video_web)
        val extraData=intent.getStringExtra("webname")
        webView.settings.javaScriptEnabled=true
        webView.webViewClient= WebViewClient()
        Log.d("webView",":${extraData}")

        webView.webViewClient = object: WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                return if(url!!.startsWith("http:")||url.startsWith("https:")){
                    //对http或者https协议的链接进行加载
                    view!!.loadUrl(url)
                    true
                }else{
                    //这里需要捕捉异常，因为如果没有安装相关的APP会有类找不到的异常
                    try {
                        //启动对应协议的APP
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(intent)
                    }catch (e:Exception){
                    }
                    true
                }
            }
        }
        webView.loadUrl(extraData!!)
    }
}