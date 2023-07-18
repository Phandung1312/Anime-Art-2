package com.anime.art.ai.common.extension

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.view.WindowInsets

@SuppressLint("InternalInsetResource", "DiscouragedApi")
fun Activity.getStatusBarHeight(): Int {
    var statusBarHeight = 0
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowInsets = window?.decorView?.rootWindowInsets
        windowInsets?.let {
            val insets = it.getInsets(WindowInsets.Type.statusBars())
            statusBarHeight = insets.top
        }
    } else {
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight = resources.getDimensionPixelSize(resourceId)
        }
    }
    return statusBarHeight
}