package com.weizu.custionviewpager

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Scroller

class MyViewPager2(context: Context?, attrs: AttributeSet?) : ViewGroup(context, attrs) {

    // 手势识别器
    var gestureDetector: GestureDetector? = null
    var mOnPagerChangerListener: OnPagerChangerListener? = null

    init {
        gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onScroll(
                e1: MotionEvent?,
                e2: MotionEvent?,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                // X轴移动
                if(scrollX + distanceX >= 0 && scrollX + distanceX <= width * (childCount - 1)){
                    scrollBy(distanceX.toInt(), 0)
                }
                return true
            }
        })
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        for (i in 0..childCount) {
            val childView = getChildAt(i)
            childView?.layout(i * width, 0, (i + 1) * width, height)
        }
    }

    var startX = 0f
    var index = 0

    var scroller: MyScroller? = null
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        //3.把事件传递给手势识别器
        gestureDetector?.onTouchEvent(event)
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                // 起始位置
                startX = event.x
            }
            MotionEvent.ACTION_MOVE -> {

            }
            MotionEvent.ACTION_UP -> {
                var tempIndex = index
                if((startX - event.x) > width / 2){
                    tempIndex++
                }else if((event.x - startX) > width / 2 ){
                    tempIndex--
                }
                // 非法处理
                if(tempIndex < 0) tempIndex = 0
                if(tempIndex > childCount - 1) tempIndex = childCount - 1
                index = tempIndex
                // 监听接口调用
                mOnPagerChangerListener?.onPageChange(index)
                // 回弹
                scrollToPage(index)
            }
        }
        return true
    }

    /**
     * 按照页面下标进行滚动
     */
    fun scrollToPage(tempIndex: Int){
        scroller = MyScroller(scrollX.toFloat(), (tempIndex * width - scrollX).toFloat(), 500)
        invalidate()
    }

    override fun computeScroll() {
        if (scroller?.isScroll() == true) {
            scrollTo(scroller!!.currentX.toInt(), 0)
            invalidate()
        }
    }

    class MyScroller(var startX: Float, var offset: Float, var time: Int) {
        var startTime = 0L
        var currentX = 0f
        var isFinish = false

        init {
            startTime = System.currentTimeMillis()
        }

        // 平滑移动imageView
        fun isScroll(): Boolean {
            if(isFinish) return false
            val consumeTime = System.currentTimeMillis() - startTime
            if(consumeTime < time){
                val distance = consumeTime * offset / time
                currentX =  startX + distance
            } else{
                isFinish = true
                currentX = startX + offset
            }
            return true
        }
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // 获取子元素个数
        if (childCount == 0) return
        // 如果是wrap_content，也就是AT_MOST模式
        // 如果是match_parents，也就是精确模式
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        // 自适应，也就是取决于最大的子元素的宽和高，这里直接设置为屏幕的宽和高
        if (widthMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels
            )
        }
        
    }

    interface OnPagerChangerListener{
        fun onPageChange(position: Int)
    }



}