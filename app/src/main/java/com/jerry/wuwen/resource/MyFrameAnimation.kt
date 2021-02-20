package com.jerry.wuwen.resource

import android.graphics.drawable.AnimationDrawable
import android.os.Handler


/**
 * 作    者：MrYan （QQ：416643088）
 * 创建日期：8/6 0006.
 * 带回调的帧动画
 */
class MyFrameAnimation : AnimationDrawable() {
    private val handler: Handler
    private val runnable: Runnable
    private var onFrameAnimationListener: OnFrameAnimationListener? = null
    private var maxDuration = 0

    /**
     * 动画不在运行，触发结束回调
     */
    private fun unRunning() {
        if (onFrameAnimationListener != null) {
            onFrameAnimationListener!!.onEnd() //触发动画停止回调
        }
        handler.removeCallbacks(runnable)
    }

    /**
     * 重写开始方法监听动画是否结束
     */
    override fun start() {
        super.start()
        initHandler()
        if (onFrameAnimationListener != null) {
            onFrameAnimationListener!!.onStart() //触发动画开始回调
        }
    }

    private fun initHandler() {
        handler.postDelayed(runnable, if (maxDuration == 0) getMaxDuration().toLong() else maxDuration.toLong())
    }

    override fun stop() {
        super.stop()
    }

    /**
     * 获取持续时间最长的帧的持续时间
     *
     * @return 时间  如果这一帧大于1秒，则返回 1 秒，否则返回这一帧的持续时间
     */
    private fun getMaxDuration(): Int {
        for (i in 0 until this.numberOfFrames) {
            if (maxDuration < getDuration(i)) {
                maxDuration = getDuration(i)
            }
        }
        return if (maxDuration > 1000) 1000 else maxDuration
    }

    /**
     * 设置动画监听器
     *
     * @param onFrameAnimationListener 监听器
     */
    fun setOnFrameAnimationListener(onFrameAnimationListener: OnFrameAnimationListener?) {
        this.onFrameAnimationListener = onFrameAnimationListener
    }

    /**
     * 动画监听器
     */
    interface OnFrameAnimationListener {
        /**
         * 动画开始
         */
        fun onStart()

        /**
         * 动画结束
         */
        fun onEnd()
    }

    init {
        handler = Handler()
        runnable = Runnable {
            //获取最后一帧，和当前帧做比较，如果相等，就结束动画，调用动画结束回调
            if (getFrame(numberOfFrames - 1) !== current) {
                initHandler() //如果不是最后一帧，重新启动handler
            } else {
                unRunning() //如果是最后一帧，触发结束回调
            }
        }
    }
}