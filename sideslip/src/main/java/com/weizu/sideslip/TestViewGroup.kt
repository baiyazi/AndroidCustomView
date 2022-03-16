package com.weizu.sideslip

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup

class TestViewGroup(context: Context?, attrs: AttributeSet?) : ViewGroup(context, attrs) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // 测量所有子View的大小
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        // 获取建议的宽高大小
        var width = MeasureSpec.getSize(widthMeasureSpec)
        var height = MeasureSpec.getSize(heightMeasureSpec)
        // 获取设置的模式
        var widthMode = MeasureSpec.getMode(widthMeasureSpec)
        var heightMode = MeasureSpec.getMode(heightMeasureSpec)

        // 判断子View的个数
        if(childCount == 0) {
            // 如果为0，不需要宽高
            setMeasuredDimension(0, 0)
        } else {
            // 布局默认类似LinearLayout,方向为竖直
            if(widthMode == MeasureSpec.AT_MOST){ // wrap_content
                // 当容器中为wrap_content的时候，容器最大宽度为最大子View的宽度
                width = getChildViewMaxWidth()
            }
            if(heightMode == MeasureSpec.AT_MOST){
                // 类似的，当容器的高度为wrap_content的时候，也需要计算
                height = getAllChildViewHeight()
            }
            setMeasuredDimension(width, height)
        }
    }

    fun getChildViewMaxWidth(): Int{
        var maxWidth = 0
        for (i in 0 until childCount){
            val childView = getChildAt(i)
            if(childView.measuredWidth > maxWidth){
                maxWidth = childView.measuredWidth
            }
        }
        return maxWidth
    }

    fun getAllChildViewHeight(): Int{
        var height = 0
        for (i in 0 until childCount){
            val childView = getChildAt(i)
            height += childView.measuredHeight
        }
        return height
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

    }
}