package com.jerry.wuwen.ui.login

import android.animation.ObjectAnimator
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.os.AsyncTask
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModelProvider
import com.jerry.wuwen.R
import com.jerry.wuwen.WuWen2Application
import com.jerry.wuwen.logic.Repository
import com.jerry.wuwen.logic.model.LoginRequest
import com.jerry.wuwen.logic.model.LoginResponse
import com.jerry.wuwen.ui.maininterface.MaininterfaceActivity
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

        val viewModel by lazy { ViewModelProvider(this).get(LoginViewModel::class.java) }

        //开场准备阶段的动画
        //设置开场的界面动画
        val rocketImage = findViewById<ImageView>(R.id.openinganimation).apply {
            setBackgroundResource(R.drawable.rocket_thrust2)
            rocketAnimation = background as AnimationDrawable
        }
        //设置登录按钮的渐变颜色动画
        val rocketButton = findViewById<Button>(R.id.login_login_btn).apply {
            setBackgroundResource(R.drawable.rocket_thrust_btn)
            rocketAnimation_button = background as AnimationDrawable
        }
        //使下方定义的更新主线程(开场准备阶段的动画)开始进行
        Animationstop(
            rocketImage,
            login_later,
            login_materialCardview,
            login_login_btn
        ).execute()


        //设置点击等事件
        //设置改变用户名输入框的内容后password框文字清空
        login_iptusn_edt.addTextChangedListener{editable->
                login_iptpsw_edt.setText(null)
        }
        //点击登录按钮
        login_login_btn.setOnClickListener {

            val username_content=login_iptusn_edt.text.toString()
            val password_content=login_iptpsw_edt.text.toString()
            if (username_content.isNotEmpty()&&password_content.isNotEmpty()){
                val requestbody=LoginRequest(login_iptusn_edt.text.toString(),login_iptpsw_edt.text.toString())
                viewModel.askLogin(requestbody)
                login_load_pgb.visibility=View.VISIBLE
            }else{
                Toast.makeText(this,"未输入用户名或密码",Toast.LENGTH_SHORT).show()

            }
        }
        //用于监听responseBodyLiveData的变化
        viewModel.responseBodyLiveData.observe(this, Observer { result->
            val response=result.getOrNull()
            if(response!=null){
                if (response?.erros==0) {
                    WuWen2Application.EMAIL = response.data.email
                    WuWen2Application.USERNAME = response.data.username
                    WuWen2Application.NAME = response.data.name
                    WuWen2Application.TELE = response.data.tele
                    //如果登陆成功就开启主界面
                    val intent = Intent(this, MaininterfaceActivity::class.java)
                    startActivity(intent)
                    finish()//结束登录界面
                }else{
                    //错误的话就返回后端传回来的错误信息
                    Toast.makeText(this,response!!.data.msg,Toast.LENGTH_SHORT).show()
                    showExitDialog01(response!!.data.msg)
                }
            }else{
                Toast.makeText(this,"无网络连接",Toast.LENGTH_SHORT).show()
            }

            Log.d("结束","加载圈圈消失")
            login_load_pgb.visibility=View.GONE
        })
    }

    override fun onStart() {
        super.onStart()
        rocketAnimation.start()
        rocketAnimation_button.start()
    }
    private fun showExitDialog01(message:String) {
        AlertDialog.Builder(this)
            .setTitle("登陆失败")
            .setMessage(message)
            .setPositiveButton("确定", null)
            .show()
    }
}
//定义一个在子线程中更新开场动画的AsyncTask()的类
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