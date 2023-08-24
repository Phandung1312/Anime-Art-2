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
                ContextCompat.getColor(tv.context, R.color.colorSecondary),
                ContextCompat.getColor(tv.context, R.color.colorPrimary),
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