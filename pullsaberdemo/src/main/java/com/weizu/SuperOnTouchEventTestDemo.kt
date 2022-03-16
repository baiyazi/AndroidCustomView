package com.weizu

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

class SuperOnTouchEventTestDemo: View {
    constructor(context: Context?) : super(context) {
        init()
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    private fun init(){
        mPaint.color = Color.RED
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = 5f
        mPaint.isAntiAlias = true
    }

    // 记录手指在屏幕上的位置
    private var mPreX = 0f
    private var mPreY = 0f
    private var mPath = Path()
    private var mPaint = Paint()

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        var flag = super.onTouchEvent(event)
        Log.e("TAG", "flag: ${flag}", )
        when(event?.action){
            MotionEvent.ACTION_DOWN -> {
                mPreX = event.x
                mPreY = event.y
                mPath.moveTo(mPreX, mPreY)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val currentX = event.x
                val currentY = event.y
                // 计算(mPreX, mPreY)和(currentX, currentY)的中点坐标
                val endX = (mPreX + currentX) / 2
                val endY = (mPreY + currentY) / 2
                // 将中点坐标作为绘图的控制点
                mPath.quadTo(mPreX, mPreY, endX, endY)
                mPreX = currentX
                mPreY = currentY
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                mPath.quadTo(mPreX, mPreY, event.x, event.y)
            }
        }
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {
            drawPath(mPath, mPaint)
        }
    }
}