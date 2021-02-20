package com.jerry.wuwen.ui.login

import android.animation.ObjectAnimator
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.os.AsyncTask
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.jerry.wuwen.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.login_later.*


private lateinit var rocketAnimation: AnimationDrawable//开场动画的rocketAnimation
private lateinit var rocketAnimation_button: AnimationDrawable//登录按钮的rocketAnimation
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //使状态栏和背景融合
        val decorView=window.decorView//拿到当前的Activity的DecorView
        decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE//表示Activity的布局会显示到状态栏上面
        window.statusBarColor= Color.TRANSPARENT//最后调用一下statusBarColor()方法将状态栏设置为透明色
        setContentView(R.layout.activity_main)

        //开场准备阶段的动画
        //设置开场的界面动画
        val rocketImage = findViewById<ImageView>(R.id.openinganimation).apply {
            setBackgroundResource(R.drawable.rocket_thrust)
            rocketAnimation = background as AnimationDrawable
        }
        //设置登录按钮的渐变颜色动画
        val rocketButton = findViewById<Button>(R.id.login_btn).apply {
            setBackgroundResource(R.drawable.rocket_thrust_btn)
            rocketAnimation_button = background as AnimationDrawable
        }
        //使下方定义的更新主线程(开场准备阶段的动画)开始进行
        Animationstop(
            rocketImage,
            login_later,
            login_materialCardview,
            login_btn
        ).execute()

        //






    }

    override fun onStart() {
        super.onStart()
        rocketAnimation.start()
        rocketAnimation_button.start()
    }
}
class Animationstop(val imageView: ImageView,val loginlater:View,val login_materialCardview:View,val button: Button):AsyncTask<Unit,Unit,Unit>(){
    override fun onProgressUpdate(vararg values: Unit?) {

    }

    override fun onPostExecute(result: Unit?) {
        rocketAnimation.stop()
        imageView.visibility=View.GONE

        loginlater.visibility=View.VISIBLE
        val animator = ObjectAnimator.ofFloat(login_materialCardview, "translationY", login_materialCardview.translationY, login_materialCardview.translationY-150f, login_materialCardview.translationY-200f)
        animator.duration = 700
        animator.start()
        val animator2 = ObjectAnimator.ofFloat(button, "translationY", button.translationY, button.translationY-220f, button.translationY-350f)
        animator2.duration = 900
        animator2.start()
    }

    override fun doInBackground(vararg params: Unit?) {
        SystemClock.sleep(5000);

    }

    override fun onPreExecute() {
        rocketAnimation.start()
    }
}