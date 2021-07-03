package com.jerry.wuwen.ui.gesture


import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.os.*
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.iflytek.cloud.SpeechConstant
import com.iflytek.cloud.SpeechUtility
import com.jerry.wuwen.R
import kotlinx.android.synthetic.main.activity_gesture.*
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import java.util.*
import kotlin.concurrent.thread


private lateinit var rocketAnimation: AnimationDrawable//开场动画的rocketAnimation
private lateinit var mTextToSpeech: TextToSpeech
class GestureActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    val viewModel by lazy { ViewModelProvider(this).get(GestureViewModel::class.java) }
    var code=0
    var message=""
    var code2=0
    var message2=""
    var data2=""
    override fun onPause() {
        viewModel.ifbox_run.value=false
        viewModel.ifok=0
        super.onPause()
    }

    //解析okhttp返回的json格式
    private fun parseJSONWithJSONObject(jsonData:String){
        try {
            val jsonArray = JSONArray(jsonData)
            for (i in 0 until jsonArray.length()){
                val jsonObject=jsonArray.getJSONObject(i)
                code = jsonObject.getInt("code")
                message = jsonObject.getString("message")
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
    //解析okhttp返回的json格式
    private fun parseJSONWithJSONObject2(jsonData:String){
        try {
            val jsonArray = JSONArray(jsonData)
            for (i in 0 until jsonArray.length()){
                val jsonObject=jsonArray.getJSONObject(i)
                code2 = jsonObject.getInt("code")
                data2 = jsonObject.getString("data")
                message2 = jsonObject.getString("message")
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }



    override fun onBackPressed() {
        viewModel.ifbox_run.value=false
        //val intent=Intent(this,MaininterfaceActivity::class.java)
        //startActivity(intent)
        finish()
    }
    override fun onStop() {
        super.onStop()
        // 不管是否正在朗读TTS都被打断
        mTextToSpeech.stop()
        // 关闭，释放资源
        mTextToSpeech.shutdown()
    }

    override fun onDestroy() {
        if (mTextToSpeech != null) {
            mTextToSpeech.stop()
            mTextToSpeech.shutdown()
        }
        super.onDestroy()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SpeechUtility.createUtility(this, SpeechConstant.APPID +"=2825fb63");
        //使状态栏和背景融合
        val decorView=window.decorView//拿到当前的Activity的DecorView
        decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE//表示Activity的布局会显示到状态栏上面
        window.statusBarColor= Color.TRANSPARENT//最后调用一下statusBarColor()方法将状态栏设置为透明色
        setContentView(R.layout.activity_gesture)
        viewModel.ifbox_run.value=true


        //设置返回事件
        mainface_back_btn.setOnClickListener {
            onBackPressed()
        }

        //实现box图标摇一摇功能
        //开启一个线程计算,发送消息刀主线程更新ui
        //注意此处还有点击开始录入以后各个控件的更新动作
        val box_animator1 = ObjectAnimator.ofFloat(this.gesture_box_img, "scaleX", gesture_box_img.scaleX,gesture_box_img.scaleX+0.2f,gesture_box_img.scaleX-0.2f,gesture_box_img.scaleX)
        val box_animator2 = ObjectAnimator.ofFloat(this.gesture_box_img, "scaleY", gesture_box_img.scaleY,gesture_box_img.scaleY+0.2f,gesture_box_img.scaleY-0.2f,gesture_box_img.scaleY)
        val box_animSet = AnimatorSet()
        var linshi=""
        box_animSet.play(box_animator1).with(box_animator2)
        box_animSet.setDuration(2000);
        val bg_animator3 = ObjectAnimator.ofFloat(gesture_bgd_img, "translationY",-90f, 0f)
        mTextToSpeech= TextToSpeech(this@GestureActivity,this@GestureActivity)
        // 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
        mTextToSpeech.setPitch(1.0f);
        // 设置语速
        mTextToSpeech.setSpeechRate(1f);
        val handler=object: Handler(Looper.getMainLooper()){
            override fun handleMessage(msg: Message) {
                when(msg.what){

                    1->{
                        box_animSet.setDuration(2000);
                        box_animSet.start()
                    }

                    2-> {
                        box_animSet.cancel()
                        val board_animator = ObjectAnimator.ofFloat(gesture_board_edt, "alpha", 0f, 1f)
                        board_animator.duration = 500
                        if (gesture_board_edt.visibility==View.GONE) {
                            gesture_bgd_img.translationY = 0f
                            board_animator.start()
                        }else{
                            gesture_bgd_img.translationY=0f
                        }
                            //gesture_bgd_img.top=gesture_board_edt.bottom
                        gesture_board_edt.visibility=View.VISIBLE
                        gesture_board_edt.setText(null)


                        gesture_hand_img.visibility=View.GONE
                        gesture_box_img.visibility=View.GONE
                        gesture_reading_img.visibility=View.VISIBLE
                        val rocketImage = findViewById<ImageView>(R.id.gesture_reading_img).apply {
                            setBackgroundResource(R.drawable.gesture_rocket_thrust3)
                            rocketAnimation = background as AnimationDrawable
                        }
                        rocketAnimation.start()
                        gesture_action_img2.visibility=View.VISIBLE
                        gesture_action_text2.visibility=View.VISIBLE

                    }
                    3-> {
                        viewModel.ifbox_run.value=true
                        gesture_action_img.visibility=View.VISIBLE
                        gesture_action_text.visibility=View.VISIBLE
                    }
                    4->{
                        gesture_board_edt.setText(null)
                        bg_animator3.start()
                        gesture_bgd_img.translationY=0f
                    }
                    5->{
                        if(code==1){
                            if(message!=gesture_board_edt.text.toString()){
                                linshi=message.replace(gesture_board_edt.text.toString(),"")
                                mTextToSpeech.speak(linshi, TextToSpeech.QUEUE_FLUSH, null);
                                gesture_board_edt.setText(message)
                            }


                            //mTextToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH, null);
                            Log.d("线程5","ddddddddddddddddddddd")
                            return;
                        }else{
                            Log.d("线程5","code=1")
                            //Toast.makeText(this,"请求失败",Toast.LENGTH_SHORT).show()
                        }

                    }
                }
            }
        }
        //用于监听判断是否可以加载box动画
        viewModel.ifbox_run.observe(this, Observer { result->
            val ifallow=result
            if (viewModel.ifThreadTsTun==false){
                Log.d("线程","开启线程")

                thread {
                    viewModel.ifThreadTsTun=true
                    while (viewModel.ifbox_run.value==true){

                        Log.d("循环","ifbox_run的值为${viewModel.ifbox_run.value}")
                        val msg= Message()
                        msg.what=1
                        handler.sendMessage((msg))
                        SystemClock.sleep(7000);
                    }
                    viewModel.ifThreadTsTun=false
                    Log.d("线程","销毁线程")
                }

            }

        })




        var top1:Float=0f
        var top2:Float=0f
        var top1_t:Float=0f
        var value:Float=0f
        val iffirsttime=true
        //设置点击开始识别以后事件
        var requestBody=0

        gesture_action_img.setOnClickListener {
            //发送清除消息
            thread {
                try {
                    val client=OkHttpClient()
                    val request = Request.Builder()
                        .url("http://121.43.149.80:8090/api/delemessage")
                        .build()
                    val response=client.newCall(request).execute()
                    var responseData = response.body()?.string()
                    if (responseData!=null){
                        responseData="["+responseData+"]"
                        parseJSONWithJSONObject2(responseData!!)
                        Log.d("vvvvvvvvvvvvvvvv",responseData!!)
                        Log.d("code",code.toString())
                        Log.d("message",message.toString())
//                            val msg= Message()
//                            msg.what=6
//                            handler.sendMessage((msg))

                    }
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
            viewModel.ifok=1

            //发送http请求获取手语信息
            //requestBody= requestBody+1
            //viewModel.askRecognition(requestBody)
            thread {
                while (viewModel.ifok==1){
                    try {
                        val client=OkHttpClient()
                        val request = Request.Builder()
                            .url("http://121.43.149.80:8090/api/getmessage")
                            .build()
                        val response=client.newCall(request).execute()
                        var responseData = response.body()?.string()
                        if (responseData!=null){
                            responseData="["+responseData+"]"
                            parseJSONWithJSONObject(responseData!!)
                            Log.d("vvvvvvvvvvvvvvvv",responseData!!)
                            Log.d("code",code.toString())
                            Log.d("message",message.toString())
                            SystemClock.sleep(1000);
                            Log.d("休眠","有一次休眠")
                            val msg= Message()
                            msg.what=5
                            handler.sendMessage((msg))

                        }
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                }

            }












            viewModel.ifbox_run.value=false
            Log.d("ifbox_run","设置为false")
            box_animSet.cancel()
            gesture_action_img.visibility=View.GONE
            gesture_action_text.visibility=View.GONE
            val bg_animator1 = ObjectAnimator.ofFloat(gesture_bgd_img, "translationY", gesture_bgu_img.translationY, gesture_bgu_img.translationY+90f)
            val hand_animator = ObjectAnimator.ofFloat(gesture_hand_img, "alpha", 1f, 0f)
            val box_animator3 = ObjectAnimator.ofFloat(this.gesture_box_img, "scaleX", gesture_box_img.scaleX,gesture_box_img.scaleX+2.2f)
            val box_animator4 = ObjectAnimator.ofFloat(this.gesture_box_img, "scaleY", gesture_box_img.scaleY,gesture_box_img.scaleY+2.2f)
            // top_c1=gesture_bgd_img.y.toFloat()

            top1=93.0f
            top1_t=gesture_bgd_img.top.toFloat()-gesture_board_edt.top.toFloat()
            val boxObg_animSet = AnimatorSet()
            boxObg_animSet.play(box_animator3).with(box_animator4).with(hand_animator)
            bg_animator1.setDuration(500)
            box_animSet.setDuration(500)
            boxObg_animSet.start()
            if (gesture_board_edt.visibility==View.GONE){
                bg_animator1.start()
                //Log.d(edttop.toString(),"调用第一种方法")
            }else{
               /* val bg_animator2 = ObjectAnimator.ofFloat(gesture_bgd_img, "translationY",gesture_bgd_img.translationY,-value-90f, -value)*/
                val bg_animator2 = ObjectAnimator.ofFloat(gesture_bgd_img, "translationY",gesture_bgd_img.translationY,-(top1_t-top1)-100f)
                Log.d("value:${value.toString()}","value:${value.toString()}")
                Log.d("top2:${top2.toString()}","top1:${top1.toString()}")
                bg_animator2.setDuration(200)
                bg_animator3.setDuration(500)
                bg_animator2.start()
                /*gesture_board_edt.visibility=View.GONE
                gesture_board_edt.visibility=View.VISIBLE*/
                //Log.d(edttop.toString(),"调用第二种方法")
                thread{
                    SystemClock.sleep(300);
                    Log.d("休眠","有一次休眠")
                    val msg= Message()
                    msg.what=4
                    handler.sendMessage((msg))

                }
            }

            thread{
                    SystemClock.sleep(600);
                    Log.d("休眠","有一次休眠")
                    val msg= Message()
                    msg.what=2
                    handler.sendMessage((msg))
                }





        }








        //用于监听responseRecognition的变化
        viewModel.responseRecognition.observe(this, Observer { result->
            val response=result.getOrNull()
            if(response!=null){
                //如果收到消息
                if(response?.code==0){
                    gesture_board_edt.setText(response.message)
                }else{
                    Toast.makeText(this,"请求失败",Toast.LENGTH_SHORT).show()
                }



//                if (response?.erros==0) {
//
//                }else{
//                    //错误的话就返回后端传回来的错误信息
//                    Toast.makeText(this,response!!.data.msg,Toast.LENGTH_SHORT).show()
//                    showExitDialog01(response!!.data.msg)
//                }
            }else{
                Toast.makeText(this,"无网络连接",Toast.LENGTH_SHORT).show()
            }
            Log.d("结束","未收到识别结果")
        })


        //设置点击结束以后的事件
        gesture_action_img2.setOnClickListener {
            mTextToSpeech.speak(gesture_board_edt.text.toString(), TextToSpeech.QUEUE_FLUSH, null);
            //首先让持续发送http请求获取识别结果关掉
            viewModel.ifok = 0
            //发送清除消息
            thread {
                    try {
                        val client=OkHttpClient()
                        val request = Request.Builder()
                            .url("http://121.43.149.80:8090/api/delemessage")
                            .build()
                        val response=client.newCall(request).execute()
                        var responseData = response.body()?.string()
                        if (responseData!=null){
                            responseData="["+responseData+"]"
                            parseJSONWithJSONObject2(responseData!!)
                            Log.d("vvvvvvvvvvvvvvvv",responseData!!)
                            Log.d("code",code.toString())
                            Log.d("message",message.toString())
//                            val msg= Message()
//                            msg.what=6
//                            handler.sendMessage((msg))

                        }
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
            }
            top2=gesture_bgd_img.top.toFloat()-gesture_board_edt.top.toFloat()
            Log.d("top2:${top2.toString()}","top1:${top1.toString()}")
            gesture_action_img2.visibility=View.GONE
            gesture_action_text2.visibility=View.GONE
            gesture_hand_img.visibility=View.VISIBLE
            gesture_box_img.visibility=View.VISIBLE
            rocketAnimation.stop()
            gesture_reading_img.visibility=View.GONE
            val hand_animator = ObjectAnimator.ofFloat(gesture_hand_img, "alpha", 0f, 1f)
            val box_animator3 = ObjectAnimator.ofFloat(this.gesture_box_img, "scaleX", gesture_box_img.scaleX,gesture_box_img.scaleX-2.2f)
            val box_animator4 = ObjectAnimator.ofFloat(this.gesture_box_img, "scaleY", gesture_box_img.scaleY,gesture_box_img.scaleY-2.2f)
            val boxObg_animSet = AnimatorSet()
            boxObg_animSet.play(box_animator3).with(box_animator4).with(hand_animator)
            box_animSet.setDuration(700)
            boxObg_animSet.start()
            thread{
                SystemClock.sleep(800);
                Log.d("休眠","有一次休眠")
                val msg= Message()
                msg.what=3
                handler.sendMessage((msg))

            }
        }
















    }

    override fun onInit(status: Int) {
        Log.d("可以使用","ddddddddddddddddddddd")
        if (status == TextToSpeech.SUCCESS) {
            Log.d("可以使用","ddddddddddddddddddddd")
            /*
                使用的是小米手机进行测试，打开设置，在系统和设备列表项中找到更多设置，
            点击进入更多设置，在点击进入语言和输入法，见语言项列表，点击文字转语音（TTS）输出，
            首选引擎项有三项为Pico TTs，科大讯飞语音引擎3.0，度秘语音引擎3.0。其中Pico TTS不支持
            中文语言状态。其他两项支持中文。选择科大讯飞语音引擎3.0。进行测试。

                如果自己的测试机里面没有可以读取中文的引擎，
            那么不要紧，我在该Module包中放了一个科大讯飞语音引擎3.0.apk，将该引擎进行安装后，进入到
            系统设置中，找到文字转语音（TTS）输出，将引擎修改为科大讯飞语音引擎3.0即可。重新启动测试
            Demo即可体验到文字转中文语言。
             */
            // setLanguage设置语言
            val result = mTextToSpeech.setLanguage(Locale.CHINA)
            // TextToSpeech.LANG_MISSING_DATA：表示语言的数据丢失
            // TextToSpeech.LANG_NOT_SUPPORTED：不支持
            if (result == TextToSpeech.LANG_MISSING_DATA
                || result == TextToSpeech.LANG_NOT_SUPPORTED
            ) {
                Toast.makeText(this, "数据丢失或不支持", Toast.LENGTH_SHORT).show()
            }
        }
    }
}