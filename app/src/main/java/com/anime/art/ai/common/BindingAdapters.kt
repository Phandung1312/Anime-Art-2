package com.anime.art.ai.common

import android.graphics.Color
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
            0f, 0f, width, tv.textSize, intArrayOf(
                ContextCompat.getColor(tv.context, R.color.yellow),
                ContextCompat.getColor(tv.context, R.color.dark_yellow),
            ), null, Shader.TileMode.CLAMP
        )
        paint.shader = textShader
        tv.requestLayout()
    }

    @JvmStatic
    @BindingAdapter("yellowArrayColor")
    fun setYellowArrayColor(tv : TextView, textGradientYellow : String?){
        val paint = tv.paint
        val width = paint.measureText(tv.text.toString())
        val textShader: Shader = LinearGradient(
            0f, 0f, width, tv.textSize, intArrayOf(
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
        tv.requestLayout()
    }
}