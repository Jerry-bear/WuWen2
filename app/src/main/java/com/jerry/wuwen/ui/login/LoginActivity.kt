package com.jerry.wuwen.ui.login

import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.os.*
import android.util.Base64
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jerry.wuwen.R
import com.jerry.wuwen.WuWen2Application
import com.jerry.wuwen.logic.model.LoginRequest
import com.jerry.wuwen.ui.maininterface.MaininterfaceActivity
import com.jerry.wuwen.ui.register.RegisterActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.login_later.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.opencv.android.BaseLoaderCallback
import org.opencv.android.LoaderCallbackInterface
import org.opencv.android.OpenCVLoader
import retrofit2.Retrofit
import kotlin.concurrent.thread
import kotlin.properties.Delegates


private lateinit var rocketAnimation: AnimationDrawable//开场动画的rocketAnimation
private lateinit var rocketAnimation_button: AnimationDrawable//登录按钮的rocketAnimation
//private lateinit var mediaRecorder: MediaRecorder
private var ifclearcaptcha:Int=0
class MainActivity : AppCompatActivity() {
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (ifclearcaptcha==1)
            {
                login_codeface_layout.visibility=View.GONE
                ifclearcaptcha=0
            }
            else{
                finish()
            }
            true
        } else false
    }
    val viewModel by lazy { ViewModelProvider(this).get(LoginViewModel::class.java) }
    override fun onResume() {
        super.onResume()
        //load OpenCV engine and init OpenCV library
        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0, this, mLoaderCallback)
        } else {
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS)
        }
    }


    //OpenCV库加载并初始化成功后的回调函数
    private val mLoaderCallback: BaseLoaderCallback = object : BaseLoaderCallback(this) {
        override fun onManagerConnected(status: Int) {
            // TODO Auto-generated method stub
            when (status) {
                BaseLoaderCallback.SUCCESS -> Log.i("FragmentActivity.TAG", "成功加载")
                else -> {
                    super.onManagerConnected(status)
                    Log.i("FragmentActivity.TAG", "加载失败")
                }
            }
        }
    }
    var code=0
    var message=""
    lateinit var data:JSONObject
    lateinit var captcha_result:JSONObject
    lateinit var id:String
    lateinit var base_64_blob:String
    lateinit var captcha_code2:String
    //解析okhttp返回的json格式
    private fun parseJSONWithJSONObject(jsonData:String){
        try {
            val jsonArray = JSONArray(jsonData)
            for (i in 0 until jsonArray.length()){
                val jsonObject=jsonArray.getJSONObject(i)
                code = jsonObject.getInt("code")
                data =jsonObject.getJSONObject("data")
                captcha_result=data.getJSONObject("captcha_result")
                Log.d("captcha_result的内容：",captcha_result.toString())
                id=captcha_result.getString("id")
                Log.d("captcha_result_id的内容：",id.toString())
                base_64_blob=captcha_result.getString("base_64_blob")
                Log.d("captcha_resultbase_64_blob的内容：",base_64_blob.toString())
                captcha_code2=captcha_result.getString("code")
                Log.d("captcha_resultcaptcha_code2的内容：",captcha_code2.toString())
            }
        }catch (e: java.lang.Exception){
            e.printStackTrace()
        }
    }
    //okhttp接收登录数据
    lateinit var code_data:String
    lateinit var code_msg:String
    lateinit var code_name:String
    lateinit var code_token:String
    var code_status by Delegates.notNull<Int>()
    var code_code by Delegates.notNull<Int>()
    private fun parseJSONWithJSONObject2(jsonData:String){
        try {

            val jsonObject=JSONObject(jsonData)
            code_msg=jsonObject.getString("msg")
            if(code_msg=="登陆成功")
            {
                code_status=jsonObject.getInt("status")
                code_data=jsonObject.getString("data")
                val code_data_object=JSONObject(code_data)
                code_name=code_data_object.getString("Name")
                code_token=code_data_object.getString("Token")

            }else{
                code_code=jsonObject.getInt("code")
                code_data=jsonObject.getString("data")
            }
        }catch (e: java.lang.Exception){
            e.printStackTrace()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //使状态栏和背景融合
        val decorView=window.decorView//拿到当前的Activity的DecorView
        decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE//表示Activity的布局会显示到状态栏上面
        window.statusBarColor= Color.TRANSPARENT//最后调用一下statusBarColor()方法将状态栏设置为透明色
        setContentView(R.layout.activity_main)
        //接收各个线程传来的各个更新ui操作
        val handler=object: Handler(Looper.getMainLooper()){
            override fun handleMessage(msg: Message) {
                when(msg.what){
                    //收到返回数据不为空后设置图片验证码
                    1->{
                        ifclearcaptcha=1
                        login_codeface_layout.visibility=View.VISIBLE
                        login_code_img.setImageBitmap(base64ToBitmap(base_64_blob.replace("data:image/png;base64,","")))
                        Log.d("去掉前最后的base64：",base_64_blob.replace("data:image/png;base64,",""))
                    }
                    //收到返回验证码信息
                    2->{
                        login_load_pgb.visibility=View.GONE
                    }
                    //登录成功跳转至主页页面
                    3->{
                        login_load_pgb.visibility=View.GONE
                        val intent=Intent(this@MainActivity,MaininterfaceActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    //登录密码错误
                    4->{
                        Toast.makeText(this@MainActivity, "输入用户名或密码错误", Toast.LENGTH_LONG).show()
                        login_code_edit.setText("")
                        login_iptusn_edt.isEnabled=true
                        login_iptpsw_edt.isEnabled=true
                        login_login_btn.isEnabled=true
                        login_codeface_layout.visibility=View.GONE
                        login_load_pgb.visibility=View.GONE
                        ifclearcaptcha=0
                    }
                    5->{
                        login_load_pgb.visibility=View.GONE
                        Toast.makeText(this@MainActivity, "无网络连接", Toast.LENGTH_LONG).show()
                    }

                }
            }
        }
        //接收从注册界面传来的数据
        val extrausername=intent.getStringExtra("username")
        val extrapassword=intent.getStringExtra("password")
        login_iptusn_edt.setText(extrausername)
        login_iptpsw_edt.setText(extrapassword)






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



        //注册账号
        login_register_text.setOnClickListener {
            val intent=Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }


        //设置登录点击等事件
        //设置改变用户名输入框的内容后password框文字清空
        login_iptusn_edt.addTextChangedListener{editable->
                login_iptpsw_edt.setText(null)
        }







        //点击验证码确定按钮
        login_codeok_button.setOnClickListener {
            login_load_pgb.visibility=View.VISIBLE
            val requestbody=LoginRequest(login_iptusn_edt.text.toString(),login_iptpsw_edt.text.toString(),id,login_code_edit.text.toString())

            Log.d("request：",requestbody.toString())
            //okhttp发送
            thread{
                //拼接发送的json数据
                val obj = JSONObject()
                try {
                    Log.d("sssssssssssssssssssss：","发送请求")
                    obj.put("name",requestbody.name )
                    Log.d("sssssssssssssssssssss："," obj.put(\"name\",requestbody.name )")
                    obj.put("password", requestbody.password)
                    obj.put("id", requestbody.id)
                    obj.put("value", requestbody.value)
                    Log.d("发送内容",obj.toString() )
                } catch (e: JSONException) {
                    e.printStackTrace()

                    Log.d("sssssssssssssssssssss：","失败请求")
                }
                val type: MediaType? = MediaType.parse("application/json;charset=utf-8")
                val RequestBody2: RequestBody = RequestBody.create(type, "" + obj.toString())
                Log.d("RequestBody2",RequestBody2.toString() )
//                val requestBody = okhttp3.FormBody.Builder()
//                    .add("name",requestbody.name )
//                    .add("password", requestbody.password)
//                    .add("id", requestbody.id)
//                    .add("value", requestbody.value)
//                    .build()
                //okhttp请求
                try {
                    val client = OkHttpClient()
                    val request = Request.Builder() // 指定访问的服务器地址
                        .url("http://121.43.149.80:8090/api/LoginUserPass")
                        .post(RequestBody2)
                        .build()
                    val response: Response = client.newCall(request).execute()
                    val responseData = response.body()?.string()
                    Log.d("登录后接收道德信息：",responseData!!)
                    parseJSONWithJSONObject2(responseData)
                    if (code_msg=="登陆成功")
                    {
                        val msg=Message()
                        msg.what=3
                        handler.sendMessage(msg)
                    } else{
                        if(code_data=="登陆失败"){
                            val msg=Message()
                            msg.what=4
                            handler.sendMessage(msg)
                        }else{
                            //如果失败了就重新获取验证码
                            thread {
                                try {
                                    val client= OkHttpClient()
                                    val request = Request.Builder()
                                        .url("http://121.43.149.80:8090/api/captcha")
                                        .build()
                                    val response=client.newCall(request).execute()
                                    var responseData = response.body()?.string()
                                    val msg=Message()
                                    msg.what=2
                                    handler.sendMessage(msg)
                                    if (responseData!=null){
                                        responseData="["+responseData+"]"
                                        parseJSONWithJSONObject(responseData!!)
                                        Log.d("responseData收到的数据",responseData!!)
                                        val msg2=Message()
                                        msg2.what=1
                                        handler.sendMessage(msg2)
                                    }
                                }catch (e: java.lang.Exception){
                                    e.printStackTrace()
                                }
                            }
                        }

                    }

                } catch (e: java.lang.Exception) {
                    val msg=Message()
                    msg.what=5
                    handler.sendMessage(msg)
                    e.printStackTrace()
                }
            }




            //viewModel.askLogin(requestbody)
        }


        //点击登录按钮
        login_login_btn.setOnClickListener {
            login_iptusn_edt.isEnabled=true
            login_iptpsw_edt.isEnabled=true
            login_login_btn.isEnabled=true
            login_load_pgb.visibility=View.VISIBLE
            val username_content=login_iptusn_edt.text.toString()
            val password_content=login_iptpsw_edt.text.toString()
            if (username_content.isNotEmpty()&&password_content.isNotEmpty()){

                thread {
                        try {
                            val client= OkHttpClient()
                            val request = Request.Builder()
                                .url("http://121.43.149.80:8090/api/captcha")
                                .build()
                            val response=client.newCall(request).execute()
                            var responseData = response.body()?.string()
                            val msg=Message()
                            msg.what=2
                            handler.sendMessage(msg)
                            if (responseData!=null){
                                responseData="["+responseData+"]"
                                parseJSONWithJSONObject(responseData!!)
                                Log.d("responseData收到的数据",responseData!!)
                                val msg2=Message()
                                msg2.what=1
                                handler.sendMessage(msg2)
                            }
                        }catch (e: java.lang.Exception){
                            val msg=Message()
                            msg.what=5
                            handler.sendMessage(msg)
                            e.printStackTrace()
                        }
                }
            }else{
                Toast.makeText(this,"未输入用户名或密码",Toast.LENGTH_SHORT).show()
                login_load_pgb.visibility=View.GONE
            }
        }
        //用于监听responseBodyLiveData的变化
        viewModel.responseBodyLiveData.observe(this, Observer { result->
            val response=result.getOrNull()
            if(response!=null){
                if (response.msg=="登录成功") {
                    WuWen2Application.USERNAME = JSONObject(response.data).getString("Name")
                    WuWen2Application.Token=JSONObject(response.data).getString("Token")
                    Log.d("传回来的username",WuWen2Application.USERNAME)
                    Log.d("传回来的Token",WuWen2Application.Token)
                    //如果登陆成功就开启主界面
                    val intent = Intent(this, MaininterfaceActivity::class.java)
                    startActivity(intent)
                    finish()//结束登录界面
                }else{
                    //错误的话就返回后端传回来的错误信息
                    Toast.makeText(this,response!!.data,Toast.LENGTH_SHORT).show()
                    showExitDialog01(response!!.data)
                }
            }else{
                Toast.makeText(this,"无网络连接",Toast.LENGTH_SHORT).show()
            }
            Log.d("结束","加载圈圈消失")
            login_load_pgb.visibility=View.GONE
            login_iptusn_edt.isEnabled=true
            login_iptpsw_edt.isEnabled=true
            login_login_btn.isEnabled=true
        })
    }

    override fun onStart() {
        super.onStart()
        if (WuWen2Application.ifactionmovie==true){
            WuWen2Application.ifactionmovie=false
            rocketAnimation.start()
        }
        rocketAnimation_button.start()
    }
    private fun showExitDialog01(message:String) {
        AlertDialog.Builder(this)
            .setTitle("登陆失败")
            .setMessage(message)
            .setPositiveButton("确定", null)
            .show()
    }
    //base64转为bitmap

    //base64转为bitmap
    fun base64ToBitmap(base64Data: String?): Bitmap? {
        val bytes =
            Base64.decode(base64Data, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }




    fun stringtoBitmap(string: String?): Bitmap? {
        //将字符串转换成Bitmap类型
        var bitmap: Bitmap? = null
        try {
            val bitmapArray: ByteArray
            bitmapArray = Base64.decode(string, Base64.DEFAULT)
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.size)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return bitmap
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
        if (WuWen2Application.ifactionmovie==true){
            SystemClock.sleep(5000);
        }


    }

    override fun onPreExecute() {
        rocketAnimation.start()
    }

}