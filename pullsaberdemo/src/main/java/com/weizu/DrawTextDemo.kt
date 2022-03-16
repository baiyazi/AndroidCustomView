package com.weizu

import android.content.Context
import android.content.pm.PackageItemInfo
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.weizu.pullsaberdemo.R

/**
 * 绘制文本测试和学习
 * 2022年3月14日
 */
class DrawTextDemo:View {
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

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    // 绘制文本的画笔
    private lateinit var mPaint: Paint
    private lateinit var mBgPaint: Paint
    private var mFontSize = dp2px(30)
    private var mContent = "text"
    private var startX = mFontSize
    private var startY = mFontSize

    /**
     * 初始化方法
     */
    private fun init(){
        var paint = Paint()
        paint.color = Color.BLACK // 黑色
        paint.style = Paint.Style.STROKE // 不填充
        paint.isAntiAlias = true // 抗锯齿
        paint.isDither = true // 防抖动
        paint.textSize = mFontSize  // 字体大小
        paint.strokeWidth = 5f // 画笔宽度


        mPaint = paint

        paint = Paint()
        paint.color = Color.GRAY // 黑色
        paint.style = Paint.Style.FILL // 不填充
        paint.isAntiAlias = true // 抗锯齿
        paint.isDither = true // 防抖动
        mBgPaint = paint
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.apply {
            startX = 100f
            startY = 300f

            // 原始路径样式
            var linePath = Path()
            linePath.moveTo(startX, startY)
            linePath.lineTo(startX + 400, startY - 100)
            linePath.lineTo(startX + 600, startY + 100)
            linePath.lineTo(startX + 800, startY)
            linePath.lineTo(startX + 1000, startY + 100)
            drawPath(linePath, mPaint)

            // 圆角特效
            translate(0f, 100f)
            var cornerPathEffect = CornerPathEffect(100f)
            mPaint.pathEffect = cornerPathEffect
            drawPath(linePath, mPaint)

            // 虚线特效
            translate(0f, 100f)
            var dashPathEffect = DashPathEffect(floatArrayOf(2f,5f,10f,10f),0f)
            mPaint.pathEffect = dashPathEffect
            drawPath(linePath, mPaint)

            // 利用ComposePathEffect先应用圆角特效,再应用虚线特效
            translate(0f, 100f)
            var composePathEffect = ComposePathEffect(dashPathEffect, cornerPathEffect)
            mPaint.pathEffect = composePathEffect
            drawPath(linePath, mPaint)

        }

    }

    /**
     * 测量一下所需的内容的大小
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // 使用画笔工具来获取字体的宽度
        val defaultWidth = mPaint.measureText(mContent)
        // 使用bottom线值减去top线值，得到字符串所占据的高度值
        val bottom = mPaint.fontMetrics.bottom
        val top = mPaint.fontMetrics.top
        val defaultHeight = bottom - top
        Log.e("TAG", "defaultWidth: ${defaultWidth}, defaultHeight: ${defaultHeight}", )
        val widthSize = getMeasuredSize(widthMeasureSpec, defaultWidth.toInt())
        val heightSize = getMeasuredSize(heightMeasureSpec, defaultHeight.toInt())
        setMeasuredDimension(widthSize, heightSize)
    }

    /**
     * 实际测量
     */
    private fun getMeasuredSize(Spec: Int, defaultValue: Int) :Int {
        var result = 0
        val mode = MeasureSpec.getMode(Spec)
        val size = MeasureSpec.getSize(Spec)

        // 判断一下
        when (mode) {
            MeasureSpec.AT_MOST -> {
                result = Math.min(size, defaultValue)
            }
            MeasureSpec.UNSPECIFIED -> {
                result = defaultValue
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
    private fun dp2px(size: Int): Float{
        return resources.displayMetrics.density * size
    }
}