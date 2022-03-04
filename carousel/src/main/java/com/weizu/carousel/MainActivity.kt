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
 * 完整版，包括指示器和标题，自动轮播，点击暂停等
 */
class MainActivity : AppCompatActivity() {
    val viewPager by lazy { findViewById<ViewPager>(R.id.other_viewpager) }
    val other_linearlayout by lazy { findViewById<LinearLayout>(R.id.other_linearlayout) }
    val other_textview by lazy { findViewById<TextView>(R.id.other_textview) }


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


    /**
     * 因为采用图片前后加一个，所以实际上和指示器的对应关系需要计算
     */
    fun mappingImageIndexToLogisticIndex(index: Int): Int{
        if(index > 0 && index < imageViewList.size - 1){
            return index - 1
        } else if( index == 0) {
            return imageViewList.size - 3
        } else {
            return 0
        }
    }


    /**
     * toNext: 表示是否是用户手动触发的滚动
     * right: 表示滑动的方向是否是向右
     */
    fun updateInfo(toNext: Boolean, right: Boolean){
        // 计算下一页下标
        var item = 0
        item = if(right) (currentPointerIndex + 1) % imageViewList.size
        else if (currentPointerIndex - 1 < 0) imageViewList.size - 3 else ((currentPointerIndex - 1) % imageViewList.size)
        if(toNext) viewPager.currentItem = item
        // 切换指示器
        for(i in 0.until(textViewTitle.size)){
            other_linearlayout.getChildAt(i)?.isEnabled = false
        }
        other_linearlayout.getChildAt(mappingImageIndexToLogisticIndex(item))?.isEnabled = true
        // 设置标题
        other_textview.text = textViewTitle[mappingImageIndexToLogisticIndex(item)]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other)

        // 为recyclerView添加图片资源
        initialization()

        handler = object: Handler(mainLooper){
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                if(msg.what == 0){
                    updateInfo(true, true)
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
            var currentPosition = 0
            var left_direction = false

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                left_direction = position <= currentPosition
                currentPosition = position
            }

            override fun onPageSelected(position: Int) {
                currentPointerIndex = position

                // 更新一下指示器和标题
                updateInfo(false, !left_direction)
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

    val textViewTitle = listOf<String>(
        "标题1！",
        "标题2！",
        "标题3！",
        "标题4！",
        "标题5",
    )

    val imageViewList = mutableListOf<ImageView>()

    fun initialization() {
        // EABCDEA
        for (imageId in imageDrawableId) {
            val imageView = ImageView(this)
            imageView.setBackgroundResource(imageId)
            imageViewList.add(imageView)
        }

        // 初始化指示器
        for(i in 0.until(textViewTitle.size)){
            val imageView = ImageView(this)
            imageView.setBackgroundResource(R.drawable.pointer)
            // 因为外层为LinearLayout，所以这里为LinearLayout
            val layoutParams = LinearLayout.LayoutParams(20, 20)
            if(i == 0){
                imageView.isEnabled = true
            } else{
                imageView.isEnabled = false
                layoutParams.marginStart = 8
            }
            imageView.layoutParams = layoutParams
            other_linearlayout.addView(imageView)
        }

        // 设置标题为第1个
        other_textview.text = textViewTitle[0]
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