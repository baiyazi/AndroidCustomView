package com.weizu.myapplication.utils

import android.content.Context

class DensityUtil {

    fun dp2px(context: Context, dpValue: Float): Int{
        val density = context.resources.displayMetrics.density
        return (density * dpValue + 0.5f).toInt()
    }

    fun px2dp(context: Context, pxValue: Float): Int{
        val density = context.resources.displayMetrics.density
        return (pxValue / density + 0.5f).toInt()
    }
}