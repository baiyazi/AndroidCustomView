package com.weizu.myapplication

import android.animation.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    val btn by lazy { findViewById<Button>(R.id.btn) }
    val textView by lazy { findViewById<TextView>(R.id.textView) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val animator = ObjectAnimator.ofInt(btn, "width", 700)
        btn.setOnClickListener {
            animator.start()
        }

    }
}