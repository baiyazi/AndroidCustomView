package com.weizu.custionviewpager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.*

class TestActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other)

        val linearlayout by lazy { findViewById<LinearLayout>(R.id.linearlayout) }
        for( i in 0..50){
            val textView = TextView(this)
            textView.width = resources.displayMetrics.widthPixels
            textView.gravity = Gravity.CENTER
            textView.textSize = 22F
            textView.text = "文本：${i}"
            linearlayout.addView(textView)
        }
    }
}