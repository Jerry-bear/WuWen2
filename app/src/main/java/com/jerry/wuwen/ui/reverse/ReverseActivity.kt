package com.jerry.wuwen.ui.reverse

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.graphics.Color
import android.media.MediaPlayer.OnCompletionListener
import android.net.Uri
import android.os.*
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.iflytek.cloud.RecognizerResult
import com.iflytek.cloud.SpeechConstant
import com.iflytek.cloud.SpeechError
import com.iflytek.cloud.SpeechUtility
import com.iflytek.cloud.ui.RecognizerDialog
import com.iflytek.cloud.ui.RecognizerDialogListener
import com.jerry.wuwen.R
import kotlinx.android.synthetic.main.activity_reverse.*
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import kotlin.concurrent.thread
import kotlin.properties.Delegates


class ReverseActivity : AppCompatActivity() {
    var ifok = 1
    lateinit var handler:Handler
    var videourlarray=ArrayList<String>()
    private fun showExitDialog01() {
        AlertDialog.Builder(this)
            .setTitle("提示")
            .setMessage("1.请打开手机的麦克风等权限。\n" +"2.视频开始为\"开始\"动作。\n"+
                    "3.目前可以识别出的词汇有\"还是\", \"开始\", \"我\", \"一个\",\"在\", \"只要\", \"哭\", \"孩子\",\"天真\"")
            .setPositiveButton("确定", null)
            .show()
    }
    override fun onStart() {
        super.onStart()
        thread {
            while (ifok==1){
                SystemClock.sleep(4000);
                val msg=Message()
                msg.what=1
                handler.sendMessage(msg)
            }

        }


    }
    var code by Delegates.notNull<Int>()
    lateinit var data:String
    lateinit var message:String
    var iall=0
    private fun parseJSONWithJSONObject(jsonData:String){
        try {

            val jsonObject=JSONObject(jsonData)
            code=jsonObject.getInt("code")
            Log.d("视频code",code.toString())
            data=jsonObject.getString("data")
            if(data!=""){
                data=jsonObject.getString("data")
                videourlarray.add(data)
                Log.d("视频data",data)
            }
            iall=iall+1
            if (iall==i){
                val msg=Message()
                msg.what=2
                handler.sendMessage(msg)
                iall=0
            }

        }catch (e: java.lang.Exception){
            e.printStackTrace()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SpeechUtility.createUtility(this, SpeechConstant.APPID +"=2825fb63");
        setContentView(R.layout.activity_reverse)



        //提示信息
        showExitDialog01()










        //使状态栏和背景融合
        val decorView=window.decorView//拿到当前的Activity的DecorView
        decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE//表示Activity的布局会显示到状态栏上面
        window.statusBarColor= Color.TRANSPARENT//最后调用一下statusBarColor()方法将状态栏设置为透明色
        handler=object: Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    1->{
                        val animator = ObjectAnimator.ofFloat(reverse_msghead_text, "rotation", 0f, 30f,-30f,20f, -20f,10f,-10f,0f)
                        animator.duration = 3000
                        animator.start()
                    }
                    2->{

                        reverse_video_layout.visibility=View.VISIBLE
                        val animator = ObjectAnimator.ofFloat(reverse_msghead_text, "alpha", 1f,0f)
                        //animator.duration = 2000
                        val animator2 = ObjectAnimator.ofFloat(reverse_video_videoview, "alpha", 0f,1f)
                        //animator2.duration = 2000
                        val animSet = AnimatorSet()
                        animSet.play(animator).with(animator2)
                        animSet.duration = 1000
                        animSet.start()
                        var uri: Uri?
                        Log.d("videourlarray.size",videourlarray.size.toString())
                        if (videourlarray.size !=0){
                            i=0

                            Log.d("i的大小",i.toString())
                            Log.d("videourlarray.size",videourlarray.size.toString())
                            Log.d("视频链接",videourlarray[0])
                            uri = Uri.parse("http://1304563715.vod2.myqcloud.com/9a8990advodtranscq1304563715/1b72b0b25285890818700276098/v.f100030.mp4")
                            reverse_video_videoview.setVideoURI(uri)
                            reverse_video_videoview.seekTo(1000)
                            reverse_video_videoview.start()
                            if (i<videourlarray.size){
                                reverse_video_videoview.setOnCompletionListener {
                                    Log.d("没有视频播放",videourlarray.size.toString())
                                    if (i<videourlarray.size){
                                        Log.d("ddddddddddddddddddddddd",i.toString())
                                        val uri = Uri.parse(videourlarray[i])
                                        reverse_video_videoview.setVideoURI(uri)
                                        reverse_video_videoview.seekTo(1000)
                                        reverse_video_videoview.start()
                                        i=i+1
                                    }else{

                                    }

                                }
                            }
                        }else{
                            Log.d("无匹配视频","*******")
                        }


//                        animator.start()
//                        animator2.start()
                        thread {
                            SystemClock.sleep(2000)
                            val msg2=Message()
                            msg2.what=3
                            handler.sendMessage(msg2)
                        }

                    }
                    3->{
                        reverse_msghead_text.visibility=View.GONE
                    }
                    4->{

                    }
                }
            }
        }

        //设置开场字的动画属性




        //设置开始梵反义点击事件
        reverse_action_button.setOnClickListener {
                ifok=0
                try {
                    initSpeech()
                }catch (e:Exception){
                    Toast.makeText(this, "请打开麦克风权限", Toast.LENGTH_SHORT).show()
                }



        }


    }
    var i=0
    private fun initSpeech() {

        //1、初始化窗口
        val dialog = RecognizerDialog(this@ReverseActivity, null)
        Log.d("fffffffffffffffffffffffffffffffffffff",dialog.toString())
        //2、设置听写参数，详见官方文档
        //识别中文听写可设置为"en_us",此处为设置英文听写
        dialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn")
        dialog.setParameter(SpeechConstant.ACCENT, "mandarin")
        //获取字体所在的控件，设置为"",隐藏字体，
        //获取字体所在的控件，设置为"",隐藏字体，
        val txt =
            dialog.getWindow()?.getDecorView()?.findViewWithTag<TextView>("textlink")
        txt?.setText("");
        dialog.getWindow()?.getDecorView()?.findViewWithTag<TextView>("textlink")?.text = "xxxxxxxxxx"
        //3、开始听写
        dialog.setListener(object : RecognizerDialogListener {
            override fun onResult(recognizerResult: RecognizerResult, b: Boolean) {
                if (!b) {
                    val result = parseVoice(recognizerResult.getResultString())
                    //reverse_msghead_text.setText(result)
                    textView41.setText(result)
                    //Toast.makeText(MainActivity.this,result,Toast.LENGTH_SHORT).show();

                }
                ifok=1
//                val msg=Message()
//                msg.what=2
//                handler.sendMessage(msg)
            }

            override fun onError(speechError: SpeechError) {}
        })
        dialog.show()

    }

    //解析Gson对象
    fun parseVoice(resultString: String?): String {
        val gson = Gson()
        val voiceBean = gson.fromJson(resultString, Voice::class.java)
        val sb = StringBuffer()
        val ws: ArrayList<Voice.WSBean>? = voiceBean.ws
        videourlarray.clear()
        if (ws != null) {
            i=ws.size
            Log.d("i=ws.size",i.toString())
        }
        for (wsBean in ws!!) {
            val word: String? = wsBean.cw?.get(0)?.w
            if (word != null) {
                //array.add(word)
                Log.d("存入词汇",word)
                Log.d("eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee",word)
                //okhttp请求接收视频
                thread {
                    //拼接发送的json数据
                    val obj = JSONObject()
                    try {
                        obj.put("message", word )
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Log.d("sssssssssssssssssssss：","失败请求")
                    }
                    val type: MediaType? = MediaType.parse("application/json;charset=utf-8")
                    val RequestBody2: RequestBody = RequestBody.create(type, obj.toString())
                    val requestBody=FormBody.Builder()
                        .add("message",word)
                        .build()
                    try {
                        val client= OkHttpClient()
                        val request = Request.Builder()
                            .url("http://121.43.149.80:8091/api/GetVideoUrl")
                            .header("Content-Type","application/json")
                            .post(RequestBody2)
                            .build()
                        val response=client.newCall(request).execute()
                        var responseData = response.body()?.string()
                        Log.d("responseData收到的数据",responseData!!)
                        if (responseData!=""){
                            Log.d("responseData收到的数据",responseData!!)
                            responseData=responseData
                            parseJSONWithJSONObject(responseData!!)


                        }
                    }catch (e: java.lang.Exception){
                        Log.d("sssssssssssssss","获取token失败")
                        e.printStackTrace()
                    }
                }

            }
            sb.append(word)
        }


        return sb.toString()
    }

    /**
     * 语音对象封装
     */
    class Voice {
        var ws: ArrayList<WSBean>? = null

        inner class WSBean {
            var cw: ArrayList<CWBean>? = null
        }

        inner class CWBean {
            var w: String? = null
        }
    }
}