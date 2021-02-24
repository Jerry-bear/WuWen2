package com.jerry.wuwen.ui.gesture

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.os.*
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jerry.wuwen.R
import com.jerry.wuwen.WuWen2Application
import com.jerry.wuwen.ui.maininterface.MaininterfaceActivity


import kotlinx.android.synthetic.main.activity_gesture.*
import kotlinx.android.synthetic.main.login_later.*
import kotlin.concurrent.thread
private lateinit var rocketAnimation: AnimationDrawable//开场动画的rocketAnimation

class GestureActivity : AppCompatActivity() {
    val viewModel by lazy { ViewModelProvider(this).get(GestureViewModel::class.java) }
    override fun onPause() {
        viewModel.ifbox_run.value=false
        super.onPause()
    }

    override fun onBackPressed() {
        viewModel.ifbox_run.value=false
        val intent=Intent(this,MaininterfaceActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        box_animSet.play(box_animator1).with(box_animator2)
        box_animSet.setDuration(2000);
        val bg_animator3 = ObjectAnimator.ofFloat(gesture_bgd_img, "translationY",-90f, 0f)
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
        gesture_action_img.setOnClickListener {
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


        //设置点击结束以后的事件
        gesture_action_img2.setOnClickListener {
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
}