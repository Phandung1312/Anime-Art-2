package com.anime.art.ai.common

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
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
        val textHeight = paint.fontMetrics.descent   - paint.fontMetrics.ascent
        val textShader: Shader = LinearGradient(
            0f, 0f, 0f, textHeight, intArrayOf(
                ContextCompat.getColor(tv.context, R.color.gradient_color_secondary),
                ContextCompat.getColor(tv.context, R.color.gradient_color_primary),
            ), null, Shader.TileMode.CLAMP
        )
        paint.shader = textShader
        tv.invalidate()
    }

    @JvmStatic
    @BindingAdapter("yellowArrayColor")
    fun setYellowArrayColor(tv : TextView, textGradientYellow : String?){
        val paint = tv.paint
        val width = paint.measureText(tv.text.toString())
        val textShader: Shader = LinearGradient(
            0f, 0f, width, tv.textSize, intArrayOf(
                tv.context.getColor(R.color.array_color_1) ,
                tv.context.getColor(R.color.array_color_2),
                tv.context.getColor(R.color.array_color_3),
                tv.context.getColor(R.color.array_color_4),
                tv.context.getColor(R.color.array_color_5),
                tv.context.getColor(R.color.array_color_6),
                tv.context.getColor(R.color.array_color_7),
                tv.context.getColor(R.color.array_color_8),
                tv.context.getColor(R.color.array_color_9)
            ), null, Shader.TileMode.CLAMP
        )
        paint.shader = textShader
        tv.requestLayout()
    }
    @JvmStatic
    @BindingAdapter("strikeRedGradient")
    fun setStrikeRedGradient(tv : TextView, strikeRedGradient : String?){
        val paint = tv.paint
        val width = paint.measureText(tv.text.toString())
        val textShader: Shader = LinearGradient(
            0f, 0f, width, tv.textSize, intArrayOf(
                Color.parseColor("#FE4B4B"),
                Color.parseColor("#F5113A"),
            ), null, Shader.TileMode.CLAMP
        )
        paint.shader = textShader
        tv.paintFlags = tv.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        tv.requestLayout()
    }
}