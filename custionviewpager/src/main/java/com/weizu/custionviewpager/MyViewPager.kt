package com.weizu.custionviewpager

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Scroller
import kotlin.math.abs

/**
 * 使用系统自带Scroller
 */
class MyViewPager(context: Context?, attrs: AttributeSet?) : ViewGroup(context, attrs) {

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
    // 使用系统android.widget.Scroller
    var scroller: Scroller = Scroller(context)

    var interceptorX = 0f
    var interceptorY = 0f
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        when (ev?.action) {
            MotionEvent.ACTION_DOWN -> { // 放行Down事件
                startX = ev.x
                // 起始位置
                interceptorX = ev.x
                interceptorY = ev.y
            }
            MotionEvent.ACTION_MOVE -> { // MOVE事件选择放行
                return abs(ev.x - interceptorX) >= abs(ev.y - interceptorY)
            }
        }
        return false
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.e("TAG", "ViewPager调用onTouchEvent， action：${event?.action }")
        //3.把事件传递给手势识别器
        gestureDetector?.onTouchEvent(event)
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> { // 0
                // 起始位置
                startX = event.x
            }
            MotionEvent.ACTION_MOVE -> { // 2

            }
            MotionEvent.ACTION_UP -> { // 1
                var tempIndex = index
                if ((startX - event.x) > width / 2) {
                    tempIndex++
                } else if ((event.x - startX) > width / 2) {
                    tempIndex--
                }
                // 非法处理
                if (tempIndex < 0) tempIndex = 0
                if (tempIndex > childCount - 1) tempIndex = childCount - 1
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
        scroller.startScroll(scrollX, scrollY, tempIndex*width - scrollX, 0)
        invalidate()
    }

    override fun computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.currX, 0)
            invalidate()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // 获取子元素个数
        if (childCount == 0) return
        // 测量孩子
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        // 如果是wrap_content，也就是AT_MOST模式
        // 如果是match_parents，也就是精确模式
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        // 自适应，也就是取决于最大的子元素的宽和高，这里直接设置为屏幕的宽和高
        var height = MeasureSpec.getSize(heightMeasureSpec)
        var width = MeasureSpec.getSize(widthMeasureSpec)
        if (widthMode == MeasureSpec.AT_MOST ){
            width = resources.displayMetrics.widthPixels
        }
        if(heightMode == MeasureSpec.AT_MOST) {
            height = resources.displayMetrics.heightPixels
        }
        setMeasuredDimension(width, height)
    }

    // 定义一个页面下标改变的监听接口
    interface OnPagerChangerListener{
        fun onPageChange(position: Int)
    }
}