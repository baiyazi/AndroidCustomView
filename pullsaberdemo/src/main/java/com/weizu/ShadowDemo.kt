package com.weizu

import android.content.Context
import android.util.AttributeSet
import android.view.View

/**
 * 阴影效果学习
 * @author 梦否
 * 2022-3-16
 */
class ShadowDemo:View {
    constructor(context: Context?) : super(context) {
        init()
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    /**
     * 初始化方法
     */
    private fun init(){

    }
}