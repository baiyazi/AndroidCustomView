package com.weizu.rotatemenu

import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.RotateAnimation
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.view.children

/**
 * 实现旋转菜单
 */

class MainActivity : AppCompatActivity(), View.OnClickListener{

    val icon_home by lazy { findViewById<ImageView>(R.id.icon_home) }
    val icon_menu by lazy { findViewById<ImageView>(R.id.icon_menu) }
    val level1 by lazy { findViewById<RelativeLayout>(R.id.level1) }
    val level2 by lazy { findViewById<RelativeLayout>(R.id.level2) }
    val level3 by lazy { findViewById<RelativeLayout>(R.id.level3) }
    var isShowLevel2 = true
    var isShowLevel3 = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 旋转
        icon_home.setOnClickListener(this)
        icon_menu.setOnClickListener(this)
        level1.setOnClickListener(this)
        level2.setOnClickListener(this)
        level3.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.icon_home -> {
                if (isShowLevel2) {
                    // 关闭第二级菜单
                    hideView(level2)
                    if(isShowLevel3) {
                        hideView(level3, 20)
                        isShowLevel3 = false
                    }
                } else {
                    // 打开第二级菜单
                    showView(level2)
                }
                isShowLevel2 = !isShowLevel2
            }
            R.id.icon_menu -> {
                if (isShowLevel3) {
                    // 关闭第二级菜单
                    hideView(level3)
                } else {
                    // 打开第二级菜单
                    showView(level3)
                }
                isShowLevel3 = !isShowLevel3
            }

        }
    }

    fun showView(view: ViewGroup){
        showView(view, 0)
    }

    fun hideView(view: ViewGroup){
        hideView(view, 0)
    }

    fun showView(view: ViewGroup, delayTime: Int){
        roateViewByAttrAnimator(view, 180, 360, delayTime)
    }

    fun hideView(view: ViewGroup, delayTime: Int){
        roateViewByAttrAnimator(view, 0, 180, delayTime)
    }


    /**
     * 属性动画实现旋转效果
     */
    fun rotateViewByRoateAnimation(view: ViewGroup, fromDegrees: Int, toDegrees: Int, delayTime: Int){
        val rotateAnimation = RotateAnimation(fromDegrees*1f, toDegrees*1f,
            view.width / 2f, view.height * 1f)
        rotateAnimation.duration = 500
        rotateAnimation.fillAfter = true
        // 设置延迟多久播放
        rotateAnimation.startOffset = delayTime.toLong()
        view.startAnimation(rotateAnimation)

        // 所有元素不可点击或可点击
        val children = view.children
        for (child in children) {
            child.isEnabled = (fromDegrees!=0)
        }
    }

    fun roateViewByAttrAnimator(view: ViewGroup, fromDegrees: Int, toDegrees: Int, delayTime: Int){
        val animator = ObjectAnimator.ofFloat(view, "rotation", fromDegrees*1f, toDegrees*1f)
        animator.duration = 500
        animator.startDelay = delayTime.toLong()
        // 设置旋转中心
        view.pivotX = view.width / 2f
        view.pivotY = view.height * 1f
        animator.start()
    }



}