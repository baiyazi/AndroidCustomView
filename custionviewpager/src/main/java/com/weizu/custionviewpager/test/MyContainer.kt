package com.weizu.custionviewpager.test

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.FrameLayout

class MyContainer(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        // Down事件要流到子view，由子view判断是否需要父view拦截
        return ev?.action != MotionEvent.ACTION_DOWN
    }

    // 开始位置
    var startX = -1f

    // 该容器的事件处理
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
            }
            MotionEvent.ACTION_MOVE -> {
                // 从子view按下，滑动到了父控件
                if (startX == -1f) {
                    // 记录进入的位置
                    startX = event.x
                }
                // 按照距离移动容器
                val distance = event.x - startX
                // 使用属性动画
                Log.e("TAG", "父控件动画， ${x}, ${x + distance}")
                val animator = ObjectAnimator.ofFloat(
                    this,
                    "translationX",
                    x,
                    x + distance
                )
                animator.start()
            }
            MotionEvent.ACTION_CANCEL -> {
                // 重置，还原
                startX = -1f
            }
        }
        return true
    }


}