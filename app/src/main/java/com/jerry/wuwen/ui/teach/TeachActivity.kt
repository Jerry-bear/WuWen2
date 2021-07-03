package com.jerry.wuwen.ui.teach

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.jerry.wuwen.R
import com.jerry.wuwen.ui.maininterface.MaininterfaceViewModel
import kotlinx.android.synthetic.main.activity_teach.*
import java.net.URL


class TeachActivity : AppCompatActivity() {
    lateinit var picUrl: URL
    var pngBM: Bitmap? = null
    lateinit var animSet: AnimatorSet
    //获取viewmodel实例
    val viewModel by lazy { ViewModelProvider(this).get(MaininterfaceViewModel::class.java) }

    override fun onStart() {
        super.onStart()
        teach_pict1_materialCardView.visibility=View.VISIBLE
        teach_pict2_materialCardView.visibility=View.VISIBLE
        teach_pict4_materialCardView.visibility=View.VISIBLE
        teach_pict3_materialCardView.visibility=View.VISIBLE
        animSet.start()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //设置对应的xml
        setContentView(R.layout.activity_teach)

        //设置入场动画
        val img1_m = ObjectAnimator.ofFloat(teach_pict1_materialCardView, "translationY", 2000f, 0f)
        val img2_m = ObjectAnimator.ofFloat(teach_pict2_materialCardView, "translationY", 2000f, 0f)
        val img3_m = ObjectAnimator.ofFloat(teach_pict3_materialCardView, "translationY", 2000f, 0f)
        val img4_m = ObjectAnimator.ofFloat(teach_pict4_materialCardView, "translationY", 2000f, 0f)
        teach_pict1_materialCardView.visibility=View.GONE
        teach_pict2_materialCardView.visibility=View.GONE
        teach_pict4_materialCardView.visibility=View.GONE
        teach_pict3_materialCardView.visibility=View.GONE
        animSet = AnimatorSet()
        animSet.play(img1_m).with(img2_m).with(img3_m).with(img4_m)
        animSet.duration = 1000



        //线程消息
        val handler=object: Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    1->{

                    }
                }
            }
        }












        //使状态栏和背景融合
        val decorView=window.decorView//拿到当前的Activity的DecorView
        decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE//表示Activity的布局会显示到状态栏上面
        window.statusBarColor= Color.TRANSPARENT//最后调用一下statusBarColor()方法将状态栏设置为透明色




        Glide.with(this).load("http://shouyu.z12345.com/datafile/cl/pad_cs.jpg").into(imageView3);
        Glide.with(this).load("http://shouyu.z12345.com/datafile/cl/pad_cx.jpg").into(imageView20);
        Glide.with(this).load("http://shouyu.z12345.com/datafile/cl/pad_rj.jpg").into(imageView21);
        Glide.with(this).load("http://shouyu.z12345.com/datafile/cl/pad_sh.jpg").into(imageView22);



        //设置手语教学的web
//        teach_teachweb_webview.settings.javaScriptEnabled=true
//        teach_teachweb_webview.webViewClient= WebViewClient()
//        teach_teachweb_webview.settings.setUseWideViewPort(true);
//        teach_teachweb_webview.settings.setLoadWithOverviewMode(true);
//        teach_teachweb_webview.loadUrl("http://shouyu.z12345.com/tutorial.html")


        teach_pict1_materialCardView.setOnClickListener {
            val intent=Intent(this,Common::class.java)
            startActivity(intent)
        }
        teach_pict2_materialCardView.setOnClickListener {
            val intent=Intent(this,Out::class.java)
            startActivity(intent)
        }
        teach_pict3_materialCardView.setOnClickListener {
            val intent=Intent(this,Talk::class.java)
            startActivity(intent)
        }
        teach_pict4_materialCardView.setOnClickListener {
            val intent=Intent(this,Social::class.java)
            startActivity(intent)
        }










    }



}