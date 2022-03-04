package com.weizu.myapplication.custom

import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.Transformation

class CustomViewAnimation:Animation() {

    var mCW: Float? = null
    var mCH: Float? = null

    override fun initialize(width: Int, height: Int, parentWidth: Int, parentHeight: Int) {
        // 设置默认时长
        duration = 5000
        // 保持动画的结束状态
        fillAfter = true
        // 设置插值器，改变动画播放速度
        interpolator = AccelerateDecelerateInterpolator()
        // 找到中心坐标
        mCW = width * 1.0f / 2;
        mCH = height * 1.0f / 2;
        super.initialize(width, height, parentWidth, parentHeight)
    }

    override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
        super.applyTransformation(interpolatedTime, t)
        t?.matrix?.preScale(1f, 1 - interpolatedTime, mCW!!, mCH!!)
    }
}