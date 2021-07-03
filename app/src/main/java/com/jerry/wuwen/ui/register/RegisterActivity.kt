package com.jerry.wuwen.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.*
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jerry.wuwen.R
import com.jerry.wuwen.logic.model.RegisterCodeRequest
import com.jerry.wuwen.logic.model.RegisterRequest
import com.jerry.wuwen.ui.login.MainActivity
import kotlinx.android.synthetic.main.activity_register.*
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import kotlin.concurrent.thread
import kotlin.properties.Delegates


class RegisterActivity : AppCompatActivity() {
    var code by Delegates.notNull<Int>()
    lateinit var data: String
    lateinit var msgresponse: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //使状态栏和背景融合
        val decorView=window.decorView//拿到当前的Activity的DecorView
        decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE//表示Activity的布局会显示到状态栏上面
        window.statusBarColor= Color.TRANSPARENT//最后调用一下statusBarColor()方法将状态栏设置为透明色
        setContentView(R.layout.activity_register)


        val moveIn = ObjectAnimator.ofFloat(register_bgd_lo, "translationY", 800f, 0f)
        val rotate = ObjectAnimator.ofFloat(register_register_btn, "translationY", 1000f, 0f)
        val moveIn2 = ObjectAnimator.ofFloat(register_log_img, "translationY", 400f, 0f)
        val moveIn3 = ObjectAnimator.ofFloat(register_logtext1_text, "translationY", 500f, 0f)
        val moveIn4 = ObjectAnimator.ofFloat(register_logtext2_text, "translationY", 600f, 0f)
        //val fadeInOut = ObjectAnimator.ofFloat(textview, "alpha", 1f, 0f, 1f)
        val animSet = AnimatorSet()
        animSet.play(rotate).with(moveIn).with(moveIn2).with(moveIn3).with(moveIn4)
        animSet.duration = 700
        animSet.start()

        //获取viewmodel实例
        val viewModel by lazy { ViewModelProvider(this).get(RegisterViewModel::class.java) }


