package com.weizu

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator

/**
 * 学习波浪效果，其实也就是移动类似于正弦的连续图像，带来的视觉效果
 * @author 梦否
 * 2022年3月15日
 */
class WaterRippleView : View {
    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }



    private lateinit var mPath: Path
    private lateinit var mPaint: Paint
    private lateinit var points1: Array<MyPoint>
    private lateinit var points2: Array<MyPoint>

    class MyPoint(var x: Float, var y: Float)

    /**
     * 初始化方法
     */
    private fun init() {
        mPath = Path()
        mPaint = Paint()
        mPaint.isDither = true
        mPaint.isAntiAlias = true
        mPaint.strokeWidth = 5f
        mPaint.color = Color.GRAY
        mPaint.style = Paint.Style.FILL

        val viewWidth = resources.displayMetrics.widthPixels
        points1 = arrayOf(
            MyPoint(0f * viewWidth, 200f),
            MyPoint(.33f * viewWidth, 20f),
            MyPoint(.66f * viewWidth, 360f),
            MyPoint(1f * viewWidth, 200f)
        )
        points2 = arrayOf(
            MyPoint(-1f * viewWidth, 200f),
            MyPoint(-.66f * viewWidth, 20f),
            MyPoint(-.33f * viewWidth, 360f),
            MyPoint(0f * viewWidth, 200f),
        )

        // 三阶贝塞尔曲线，传入0，也就是初始时刻
        updatePathByDistance(0f)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.apply {
            drawPath(mPath, mPaint)
        }

    }

    /**
     * 根据距离来进行更新在贝赛尔曲线中的点的坐标值
     * @param distance 传入的距离
     */
    private fun updatePathByDistance(distance: Float) {
        // 重置
        mPath.reset()
        // 设置
        mPath.moveTo(points2[0].x, points2[0].y)
        mPath.cubicTo(
            points2[1].x + distance,
            points2[1].y,
            points2[2].x + distance,
            points2[2].y,
            points2[3].x + distance,
            points2[3].y
        )

        mPath.cubicTo(
            points1[1].x + distance,
            points1[1].y,
            points1[2].x + distance,
            points1[2].y,
            points1[3].x + distance,
            points1[3].y
        )

        val y = resources.displayMetrics.heightPixels
        mPath.lineTo(points1[3].x, y.toFloat())
        mPath.lineTo(points2[0].x + distance,  y.toFloat())
        mPath.lineTo(points2[0].x + distance, points2[0].y)
    }

    /**
     * 一直移动绘制的两个类似于正弦函数的路径
     */
    var startedMove = false
    private fun startMove() {
        startedMove = true
        val animator = ValueAnimator.ofFloat(0f, resources.displayMetrics.widthPixels.toFloat())
        animator.duration = 800
        // 线性插值器，使之匀速运动
        animator.interpolator = LinearInterpolator()
        // 循环
        animator.repeatCount = ValueAnimator.INFINITE
        animator.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
            override fun onAnimationUpdate(animation: ValueAnimator?) {
                val value = animator.getAnimatedValue()
                updatePathByDistance(value as Float)
                // 重绘
                invalidate()
            }
        })
        animator.start()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        super.onTouchEvent(event)
        var flag = false
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                flag = true
                if(!startedMove) startMove()
            }
            MotionEvent.ACTION_MOVE,
            MotionEvent.ACTION_UP -> {
                flag = false
            }
        }
        return flag
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val minHeight = dp2px(300)
        val minWidth = dp2px(500)
        val widthSize = getMeasureSize(widthMeasureSpec, minWidth.toInt())
        val heightSize = getMeasureSize(heightMeasureSpec, minHeight.toInt())
        setMeasuredDimension(widthSize, heightSize)
    }

    /**
     * 计算高度和宽度
     */
    private fun getMeasureSize(Spec: Int, minValue: Int): Int {
        var result = 0
        // 获取模式
        val mode = MeasureSpec.getMode(Spec)
        val size = MeasureSpec.getSize(Spec)

        // 判断一下
        when (mode) {
            MeasureSpec.AT_MOST -> {
                result = Math.min(size, minValue)
            }
            MeasureSpec.UNSPECIFIED -> {
                result = minValue
            }
            MeasureSpec.EXACTLY -> {
                result = size
            }
        }
        return result
    }

    /**
     * dp转换为px
     */
    private fun dp2px(size: Int): Float {
        return resources.displayMetrics.density * size
    }
}


