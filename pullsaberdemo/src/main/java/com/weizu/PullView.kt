package com.weizu

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.DecelerateInterpolator

class PullView : View {

    constructor(context: Context?) : super(context) {
        init(context, null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    // 绘制圆形的画笔
    private val mCirclePaint: Paint = Paint()

    // 圆形半径
    private val mCircleRadius = 60f

    // 圆形的中心点
    private var mCircleCenterX = 200f

    // 直接使用Y轴值，来存储手指移动的距离
    private var mCircleCenterY = -mCircleRadius

    // 圆形的画笔宽度和边距
    private var mCircleStrokeWidth = 5f

    // 最大移动的距离值
    private var mFingerMovedMaxDistance = 300f

    // 最大移动角度
    private var mMaxMoveAngle = 90f

    // 手指移动的百分比
    private var mFingerMovedProgress = 0f

    // 贝塞尔曲线
    var mPath: Path = Path()
    // 绘制贝塞尔曲线的画笔
    private val mPathPaint: Paint = Paint()

    /**
     * 初始化方法
     */
    private fun init(context: Context?, attrs: AttributeSet?) {
        // 设置画笔
        mCirclePaint.isAntiAlias = true // 抗锯齿
        mCirclePaint.color = Color.GRAY // 颜色
        mCirclePaint.isDither = true // 抗抖动
        mCirclePaint.strokeWidth = 5f // 线粗细
        mCirclePaint.style = Paint.Style.FILL // 样式为：填充

        // 设置画笔
        mPathPaint.isAntiAlias = true // 抗锯齿
        mPathPaint.color = Color.RED // 颜色
        mPathPaint.isDither = true // 抗抖动
        mPathPaint.strokeWidth = 5f // 线粗细
        mPathPaint.style = Paint.Style.FILL // 样式为：填充
    }

    /**
     * 测量一下控件所需的宽度
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.e("TAG", "onMeasure: " )
        // 获取模式
        val defalut_width = ((mCircleRadius + mCircleStrokeWidth) * 2 + paddingLeft + paddingRight).toInt()
        val defalut_height = (mFingerMovedMaxDistance * mFingerMovedProgress + 0.5f).toInt()
        val widthSize = getMeasureSize(widthMeasureSpec, defalut_width)
        val heightSize = getMeasureSize(heightMeasureSpec, defalut_height)
        setMeasuredDimension(widthSize, heightSize)
        mCircleCenterX = measuredWidth / 2f
    }
    

    /**
     * 计算高度和宽度
     */
    private fun getMeasureSize(Spec: Int, minValue: Int):Int {
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
     * 绘制方法
     */
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        for (i in 0..10){
            canvas?.translate(i.toFloat(), 0f)
        }
        canvas?.drawPath(mPath, mPathPaint);
        canvas?.drawCircle(mCircleCenterX, mCircleCenterY, mCircleRadius, mCirclePaint)

    }

    /**
     * 从外部控件传入手指移动的距离
     */
    fun setFingerMovedDistenceY(distanceY: Float) {
        // 更新进度值，百分比
        if(distanceY > mFingerMovedMaxDistance){
            mFingerMovedProgress = 1f
        } else {
            mFingerMovedProgress = distanceY / mFingerMovedMaxDistance
        }
        updateProgress()
    }

    /**
     * 更新进度值，百分比
     */
    private fun updateProgress() {
        // 计算圆心距离
        mCircleCenterY = getValueByProgress(0f, mFingerMovedMaxDistance, mFingerMovedProgress)
        mFingerMovedProgress = mCircleCenterY / mFingerMovedMaxDistance
        // 角度
        val angle = getValueByProgress(0f, mMaxMoveAngle, mFingerMovedProgress)
        // 弧度
        val radian = Math.toRadians(angle.toDouble())
        updateCoordinate(radian)
        invalidate()
    }

    private fun updateProgress(value:Float){
        mFingerMovedProgress = value
        // 计算圆心距离
        mCircleCenterY = getValueByProgress(0f, mFingerMovedMaxDistance, mFingerMovedProgress)
        mFingerMovedProgress = mCircleCenterY / mFingerMovedMaxDistance
        // 角度
        val angle = getValueByProgress(0f, mMaxMoveAngle, mFingerMovedProgress)
        // 弧度
        val radian = Math.toRadians(angle.toDouble())
        updateCoordinate(radian)
        invalidate()
    }


    private fun getValueByProgress(start: Float, end: Float, progress: Float): Float{
        return start + (end - start) * progress;
    }


    /**
     * 根据theta来计算贝塞尔曲线的三个点坐标
     */
    private fun updateCoordinate(theta: Double) {
        val cx = mCircleCenterX
        val cy = mCircleCenterY
        val r = mCircleRadius
        // 圆上左边点
        val A_X = cx - r * Math.sin(theta)
        val A_Y = cy + r * Math.cos(theta)
        // 和横轴的交点
        val B_X = cx - r - A_Y / Math.tan(theta)
        val B_Y = 0
        // 最左边的点
        val C_X = B_X - (r * Math.cos(theta) + cy) / Math.sin(theta)
        val C_Y = 0
        // 绘制贝赛尔曲线
        mPath.reset()
        mPath.moveTo(A_X.toFloat(), A_Y.toFloat())
        mPath.quadTo(B_X.toFloat(), B_Y.toFloat(), C_X.toFloat(), C_Y.toFloat())
        mPath.lineTo((cx + (cx - C_X)).toFloat(), 0F)
        mPath.quadTo((cx + (cx - B_X)).toFloat(), 0F, (cx + r * Math.sin(theta)).toFloat(),
            A_Y.toFloat()
        )

    }

    private var valueAnimator: ValueAnimator? = null

    fun fingerRelease(){
        if(valueAnimator == null){
            valueAnimator = ValueAnimator.ofFloat(mFingerMovedProgress, -(mCircleRadius/mFingerMovedMaxDistance))
            valueAnimator?.interpolator = DecelerateInterpolator()
            valueAnimator?.duration = 5000
            valueAnimator?.addUpdateListener {
                val animatedValue = it.animatedValue
                if(animatedValue is Float){
                    updateProgress(animatedValue)
                }
            }
        } else {
            valueAnimator?.cancel()
            valueAnimator?.setFloatValues(mFingerMovedProgress, -(mCircleRadius/mFingerMovedMaxDistance))
        }
        valueAnimator?.start()
    }

}