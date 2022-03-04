package com.weizu.custionviewpager.test

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

class TestView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    var startX = -1f

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_DOWN -> {
                Log.e("TAG", "TestView ==> ACTION_DOWN")
                // 告诉父控件，自己要处理，不允许拦截
                parent.requestDisallowInterceptTouchEvent(true)
                startX = event.x
                // 由我TestView处理按下事件
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val distance = event.x - startX
                // x为相对父控件的距离
                if (x + distance <= 0) {
                    // 到最左侧不再响应，交给父控件处理
                    parent.requestDisallowInterceptTouchEvent(false)
                    return false
                } else if (x + distance >= ((parent as ViewGroup).width - width)) {
                    parent.requestDisallowInterceptTouchEvent(false)
                    return false
                } else {
                    // 使用属性动画
                    val animator = ObjectAnimator.ofFloat(
                        this,
                        "translationX",
                        x,
                        x + distance
                    )
                    animator.start()
                    return true
                }
            }
            MotionEvent.ACTION_CANCEL -> {
                parent.requestDisallowInterceptTouchEvent(false)
            }
        }
        return false
    }
}