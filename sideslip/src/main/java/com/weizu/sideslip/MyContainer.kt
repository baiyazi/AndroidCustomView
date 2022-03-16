package com.weizu.sideslip

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.Scroller
import kotlin.math.abs

class MyContainer(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    var itemWidth = 0
    var scroller: Scroller = Scroller(context)

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, left, top, right, bottom)
        val childView = getChildAt(1)
        itemWidth = childView.width
        childView.layout(width, 0, width + itemWidth, childView.height)
    }

    // 放行点击事件，因为Item的点击事件是其子View需要响应
    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        var intercept = false
        when(event?.action){
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                downX = event.x
                downY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                val xAxis = (scrollX - ( event.x - startX)).toInt()
                if(xAxis > 8) {
                    intercept = true  // 横向滑动需要拦截
                }
            }
        }
        return intercept
    }


    var startX = 0f
    var downX = 0f
    var downY = 0f
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        super.onTouchEvent(event)
        when(event?.action){
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                downX = event.x
                downY = event.y
                mListener?.onDown(this)
            }
            MotionEvent.ACTION_MOVE -> {
                var xAxis = (scrollX - ( event.x - startX)).toInt()
                if(xAxis > itemWidth) {
                    xAxis = itemWidth
                } else if(xAxis < 0) {
                    xAxis = 0
                }
                scrollTo(xAxis, 0)
                startX = event.x

                // 因为外层ListView可以竖直滑动，而这里的Item可以横向滑动，所以这里也要处理一下事件
                val distanceX = abs(event.x - downX)
                val distanceY = abs(event.y - downY)
                if(distanceX > distanceY && distanceX > 8){
                    parent.requestDisallowInterceptTouchEvent(true)
                }
            }
            MotionEvent.ACTION_UP -> {
                // 抬起手指就根据位置动画
                if (scrollX > itemWidth / 2) {
                    // 开启菜单
                    openMenu()
                } else {
                    closeMenu()
                }
            }
        }
        return true
    }

    fun openMenu(){
        // 目标 - scrollX
        val dx = itemWidth - scrollX
        scroller.startScroll(scrollX, scrollY, dx, scrollY)
        invalidate()
        mListener?.onOpen(this)
    }

    fun closeMenu(){
        // 目标 - scrollX
        val dx = 0 - scrollX
        scroller.startScroll(scrollX, scrollY, dx, scrollY)
        invalidate()
        mListener?.onClose(this)
    }

    override fun computeScroll() {
        super.computeScroll()
        if(scroller.computeScrollOffset()){
            scrollTo(scroller.currX, scroller.currY)
            invalidate()
        }
    }

    interface OnItemMenuStateChangeListener{
        fun onClose(view: MyContainer)
        fun onDown(view: MyContainer)
        fun onOpen(view: MyContainer)
    }

    private var mListener: OnItemMenuStateChangeListener? = null

    fun setOnItemChangeListener(l: OnItemMenuStateChangeListener){
        mListener = l
    }
}