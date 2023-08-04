package com.anime.art.ai.common.extension

import android.content.Context
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
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
