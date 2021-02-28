package com.jerry.wuwen.ui.video

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jerry.wuwen.R


import com.jerry.wuwen.logic.model.Video
import com.jerry.wuwen.resource.MyImageView



class VideoAdapter(private val videoList:List<Video>,val context: Context) :RecyclerView.Adapter<VideoAdapter.ViewHolder>(){
    inner class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        val itemimg:MyImageView=view.findViewById(R.id.video_itimg_img)
        val itemtitle:TextView=view.findViewById(R.id.video_ittitle_img)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.video_item,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val video=videoList[position]
        holder.itemView.setOnClickListener {
            val position=holder.adapterPosition
            Log.d("Adapter","${position},${videoList}")
            val video=videoList[position]
            val intent= Intent(context,VideoWebActivity::class.java)
            intent.putExtra("webname",video.url)
            context.startActivity(intent)

        }
        holder.itemimg.setImageURL(video.pictureurl)
        holder.itemtitle.setText(video.title)
    }

    override fun getItemCount(): Int {
        return videoList.size
    }


}