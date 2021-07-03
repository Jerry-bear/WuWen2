package com.jerry.wuwen.ui.search

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jerry.wuwen.R
import com.jerry.wuwen.ui.maininterface.MaininterfaceViewModel
import kotlinx.android.synthetic.main.activity_search.*
import okhttp3.*
import org.jsoup.Jsoup
import java.io.IOException
import java.lang.Exception
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread



private val pictureList=ArrayList<Picture>()


class SearchActivity : AppCompatActivity() {














    //获取viewmodel实例
    val viewModel by lazy { ViewModelProvider(this).get(MaininterfaceViewModel::class.java) }
    lateinit var adapter: PictureAdapter
    lateinit var handler: Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        //解析html文件
        fun demo1(htmltest: String) {
            val doc = Jsoup.parse(htmltest)
            pictureList.clear()
            try {
                val rows = doc.select("img")
                val title=doc.select("tr")


                for (i in rows.indices) {
                    var titlenew=title[1+i*2].select("td")[0].select("strong").toString()
                    var content=title[1+i*2].select("td")[1].toString()
                    titlenew=titlenew.replace("<strong>","")
                    titlenew=titlenew.replace("</strong>","")
                    content=content.replace("<td bgcolor=\"#FFFFFF\">","")
                    content=content.replace("</td>","")
                    try {
                        val pictureuri=title[1+i*2].select("td")[2].select("img").attr("src")
                        //Log.d("得到的title",titlenew.toString())
                        pictureList.add(Picture(titlenew,"https:"+pictureuri,content))
                    }catch (e: java.lang.Exception){
                        //Log.d("得到的：","没有内容")
                        e.printStackTrace()
                    }

                }

                //Log.d("得到的：",rows.toString())

                val msg=Message()
                msg.what=1
                handler.sendMessage(msg)
            }catch (e: java.lang.Exception){
                //Log.d("得到的：","没有内容")
                val msg=Message()
                msg.what=2
                handler.sendMessage(msg)
                e.printStackTrace()
            }

    }






        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)


        //线程消息

        handler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    1 -> {
                        textView27.visibility=View.VISIBLE
                        //配置适配器
                        val layoutManager=LinearLayoutManager(this@SearchActivity)
                        recyclerView.layoutManager=layoutManager
                        val adapter=PictureAdapter(pictureList,this@SearchActivity)
                        recyclerView.adapter=adapter
                    }
                    2->{
                        textView27.visibility=View.GONE
                    }
                }
            }
        }


        //使状态栏和背景融合
        val decorView=window.decorView//拿到当前的Activity的DecorView
        decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE//表示Activity的布局会显示到状态栏上面
        window.statusBarColor= Color.TRANSPARENT//最后调用一下statusBarColor()方法将状态栏设置为透明色

        //search_back_layout.setBackgroundResource(R.drawable.gesture)


        //发现编辑框是否由内容变化
