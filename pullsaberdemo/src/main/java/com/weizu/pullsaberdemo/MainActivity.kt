package com.weizu.pullsaberdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.RelativeLayout
import com.weizu.PullView

class MainActivity : AppCompatActivity() {
    // 记录手指按下坐标位置
    private var mFingerPressedY = 0f
    val relativeLayout by lazy { findViewById<RelativeLayout>(R.id.relativeLayout) }
    val pullView by lazy {  findViewById<PullView>(R.id.pullView) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        relativeLayout.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                var flag = false
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        // 记录初始位置
                        mFingerPressedY = event.y
                        flag = true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        // 检查距离
                        val distanceY = event.y - mFingerPressedY
                        if(distanceY > 0){
                            flag = true
                            pullView.setFingerMovedDistenceY(distanceY)
                        }
                    }
                    MotionEvent.ACTION_UP -> {
                        // 重置
                        mFingerPressedY = 0f
                        flag = false
                        pullView.fingerRelease()
                    }
                }
                return flag
            }
        })
    }



}