package com.jerry.wuwen.ui.video

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.jerry.wuwen.R
import com.jerry.wuwen.WuWen2Application
import com.jerry.wuwen.logic.model.Video
import com.jerry.wuwen.logic.model.VideoRequest
import kotlinx.android.synthetic.main.activity_video.*


class VideoActivity : AppCompatActivity() {

    override fun onStart() {

        /*for (i in WuWen2Application.videoResponse.data.result){
            videoList.add(Video(i.title,i.arcurl))
        }*/
        super.onStart()
    }

    private val tabs = arrayOf("主页", "游戏", "科技")
    private val tabFragmentList: MutableList<Fragment> = ArrayList()
    //获取viewModel
    val viewModel by lazy { ViewModelProvider(this).get(VideoViewModel::class.java) }
    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //使状态栏和背景融合
        val decorView=window.decorView//拿到当前的Activity的DecorView
        decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE//表示Activity的布局会显示到状态栏上面
        window.statusBarColor= Color.TRANSPARENT//最后调用一下statusBarColor()方法将状态栏设置为透明色
        setContentView(R.layout.activity_video)









        //tablayout
        val tabLayout: TabLayout = findViewById(R.id.video_tlo_tlo)
        val viewPager: ViewPager = findViewById(R.id.video_vp_vp)
        //添加tab
        for (i in tabs.indices) {
            tabLayout.addTab(tabLayout.newTab().setText(tabs[i]))

        }
        tabFragmentList.add(VideoFragment.newInstance())
        tabFragmentList.add(VideoFragment1.newInstance())
        tabFragmentList.add(VideoFragment2.newInstance())
        //tabFragmentList.add(VideoFragment.newInstance())

        viewPager.adapter = object : FragmentPagerAdapter(
            supportFragmentManager,
            BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        ) {
            override fun getItem(position: Int): Fragment {
                return tabFragmentList[position]
            }

            override fun getCount(): Int {
                return tabFragmentList.size
            }

            @Nullable
            override fun getPageTitle(position: Int): CharSequence? {
                return tabs[position]
            }
        }

        //设置TabLayout和ViewPager联动
        tabLayout.setupWithViewPager(viewPager, false)
        video_tlo_tlo.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val fragmentManager: android.app.FragmentManager? = getFragmentManager()
                val fragmentTransaction: android.app.FragmentTransaction? = fragmentManager!!.beginTransaction()

                val bundle = Bundle()
                bundle.putString("requestbody",tab.text.toString())
                Toast.makeText(this@VideoActivity, "选中的" + tab.text, Toast.LENGTH_SHORT).show()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                /*val requestbody= VideoRequest("video","主页","totalrank",1)
                viewModel.askVideo(requestbody)*/
                Toast.makeText(this@VideoActivity, "未选中的" + tab.text, Toast.LENGTH_SHORT).show()
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                Toast.makeText(this@VideoActivity, "复选的" + tab.text, Toast.LENGTH_SHORT).show()
            }
        })







    }






}