//        search_input_edt.addTextChangedListener{
        imageView7.setOnClickListener {

//            if(search_input_edt.text==null){
//                recyclerView.setBackgroundResource(R.drawable.chaxunshibai)
//            }else{
//                recyclerView.setBackgroundColor(Color.parseColor("#F8F8F8"))
//            }

            try {
                //发送请求
                thread {
                    val okHttpClient = OkHttpClient.Builder()
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .writeTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(20, TimeUnit.SECONDS)
                        .build()

                    //post方式提交的数据

                    //post方式提交的数据
                    val formBody = FormBody.Builder()
                        .add("sou_suo_q", search_input_edt.text.toString())
                        .build()

                    val request: Request = Request.Builder()
                        .url("https://shouyu.bmcx.com/web_system/bmcx_com_www/system/file/shouyu/s/?ajaxtimestamp=1620957331624") //请求的url
                        .post(formBody)
                        .build()


                    //创建/Call


                    //创建/Call
                    val call: Call = okHttpClient.newCall(request)
                    //加入队列 异步操作
                    //加入队列 异步操作
                    call.enqueue(object : Callback {
                        //请求错误回调方法
                        override fun onFailure(call: Call?, e: IOException?) {
                            println("连接失败")
                        }

                        @Throws(IOException::class)
                        override fun onResponse(call: Call?, response: Response) {
                            if (response.code() == 200) {
                                val response2= response.body()?.string()

                                if (response2!=null){
                                    //Log.d("接收到的：",response.body()?.string().toString())
                                    demo1(response2)
                                }
                                //System.out.println(response.body()?.string())
                            }
                        }
                    })
                }
            }catch (e:Exception){

            }


        }







        //initpicture()
        //配置适配器
        val layoutManager=LinearLayoutManager(this)
        recyclerView.layoutManager=layoutManager
        val adapter=PictureAdapter(pictureList,this)
        recyclerView.adapter=adapter







    }
    private fun initpicture(){

        pictureList.add(Picture("你好","http://shouyu.z12345.com/datafile/cl/pad_cs.jpg","这是我们的家"))
        pictureList.add(Picture("你好","http://shouyu.z12345.com/datafile/cl/pad_cs.jpg","这是我们的家"))
        pictureList.add(Picture("你好","http://shouyu.z12345.com/datafile/cl/pad_cs.jpg","这是我们的家"))
        pictureList.add(Picture("你好","http://shouyu.z12345.com/datafile/cl/pad_cs.jpg","这是我们的家"))
        pictureList.add(Picture("你好","http://shouyu.z12345.com/datafile/cl/pad_cs.jpg","这是我们的家"))
        pictureList.add(Picture("你好","http://shouyu.z12345.com/datafile/cl/pad_cs.jpg","这是我们的家"))
        pictureList.add(Picture("你好","http://shouyu.z12345.com/datafile/cl/pad_cs.jpg","这是我们的家"))
        pictureList.add(Picture("你好","http://shouyu.z12345.com/datafile/cl/pad_cs.jpg","这是我们的家"))
        pictureList.add(Picture("你好","http://shouyu.z12345.com/datafile/cl/pad_cs.jpg","这是我们的家"))
        pictureList.add(Picture("你好","http://shouyu.z12345.com/datafile/cl/pad_cs.jpg","这是我们的家"))
        pictureList.add(Picture("你好","http://shouyu.z12345.com/datafile/cl/pad_cs.jpg","这是我们的家"))
        pictureList.add(Picture("你好","http://shouyu.z12345.com/datafile/cl/pad_cs.jpg","这是我们的家"))
        pictureList.add(Picture("你好","http://shouyu.z12345.com/datafile/cl/pad_cs.jpg","这是我们的家"))
        pictureList.add(Picture("你好","http://shouyu.z12345.com/datafile/cl/pad_cs.jpg","这是我们的家"))
        pictureList.add(Picture("你好","http://shouyu.z12345.com/datafile/cl/pad_cs.jpg","这是我们的家"))
        pictureList.add(Picture("你好","http://shouyu.z12345.com/datafile/cl/pad_cs.jpg","这是我们的家"))
    }
}
//适配器传入的类
class Picture(val title:String,val uri:String,val text:String)
//适配器
class PictureAdapter(val pictureList:List<Picture>,val activity: Activity):
        RecyclerView.Adapter<PictureAdapter.ViewHolder>(){
    inner class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        val pictureImage:ImageView=view.findViewById(R.id.search_adapterpicture_picture)
        val picturetitle: TextView =view.findViewById(R.id.search_adaptertitle_text)
        val picturecontent:TextView=view.findViewById(R.id.search_adaptercontent_text)
        val layout:ConstraintLayout=view.findViewById(R.id.linearLayout8)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context)
            .inflate(R.layout.search_adapter_layout,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val picture=pictureList[position]
        Glide.with(activity).load(picture.uri).into(holder.pictureImage)
        holder.picturecontent.text=picture.text
        holder.picturetitle.text=picture.title
//        if(position%4==0){
//            holder.layout.setBackgroundColor(Color.parseColor("#DEEAFF"))
//        }else if (position%4==1){
//            holder.layout.setBackgroundColor(Color.parseColor("#FDE2D9"))
//        }else if (position%4==2){
//            holder.layout.setBackgroundColor(Color.parseColor("#C4F2FF"))
//        }else if (position%4==3){
//            holder.layout.setBackgroundColor(Color.parseColor("#CDFFEE"))
//        }

    }

    override fun getItemCount()=pictureList.size
}