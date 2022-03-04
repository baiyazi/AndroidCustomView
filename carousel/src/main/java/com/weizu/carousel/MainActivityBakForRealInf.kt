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
 * 真正无限轮播
 */
class MainActivityBakForRealInf : AppCompatActivity() {
    val viewPager by lazy { findViewById<ViewPager>(R.id.viewPager) }

    val imageDrawableId = listOf<Int>(
        R.drawable.e,
        R.drawable.a,
        R.drawable.b,
        R.drawable.c,
        R.drawable.d,
        R.drawable.e,
        R.drawable.a
    )

    var currentPointerIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 为recyclerView添加图片资源
        initialization()

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
                currentPointerIndex = position
            }

            override fun onPageScrollStateChanged(state: Int) {
                //验证当前的滑动是否结束
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    if (currentPointerIndex == 0) {
                        viewPager.setCurrentItem(imageViewList.size - 2, false);//切换，不要动画效果
                    } else if (currentPointerIndex == imageViewList.size - 1) {
                        viewPager.setCurrentItem(1, false);//切换，不要动画效果
                    }
                }
            }
        })
    }



    val imageViewList = mutableListOf<ImageView>()

    fun initialization() {
        // EABCDEA
        for (imageId in imageDrawableId) {
            val imageView = ImageView(this)
            imageView.setBackgroundResource(imageId)
            imageViewList.add(imageView)
        }
    }

    inner class MyViewPagerAdapter : PagerAdapter() {

        // 初始化 container->ViewPager
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view = imageViewList[position]
            if (view.parent != null) {
                (view.parent as ViewGroup).removeView(view)
            }
            container.addView(view)
            return view
        }

        // 返回图片个数
        override fun getCount(): Int {
            return imageViewList.size
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