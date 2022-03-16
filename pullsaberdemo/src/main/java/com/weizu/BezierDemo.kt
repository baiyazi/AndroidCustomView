package com.weizu

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.View

class BezierDemo:View {
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

    lateinit var mPath: Path
    lateinit var mPaint: Paint

    private fun init(){
        mPath = Path()
        mPaint = Paint()
        mPaint.isAntiAlias = true
        mPaint.color = Color.RED
        mPaint.isDither = true
        mPaint.strokeWidth = 5f
        mPaint.style = Paint.Style.STROKE
    }

    // 绘图方法
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {
            val points = arrayOf(Point(200f, 400f),
                Point(100f, 20f),
                Point(500f, 20f),
                Point(800f, 400f),
                Point(1000f, 20f)
            )
            val numberOfPoint = 1000
            mPath = getBezierPointsPath(points, numberOfPoint)
            drawPath(mPath, mPaint)


        }
    }

    class Point(var x: Float, var y: Float){
    }

    /**
     * 得到贝赛尔曲线上的点集
     * @param points 起始、控制和终止点坐标
     * @param number 需要计算的贝赛尔曲线上的点的个数
     * @return 返回路径
     */
    private fun getBezierPointsPath(points: Array<Point>, number: Int): Path{
        val path = Path()
        for (time in 0 until number){
            val t = time * 1f / number
            val point = calcPoint(points, t)
            if(time == 0){
                path.moveTo(point.x, point.y)
            } else {
                path.lineTo(point.x, point.y)
            }
            Log.e("TAG", "getBezierPointsPath: ${point.x} , ${point.y}", )
        }
        return path
    }


    /**
     * 计算在t时刻上，位于贝赛尔曲线上的点的坐标
     * @param points 点的集合
     * @param t 时刻，属于0-1
     * @return 点坐标 Point
     */
    private fun calcPoint(points: Array<Point>, t: Float): Point{
        // 分别求任意两个点之间的在t时刻运动的距离
        // 任意两点，按照顺序分别为始和终
        var index = 0
        var len = points.size - 1
        while (index < len){
            points[index].x = getValueByTime(points[index].x, points[index + 1].x, t)
            points[index].y = getValueByTime(points[index].y, points[index + 1].y, t)
            index++
            if(index == len){
                index = 0
                len--
            }
        }
        return points[0]
    }

    /**
     * 定义匀速运动的计算坐标
     * @param start 开始的位置
     * @param end 结束的位置
     * @param time 运动的时间，范围0-1
     * @return time时刻的运动位置
     */
    private fun getValueByTime(start: Float, end: Float, time: Float): Float{
        return start + (end - start) * time
    }
}
