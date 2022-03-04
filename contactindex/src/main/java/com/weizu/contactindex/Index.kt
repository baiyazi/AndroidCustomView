package com.weizu.contactindex

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

class Index: View{
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    private var paint: Paint = Paint()
    var defaultTextSize = dp2px(20)  // 默认字体大小
    var defaultWidth = dp2px(40).toInt()    // 默认宽度
    var itemHeight = defaultTextSize  // 每个词的高度
    var itemWidth = defaultTextSize   // 每个词的宽度
    val defaultPaddingBottom = 10     // 默认底部距离
    var touchIndex = -1   // 手指触摸的下标
    var mListener: OnIndexTouchListener? = null

    init {
        paint.isAntiAlias = true
        paint.color = Color.WHITE
        paint.textSize = defaultTextSize
        paint.setTypeface(Typeface.DEFAULT_BOLD)
        paint.setTextAlign(Paint.Align.CENTER)
    }

    val words = listOf<String>("A", "B", "C", "D", "E", "F", "G", "H", "I",
        "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
        "W", "X", "Y", "Z")

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var width = defaultWidth
        if(MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY){
            width = MeasureSpec.getSize(widthMeasureSpec)
        }
        val height = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(width, height)
        // 计算每一项的高度
        itemHeight = ((height * 1.0f - defaultPaddingBottom) / words.size)
        Log.e("TAG", "itemHeight: ${itemHeight}", )
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        for( i in 0 until words.size){
            val word = words[i]
            if(touchIndex == i){
                paint.color = resources.getColor(R.color.purple_700)
            } else{
                paint.color = Color.WHITE
            }
            // 绘制一个矩形
            var rect = Rect(0, (i * itemHeight).toInt(), itemWidth.toInt(), ((i + 1) * itemHeight).toInt())
            val x = (itemWidth + paint.measureText(word)) / 2
            val y = rect.centerY() + itemHeight / 2;
            canvas?.drawText(word, x, y, paint)
        }

    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        super.onTouchEvent(event)
        when(event?.action){
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                var currentIndex = (event.y / itemHeight).toInt()
                if(currentIndex != touchIndex){
                    touchIndex = currentIndex
                    invalidate()
                    // 设置监听
                    mListener?.onIndexTouch(touchIndex, words[touchIndex])
                }
            }
            MotionEvent.ACTION_UP -> {
                touchIndex = -1
                invalidate()
            }
        }
        return true
    }

    fun dp2px(dp: Int): Float{
        return resources.displayMetrics.density * dp
    }


    fun setOnIndexTouchListener(l: OnIndexTouchListener){
        mListener = l
    }

    interface OnIndexTouchListener{
        fun onIndexTouch(index: Int, word: String)
    }
}