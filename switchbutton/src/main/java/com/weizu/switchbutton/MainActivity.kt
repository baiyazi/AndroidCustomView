package com.weizu.switchbutton

import android.annotation.SuppressLint
import android.app.ActionBar
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.view.marginLeft
import androidx.recyclerview.widget.LinearLayoutManager

class MainActivity : AppCompatActivity() {

    val gb by lazy { findViewById<ImageView>(R.id.btn_b)}
    val qg by lazy { findViewById<ImageView>(R.id.btn_f)}

    var startX = 0f
    var marginLeftValue = 0f
    var margin = 0f
    var isOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other)

        qg.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                margin = (gb.width - qg.width).toFloat()
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        // 记录起始位置
                        startX = event.x
                    }
                    MotionEvent.ACTION_MOVE -> {
                        // 计算逻辑位置
                        val offset = event.x - startX
                        marginLeftValue += offset
                        // 屏蔽边界
                        if (marginLeftValue < 0) {
                            marginLeftValue = 0f
                        } else if (marginLeftValue > margin) {
                            marginLeftValue = margin
                        }
                        refresh()
                    }
                }

                return true
            }
        })
    }

    fun refresh(){
        val parms = RelativeLayout.LayoutParams(qg.width, qg.height)
        parms.marginStart = marginLeftValue.toInt()
        qg.layoutParams = parms
    }
}