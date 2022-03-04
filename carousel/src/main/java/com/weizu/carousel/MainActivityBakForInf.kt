package com.weizu.carousel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager

/**
 * 假的无限轮播
 */
class MainActivityBakForInf : AppCompatActivity() {
    val viewPager by lazy { findViewById<ViewPager>(R.id.viewPager) }
    val textView_title by lazy { findViewById<TextView>(R.id.textView_title) }
    val linearLayout_pointer by lazy { findViewById<LinearLayout>(R.id.linearLayout_pointer) }


    val imageDrawableId = listOf<Int>(
        R.drawable.a,
        R.drawable.b,
        R.drawable.c,
        R.drawable.d,
        R.drawable.e
    )

    val textViewTitle = listOf<String>(
        "尚硅谷波河争霸赛！",
        "凝聚你我，放飞梦想！",
        "抱歉没座位了！",
        "7月就业名单全部曝光！",
        "平均起薪11345元"
    )

    var currentPointerIndex = 0
    val Value = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 为recyclerView添加图片资源
        initialization()
        currentPointerIndex = Value/2 - Value/2%(imageViewList.size)

        // 设置适配器
        viewPager.adapter = MyViewPagerAdapter()

        // 设置从第一个开始
        viewPager.currentItem = currentPointerIndex

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                // 这里需要对应更新指示器和标题的下标
                updatePointer(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })

//        findViewById<Button>(R.id.btn).setOnClickListener {
//            val temp = viewPager.currentItem + 1
//            // 判断temp的位置
//            viewPager.currentItem = temp%(imageViewList.size)
//        }
    }

    fun updatePointer(position: Int) {
        Log.e("TAG", "updatePointer: ${position}" )
        // 这里关联指示器的动态效果
        linearLayout_pointer.getChildAt(position%(imageViewList.size)).isEnabled = true
        // 之前的设置为灰色
        linearLayout_pointer.getChildAt(currentPointerIndex%(imageViewList.size)).isEnabled = false
        // 更新标题
        textView_title.setText(textViewTitle[currentPointerIndex%(imageViewList.size)])
        // 更新当前下标
        currentPointerIndex = position
    }

    val imageViewList = mutableListOf<ImageView>()

    fun initialization() {
        // DABCDEA
        for (imageId in imageDrawableId) {
            val imageView = ImageView(this)
            imageView.setBackgroundResource(imageId)
            imageViewList.add(imageView)
        }

        // 添加指示器
        for (i in 0.until(imageDrawableId.size)) {
            val imageView = ImageView(this)
            imageView.setBackgroundResource(R.drawable.pointer)
            val layoutParams = LinearLayout.LayoutParams(20, 20)
            if (i == 0) {
                imageView.isEnabled = true
            } else {
                imageView.isEnabled = false
                layoutParams.marginStart = 8
            }
            imageView.layoutParams = layoutParams
            linearLayout_pointer.addView(imageView)
        }

        // 设置标题
        textView_title.setText(textViewTitle[currentPointerIndex%(imageViewList.size)])
    }

    inner class MyViewPagerAdapter : PagerAdapter() {

        // 初始化 container->ViewPager
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view = imageViewList[position%(imageViewList.size)]
            if (view.parent != null) {
                (view.parent as ViewGroup).removeView(view)
            }
            container.addView(view)
            return view
        }

        // 返回图片个数
        override fun getCount(): Int {
            return Value
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view == `object`
        }

        // 移除
        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }
}