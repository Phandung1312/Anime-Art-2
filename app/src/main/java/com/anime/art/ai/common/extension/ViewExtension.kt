package com.anime.art.ai.common.extension

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.view.MotionEvent
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat

fun TextView.gradient(startColor: Int, endColor: Int) {
        val paint = this.paint
        val width = paint.measureText(this.text.toString())

        val textShader: Shader = LinearGradient(
            0f, 0f, width, this.textSize, intArrayOf(
                ContextCompat.getColor(this.context, startColor),
                ContextCompat.getColor(this.context, endColor),
            ), null, Shader.TileMode.CLAMP
        )

        paint.shader = textShader
}
fun TextView.gradientYellowArray() {
    val paint = this.paint
    val width = paint.measureText(this.text.toString())

    val textShader: Shader = LinearGradient(
        0f, 0f, width, this.textSize, intArrayOf(
            Color.parseColor("#A57D24"),
            Color.parseColor("#A88128"),
            Color.parseColor("#B28E34"),
            Color.parseColor("#C3A348"),
            Color.parseColor("#DABF64"),
            Color.parseColor("#F1DD80"),
            Color.parseColor("#F1DD80"),
            Color.parseColor("#A57D24"),
            Color.parseColor("#BA973B"),
        ), null, Shader.TileMode.CLAMP
    )

    paint.shader = textShader
}
fun View.margin(marginStartResId : Int, marginEndResId : Int){
    val layoutParams = this.layoutParams as ViewGroup.MarginLayoutParams
    layoutParams.marginStart = this.context.resources.getDimensionPixelSize(marginStartResId)
    layoutParams.marginEnd = this.context.resources.getDimensionPixelSize(marginEndResId)
}

fun View.showKeyboard(){
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun View.animateShowView(){
    val cx = this.width / 2
    val cy = this.height / 2
    val finalRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()

    val anim = ViewAnimationUtils.createCircularReveal(this, cx, cy, 0f, finalRadius)
    anim.duration = 500
    this.visibility = View.VISIBLE
    anim.start()
}

fun View.animateHideView() {
    val cx = this.width / 2
    val cy = this.height / 2
    val initialRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()

    val anim = ViewAnimationUtils.createCircularReveal(this, cx, cy, initialRadius, 0f)
    anim.duration = 500
    anim.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            this@animateHideView.visibility = View.INVISIBLE
        }
    })
    anim.start()
}
fun EditText.enableScrollText()
{
    overScrollMode = View.OVER_SCROLL_ALWAYS
    scrollBarStyle = View.SCROLLBARS_INSIDE_INSET
    isVerticalScrollBarEnabled = true
    setOnTouchListener { view, event ->
        if (view is EditText) {
            if(!view.text.isNullOrEmpty()) {
                view.parent.requestDisallowInterceptTouchEvent(true)
                when (event.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_UP -> view.parent.requestDisallowInterceptTouchEvent(false)
                }
            }
        }
        false
    }
}