        //线程开启返回的消息执行
        val handler=object : Handler(Looper.getMainLooper()){
            override fun handleMessage(msg: Message) {
                when(msg.what){
                    1->{
                        if (viewModel.second!=0){
                            register_code_text.setText("${viewModel.second}秒后重新发送")
                        }else{
                            viewModel.ifcodeable=true
                            if(register_register_btn.isEnabled==true) {
                                register_usn_edt.isEnabled=true
                                register_code_text.isEnabled=true
                                register_code_text.setTextColor(
                                    this@RegisterActivity.resources.getColor(R.color.code_unclick)
                                )
                            }


                            register_code_text.setText("重新获取验证码")
                        }

                    }
                    2->{
                        if (code==0) {
                            val intent=Intent(this@RegisterActivity, MainActivity::class.java)
                            intent.putExtra("username",register_code_edt.text.toString())
                            intent.putExtra("password",register_psw_edt.text.toString())
                            startActivity(intent)
                            finish()
                        }else{
                            //错误的话就返回后端传回来的错误信息
                            register_psw_edt.isEnabled=true
                            register_code_edt.isEnabled=true
                            register_register_btn.isEnabled=true
                            Toast.makeText(this@RegisterActivity,msgresponse,Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }




        //设置点击验证码事件
        register_code_text.setOnClickListener {

            val username_content=register_usn_edt.text.toString()
            val registerCodeRequest=RegisterCodeRequest(username_content)
            if (username_content.isNotEmpty()){
                viewModel.ifcodeable=false
                register_usn_edt.isEnabled=false
                register_code_text.setTextColor(this.resources.getColor(R.color.code_isclick))
                Log.d("检测this",this.toString())
                register_code_text.isEnabled=false
                viewModel.registerCode(registerCodeRequest)
            }else{
                Toast.makeText(this,"未输入用户名", Toast.LENGTH_SHORT).show()
            }
        }
        //用于监听responseBodyLiveData的变化
        viewModel.responseCodeLiveData.observe(this, Observer { result->
            val response=result.getOrNull()
            if(response!=null){
                if (response.msg==true) {
                    viewModel.second=60
                    Toast.makeText(this,"发送验证码成功",Toast.LENGTH_SHORT).show()
                    thread {
                        while (viewModel.second!=0){
                            viewModel.second--
                            SystemClock.sleep(1000)
                            val msg=Message()
                            msg.what=1
                            handler.sendMessage(msg)
                        }
                    }
                }else{
                    //错误的话就返回后端传回来的错误信息
                    viewModel.ifcodeable=true
                    register_usn_edt.isEnabled=true
                    register_code_text.isEnabled=true
                    Toast.makeText(this,"用户名已存在",Toast.LENGTH_SHORT).show()
                    register_code_text.setTextColor(this.resources.getColor(R.color.code_unclick))
                }
            }else{
                viewModel.ifcodeable=true
                register_usn_edt.isEnabled=true
                register_code_text.isEnabled=true
                register_code_text.setTextColor(this.resources.getColor(R.color.code_unclick))
                Toast.makeText(this,"无网络连接",Toast.LENGTH_SHORT).show()
            }
        })




        //设置点击注册
        register_register_btn.setOnClickListener {
            val username_content=register_usn_edt.text.toString()
            val password_content=register_psw_edt.text.toString()
            val code_content=register_code_edt.text.toString()
            if (username_content.isNotEmpty()&&password_content.isNotEmpty()&&code_content.isNotEmpty()){
                register_load_pgb.visibility=View.VISIBLE
                val registerRequest= RegisterRequest(code_content,password_content,username_content)
                register_psw_edt.isEnabled=false
                register_code_edt.isEnabled=false
                register_register_btn.isEnabled=false
                //retrofit验证码解析
                //viewModel.register(registerRequest)



                //发送okhttp
                thread {
                    //拼接发送json数据
                    val obj = JSONObject()
                    try {

                        Log.d("sssssssssssssssssssss：","发送请求")
                        obj.put("username",register_code_edt.text.toString() )
                        obj.put("password",register_psw_edt.text.toString() )
                        obj.put("tele",register_usn_edt.text.toString() )
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Log.d("sssssssssssssssssssss：","失败请求")
                    }
                    val type: MediaType? = MediaType.parse("application/json;charset=utf-8")
                    val RequestBody2: RequestBody = RequestBody.create(type, "" + obj.toString())
                        try {
                            val client= OkHttpClient()
                            val request = Request.Builder()
                                .url("http://121.43.149.80:8090/api/register")
                                .post(RequestBody2)
                                .build()
                            val response=client.newCall(request).execute()
                            var responseData = response.body()?.string()
                            if (responseData!=null){
                                responseData="["+responseData+"]"
                                val jsonData=responseData
                                try {
                                    val jsonArray = JSONArray(jsonData)
                                    for (i in 0 until jsonArray.length()){
                                        val jsonObject=jsonArray.getJSONObject(i)
                                        code = jsonObject.getInt("code")
                                        data = jsonObject.getString("data")
                                        msgresponse = jsonObject.getString("msg")
                                        val threadmsg=Message()
                                        threadmsg.what=2
                                        handler.sendMessage(threadmsg)
                                    }
                                }catch (e: java.lang.Exception){
                                    e.printStackTrace()
                                }
                                Log.d("responseData收到的数据",responseData!!)
                                val msg2=Message()
                                msg2.what=1
                                handler.sendMessage(msg2)
                            }
                        }catch (e: java.lang.Exception){
                            e.printStackTrace()
                        }
                }






            }else {
                if(password_content.isEmpty()){
                    Toast.makeText(this,"未输入密码", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this,"未输入验证码", Toast.LENGTH_SHORT).show()
                }
            }
        }
        //用于监听responseBodyLiveData的变化
        viewModel.responseLiveData.observe(this, Observer { result->
            val response=result.getOrNull()
            if(response!=null){
                if (response.msg=="注册成功") {
                    val intent=Intent(this, MainActivity::class.java)
                    intent.putExtra("username",register_code_edt.text.toString())
                    intent.putExtra("password",register_psw_edt.text.toString())
                    startActivity(intent)
                    finish()
                }else{
                    //错误的话就返回后端传回来的错误信息
                    register_psw_edt.isEnabled=true
                    register_code_edt.isEnabled=true
                    register_register_btn.isEnabled=true
                    Toast.makeText(this,response.msg,Toast.LENGTH_SHORT).show()
                }
            }else{
                register_psw_edt.isEnabled=true
                register_code_edt.isEnabled=true
                register_register_btn.isEnabled=true
                Toast.makeText(this,"无网络连接",Toast.LENGTH_SHORT).show()
            }
            if(viewModel.ifcodeable==true){
                register_usn_edt.isEnabled=true
                register_code_text.isEnabled=true
                register_code_text.setTextColor(this.resources.getColor(R.color.code_unclick))
            }
            register_load_pgb.visibility=View.GONE
        })







    }

    override fun onStart() {
        super.onStart()

    }
}