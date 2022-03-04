package com.weizu.custionviewpager

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.viewpager.widget.ViewPager
import kotlin.math.abs

class MainActivity : AppCompatActivity() {

    val custom_viewpager by lazy { findViewById<MyViewPager>(R.id.custom_viewpager) }
    val radioGroup by lazy { findViewById<RadioGroup>(R.id.radioGroup) }
    var imageRes = listOf<Int>(R.drawable.a, R.drawable.b, R.drawable.c)

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        for (i in 0 until imageRes.size) {
            val imageView = ImageView(this)
            imageView.setBackgroundResource(imageRes.get(i))
            custom_viewpager.addView(imageView)
        }

        val view = layoutInflater.inflate(R.layout.activity_other, custom_viewpager, false)
        val linearlayout by lazy { view.findViewById<LinearLayout>(R.id.linearlayout) }
        for (i in 0..50) {
            val textView = TextView(this)
            textView.width = resources.displayMetrics.widthPixels
            textView.height = 90
            textView.gravity = Gravity.CENTER
            textView.textSize = 22F
            textView.text = "文本：${i}"
            linearlayout.addView(textView)
        }
        val scrollview = view.findViewById<ScrollView>(R.id.scrollview)
        custom_viewpager.addView(view, 0)

        var interceptorX = 0f
        var interceptorY = 0f
        // 添加到ViewGroup之后执行

        scrollview.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                Log.e("TAG", "scrollview调用onTouchEvent， action：${event?.action }")
                scrollview.onTouchEvent(event)
                return true
            }
        })
//        scrollview.setOnTouchListener(object : View.OnTouchListener {
//            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
//                when (event?.action) {
//                    MotionEvent.ACTION_DOWN -> {
//                        //Log.e("TAG", "Scrollview的Down")
//                        // 告诉父控件，自己要处理，不允许拦截
//                        scrollview.parent?.apply {
//                            scrollview.parent.requestDisallowInterceptTouchEvent(true)
//                        }
//                        // 起始位置
//                        interceptorX = event.x
//                        interceptorY = event.y
//                    }
//                    MotionEvent.ACTION_MOVE -> {
//                        event.apply {
//                            if (abs(event.x - interceptorX) >= abs(event.y - interceptorY)) {
//                                // Log.e("TAG", "ScrollView不消费")
//                                // 让父控件拦截
//                                scrollview.parent.requestDisallowInterceptTouchEvent(
//                                    false
//                                )
//                                return false // ScrollView不消费
//                            } else {
//                                scrollview.parent.requestDisallowInterceptTouchEvent(
//                                    true
//                                )
//                                // Log.e("TAG", "交给scrollview处理: ${event.action}")
//                                scrollview.onTouchEvent(event)
//                                return true
//                            }
//                        }
//                    }
//                }
//                return false
//            }
//        })

        for (i in 0 until custom_viewpager.childCount) {
            val btn = RadioButton(this)
            if (i == 0) btn.isChecked = true
            btn.id = i
            radioGroup.addView(btn)
        }

        radioGroup.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                // 切换页面
                custom_viewpager.scrollToPage(checkedId)
            }
        })

        // 页面切换关联radioButton
        custom_viewpager.mOnPagerChangerListener = object : MyViewPager.OnPagerChangerListener {
            override fun onPageChange(position: Int) {
                radioGroup.check(position)
            }
        }
    }
}