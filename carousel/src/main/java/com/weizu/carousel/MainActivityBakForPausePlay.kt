package com.weizu.carousel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.MotionEvent
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
 * 使用Handler来发送延迟消息，以实现无限滑动+自动轮播
 */
class MainActivityBakForPausePlay : AppCompatActivity() {
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

    var handler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 为recyclerView添加图片资源
        initialization()

        handler = object: Handler(mainLooper){
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                if(msg.what == 0){
                    // 设置下一页
                    val item = (viewPager.currentItem + 1) % imageViewList.size
                    viewPager.currentItem = item

                    // 延迟发送消息，一直回调自己
                    sendEmptyMessageDelayed(0, 4000)
                }
            }
        }

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

        // 为viewpager注册一个监听器，用来监听其状态发生改变的时候
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
            }

            // viewpager状态发生改变
            override fun onPageScrollStateChanged(state: Int) {
                when(state){
                    ViewPager.SCROLL_STATE_DRAGGING -> { // 开始拖拽的时候
                        handler?.removeCallbacksAndMessages(null)
                    }
                    ViewPager.SCROLL_STATE_IDLE -> { // 滑动释放的时候
                        handler?.removeCallbacksAndMessages(null)
                        handler?.sendEmptyMessageDelayed(0, 4000)
                    }
                }
            }

        })

        // 启动自动Handler
        handler!!.sendEmptyMessage(0)
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

            // 注册触摸事件
            view.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    when(event?.action){
                        MotionEvent.ACTION_DOWN -> { // 按下
                            // 按下需要暂停，所以需要清空消息队列中的所有消息
                            handler?.removeCallbacksAndMessages(null)  // 传入null，表示清空所有消息
                        }
                        MotionEvent.ACTION_UP -> { // 离开
                            // 离开，也就是再次发送一个延迟的消息到消息队列中
                            handler?.removeCallbacksAndMessages(null)  // 清空可能存在的消息
                            handler?.sendEmptyMessageDelayed(0, 4000)
                        }
                    }

                    return true; // 表示消费掉事件
                }
            })

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