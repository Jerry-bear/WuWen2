package com.jerry.wuwen.ui.video

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.jerry.wuwen.R
import com.jerry.wuwen.WuWen2Application
import com.jerry.wuwen.logic.model.Video
import com.jerry.wuwen.logic.model.VideoRequest
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import com.youth.banner.listener.OnBannerListener
import com.youth.banner.loader.ImageLoader
import kotlinx.android.synthetic.main.video_fragment_fragment.*

class VideoFragment : Fragment(),OnBannerListener {
    lateinit var requestbody: VideoRequest
    //banner的两个变量
    lateinit var list_path:ArrayList<String>
    lateinit var list_title:ArrayList<String>

    private val videoList=ArrayList<Video>()
    companion object {
        fun newInstance() = VideoFragment()
    }

    private lateinit var viewModel: VideoFragmentViewModel
    lateinit var  layoutManager: StaggeredGridLayoutManager
    lateinit var adapter: VideoAdapter
    override fun onStart() {
        requestbody= VideoRequest("video","主页","totalrank",1)
        Log.d("onStart","方法调用2")
        viewModel.askVideo(requestbody)
        super.onStart()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.video_fragment_fragment, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(VideoFragmentViewModel::class.java)
        //videoList.add(Video("你还是从前的那个<em class=\\\"keyword\\\">少年</em>吗？来自元气少女超甜的《<em class=\\\"keyword\\\">少年</em>》","http://i0.hdslb.com/bfs/archive/d11c3ae7a8beb7ef2ebc5b9549dbd4957ee80941.jpg"))


        layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        video_rcv_rcv.layoutManager=layoutManager

        adapter = VideoAdapter(videoList,this.activity!!)
        /*video_rcv_rcv.adapter=adapter*/


        //用于监听responseBodyLiveData的变化
        viewModel.responseBodyLiveData.observe(this, Observer { result->
            val response=result.getOrNull()
            Log.d("VideoActivity","response:${response}")
            if(response!=null){
                if (response.code==0) {

                    WuWen2Application.videoResponse=response
                    videoList.clear()
                    for (i in WuWen2Application.videoResponse.data.result){
                        videoList.add(Video(i.title,"http:"+i.pic,i.arcurl))
                    }
                    layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
                    video_rcv_rcv.layoutManager=layoutManager

                    adapter = VideoAdapter(videoList,this.activity!!)
                    adapter=VideoAdapter(videoList, this.activity!!)
                    video_rcv_rcv.adapter=adapter
                    Log.d("执行adapter适配","")
                    //banner的资源
                    list_path.clear()
                    list_path.add("http:"+WuWen2Application.videoResponse.data.result[1].pic);
                    list_path.add("http:"+WuWen2Application.videoResponse.data.result[2].pic);
                    list_path.add("http:"+WuWen2Application.videoResponse.data.result[3].pic);
                    list_path.add("http:"+WuWen2Application.videoResponse.data.result[4].pic);

                    val myloader=GlideImageLoader()
                    //设置内置样式，共有六种可以点入方法内逐一体验使用。
                    video_bn_bn.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                    //设置图片加载器，图片加载器在下方
                    video_bn_bn.setImageLoader(myloader);
                    //设置图片网址或地址的集合
                    video_bn_bn.setImages(list_path);
                    //设置轮播的动画效果，内含多种特效，可点入方法内查找后内逐一体验
                    video_bn_bn.setBannerAnimation(Transformer.Default);
                    //设置轮播图的标题集合
                    video_bn_bn.setBannerTitles(list_title);
                    //设置轮播间隔时间
                    video_bn_bn.setDelayTime(3000);
                    //设置是否为自动轮播，默认是“是”。
                    video_bn_bn.isAutoPlay(true);
                    //设置指示器的位置，小点点，左中右。
                    video_bn_bn.setIndicatorGravity(BannerConfig.CENTER)
                        //以上内容都可写成链式布局，这是轮播图的监听。比较重要。方法在下面。
                        .setOnBannerListener(this)
                        //必须最后调用的方法，启动轮播图。
                        .start();
                    Log.d("banner的操作",":${list_path}")

                    Toast.makeText(this.activity,response!!.message,Toast.LENGTH_SHORT).show()
                }else{
                    //错误的话就返回后端传回来的错误信息
                    Toast.makeText(this.activity,response!!.message,Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this.activity,"连接失败",Toast.LENGTH_SHORT).show()
            }
            Log.d("tablayout结束","加载结束")

        })



        //banner
        initView()


    }
    //实现适配banner的适配器
    private fun initView() {

        //放图片地址的集合
        //注：在本地也可以，只不过适配器类型为Int，之后在add中直接输入例如R.drawable.dog即可

        list_path = ArrayList();
        //放标题的集合

        list_title = ArrayList();

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
        video_bn_bn.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
        //设置图片加载器，图片加载器在下方
        video_bn_bn.setImageLoader(myloader);
        //设置图片网址或地址的集合
        video_bn_bn.setImages(list_path);
        //设置轮播的动画效果，内含多种特效，可点入方法内查找后内逐一体验
        video_bn_bn.setBannerAnimation(Transformer.Default);
        //设置轮播图的标题集合
        video_bn_bn.setBannerTitles(list_title);
        //设置轮播间隔时间
        video_bn_bn.setDelayTime(3000);
        //设置是否为自动轮播，默认是“是”。
        video_bn_bn.isAutoPlay(true);
        //设置指示器的位置，小点点，左中右。
        video_bn_bn.setIndicatorGravity(BannerConfig.CENTER)
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