package com.anime.art.ai.common

import android.graphics.LinearGradient
import android.graphics.Shader

import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.anime.art.ai.R


object BindingAdapters{
    @JvmStatic
    @BindingAdapter("textGradientYellow")
    fun setGradientYellowText(tv : TextView, textGradientYellow : String?){
        val paint = tv.paint
        val width = paint.measureText(tv.text.toString())
        val textShader: Shader = LinearGradient(
            0f, 0f, 0f, tv.textSize, intArrayOf(
                ContextCompat.getColor(tv.context, R.color.yellow),
                ContextCompat.getColor(tv.context, R.color.dark_yellow),
            ), null, Shader.TileMode.CLAMP
        )
        paint.shader = textShader
    }
}