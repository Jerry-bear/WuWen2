package com.jerry.wuwen.ui.video

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jerry.wuwen.R

class VideoFragment1 : Fragment() {

    companion object {
        fun newInstance() = VideoFragment1()
    }

    private lateinit var viewModel: VideoFragment1ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.video_fragment1_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(VideoFragment1ViewModel::class.java)
        // TODO: Use the ViewModel
    }

}