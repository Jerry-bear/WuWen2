package com.jerry.wuwen.ui.maininterface

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.*
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.jerry.wuwen.R
import com.jerry.wuwen.ui.gesture.GestureActivity
import com.jerry.wuwen.ui.login.LoginViewModel

import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import com.youth.banner.listener.OnBannerListener
import com.youth.banner.loader.ImageLoader
import kotlinx.android.synthetic.main.activity_maininterface.*
import kotlinx.coroutines.NonCancellable.start
import kotlin.concurrent.thread


class MaininterfaceActivity : AppCompatActivity(),OnBannerListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //使状态栏和背景融合
        val decorView=window.decorView//拿到当前的Activity的DecorView
        decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE//表示Activity的布局会显示到状态栏上面
        window.statusBarColor= Color.TRANSPARENT//最后调用一下statusBarColor()方法将状态栏设置为透明色
        setContentView(R.layout.activity_maininterface)



        //获取viewmodel实例
        val viewModel by lazy { ViewModelProvider(this).get(MaininterfaceViewModel::class.java) }




        //使用banner实现轮播效果
        initView()




        //实现vip图标摇一摇功能
        //开启一个线程计算,发送消息刀主线程更新ui
        val animator = ObjectAnimator.ofFloat(this.mainface_vie_img, "rotation", 0f, 30f,-30f,20f,-20f,10f,-10f,0f)
            val handler=object:Handler(Looper.getMainLooper()){
                override fun handleMessage(msg: Message) {
                    when(msg.what){
                        1->animator.start()
                    }
               }
            }
        animator.setDuration(3000);
        thread{
            while (true){
                Log.d("循环","有一次循环")

                val msg=Message()
                msg.what=1
                handler.sendMessage((msg))
                SystemClock.sleep(7000);
            }

        }

        //设置点击手势识别图标动作
        mainface_gusture_cardview.setOnClickListener {
            val intent=Intent(this,GestureActivity::class.java)
            startActivity(intent)
        }
    }





    //实现适配banner的适配器
    private fun initView() {

        //放图片地址的集合
        //注：在本地也可以，只不过适配器类型为Int，之后在add中直接输入例如R.drawable.dog即可
        val list_path:ArrayList<String> = ArrayList();
        //放标题的集合
        val list_title:ArrayList<String> =ArrayList();

        list_path.add("https://alifei01.cfp.cn/creative/vcg/veer/800water/veer-377601260.jpg");
        list_path.add("https://t7.baidu.com/it/u=2367247276,2417697747&fm=193&f=GIF");
        list_path.add("https://t7.baidu.com/it/u=3880988462,4124731010&fm=193&f=GIF");
        list_path.add("https://t7.baidu.com/it/u=1299255302,2454138010&fm=193&f=GIF");
        list_title.add("好好学习");
        list_title.add("天天向上");
        list_title.add("热爱劳动");
        list_title.add("不搞对象");
        val myloader=GlideImageLoader()
        //设置内置样式，共有六种可以点入方法内逐一体验使用。
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
        //设置图片加载器，图片加载器在下方
        banner.setImageLoader(myloader);
        //设置图片网址或地址的集合
        banner.setImages(list_path);
        //设置轮播的动画效果，内含多种特效，可点入方法内查找后内逐一体验
        banner.setBannerAnimation(Transformer.Default);
        //设置轮播图的标题集合
        banner.setBannerTitles(list_title);
        //设置轮播间隔时间
        banner.setDelayTime(3000);
        //设置是否为自动轮播，默认是“是”。
        banner.isAutoPlay(true);
        //设置指示器的位置，小点点，左中右。
        banner.setIndicatorGravity(BannerConfig.CENTER)
            //以上内容都可写成链式布局，这是轮播图的监听。比较重要。方法在下面。
            .setOnBannerListener(this)
            //必须最后调用的方法，启动轮播图。
            .start();

    }
    //图片轮播加载
    class GlideImageLoader : ImageLoader() {
        // path随便传，我这里最终传的是个对象，拿到图片Url
        override fun displayImage(context: Context?, path: Any, imageView: ImageView?) {

            //Glide 加载图片，Fresco也好、加载本地图片也好，这个类功能就是加载图片
            Glide.with(context).load(path).into(imageView);
        }
    }
    override fun OnBannerClick(position: Int) {
        Log.d("tag", "你点了第"+position+"张轮播图");
    }

}


/****************************************************下方为其他类******************************************************************************/
//定义一个在子线程中更新vip动画的AsyncTask()的类
class Animationstop(val imageView: ImageView?,var number:Int):
    AsyncTask<Unit, Unit, Unit>(){

    override fun onProgressUpdate(vararg values: Unit?) {

    }

    override fun onPostExecute(result: Unit?) {
        Log.d("onpostexecute",number.toString())

        if (number==1){
            val animator = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 30f,0f,-30f,0f,10f,0f,-10f,0f)
            animator.setDuration(3000);
            animator.start();
        }
    }

    override fun doInBackground(vararg params: Unit?) {
        while (true) {
            number=0
            SystemClock.sleep(5000);
            number=1
        }
    }

    override fun onPreExecute() {
        //实现vip图标摇一摇功能
        val animator = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 30f,0f,-30f,0f,10f,0f,-10f,0f)
        animator.setDuration(3000);
        animator.start();
    }
}
fun viprun(imageView: ImageView?){
    val animator = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 30f,0f,-30f,0f,10f,0f,-10f,0f)
    animator.setDuration(3000);
    animator.start();
}