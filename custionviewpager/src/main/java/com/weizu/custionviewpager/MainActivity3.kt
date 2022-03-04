package com.weizu.custionviewpager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup

/**
 * 自定义ViewPager
 */
class MainActivity3 : AppCompatActivity() {

    val custom_viewpager by lazy { findViewById<MyViewPager>(R.id.custom_viewpager) }
    val radioGroup by lazy { findViewById<RadioGroup>(R.id.radioGroup) }
    var imageRes = listOf<Int>(R.drawable.a, R.drawable.b, R.drawable.c)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        for (i in 0 until imageRes.size){
            val imageView = ImageView(this)
            imageView.setBackgroundResource(imageRes.get(i))
            custom_viewpager.addView(imageView)
        }

        val view = layoutInflater.inflate(R.layout.activity_other, null)
        custom_viewpager.addView(view, 2)

        for(i in 0 until custom_viewpager.childCount){
            val btn = RadioButton(this)
            if(i == 0) btn.isChecked = true
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
        custom_viewpager.mOnPagerChangerListener = object : MyViewPager.OnPagerChangerListener{
            override fun onPageChange(position: Int) {
                radioGroup.check(position)
            }
        }
    }
}