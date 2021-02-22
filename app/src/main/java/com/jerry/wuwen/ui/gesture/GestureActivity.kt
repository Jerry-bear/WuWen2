package com.jerry.wuwen.ui.gesture

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.jerry.wuwen.R

class GestureActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //使状态栏和背景融合
        val decorView=window.decorView//拿到当前的Activity的DecorView
        decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE//表示Activity的布局会显示到状态栏上面
        window.statusBarColor= Color.TRANSPARENT//最后调用一下statusBarColor()方法将状态栏设置为透明色
        setContentView(R.layout.activity_gesture)
    }
}