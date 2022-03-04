package com.weizu.switchbutton

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class MyButton(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    lateinit var mQg: Bitmap
    lateinit var mBg: Bitmap
    var paint: Paint
    var mDTime: Int = 0
    var margin: Int = 0
    // 开关状态
    var isOpen = false
    // 前景层距离左边界的值
    var marginLeftValue: Float


    init {
        // 初始化边距值为0
        marginLeftValue = 0f

        // 方法二，TypedArray// 读取xml中配置的属性
        val typedArray = context?.obtainStyledAttributes(attrs, R.styleable.MyButton)
        for (i in 0..typedArray!!.indexCount) {
            val index = typedArray.getIndex(i)
            when (index) {
                R.styleable.MyButton_qgImg -> {
                    mQg = (typedArray.getDrawable(index)!! as BitmapDrawable).bitmap
                }
                R.styleable.MyButton_bgImg -> {
                    mBg = (typedArray.getDrawable(index)!! as BitmapDrawable).bitmap
                }
                R.styleable.MyButton_DTime -> {
                    mDTime = typedArray.getInt(index, mDTime)
                }
            }
        }

        // 计算距离
        margin = mBg.width - mQg.width

        // 初始化画笔
        paint = Paint()
        paint.isAntiAlias = true

        // 设置点击监听
        setOnClickListener(object : OnClickListener{
            override fun onClick(v: View?) {
                if(isOpen){
                    // 设置边距为最大，然后重新绘制
                    marginLeftValue = margin.toFloat()
                } else{
                    // 设置边距为0，然后重新绘制
                    marginLeftValue = 0f
                }
                isOpen = !isOpen
                postInvalidate()
            }
        })

    }

    var currentX = 0f

    // 触摸事件
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        super.onTouchEvent(event)
        when(event?.action){
            MotionEvent.ACTION_DOWN -> {
                // 手指按下，记录起始值
                currentX = event.x
            }
            MotionEvent.ACTION_MOVE -> {
                // 手指移动，计算偏移量
                val endX = event.x
                val offset = endX - currentX
                // 对应逻辑上移动的距离
                marginLeftValue += offset
                // 但是要屏蔽非法值
                if(marginLeftValue < 0){
                    marginLeftValue = 0f
                } else if(marginLeftValue > margin){
                    marginLeftValue = margin.toFloat()
                }
                // 请求重新绘制
                postInvalidate()
                // 更新
                currentX = endX
            }
            MotionEvent.ACTION_UP -> {
                // 手指离开屏幕，判断当前状态
                isOpen = marginLeftValue > margin / 2
                if(isOpen){
                    // 设置边距为最大，然后重新绘制
                    marginLeftValue = margin.toFloat()
                } else{
                    // 设置边距为0，然后重新绘制
                    marginLeftValue = 0f
                }
                postInvalidate()
            }
        }
        return true; // 表示事件已经处理
    }



    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // 设置宽度为背景图片的宽度
        setMeasuredDimension(mBg.width, mBg.height)
    }

    override fun onDraw(canvas: Canvas?) {
        // 绘制前景和背景
        canvas?.drawBitmap(mBg, 0f, 0f, paint)
        canvas?.drawBitmap(mQg, marginLeftValue, 0f, paint)
    }
}