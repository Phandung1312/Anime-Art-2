package com.anime.art.ai.common.extension

import android.graphics.LinearGradient
import android.graphics.Shader
import android.widget.TextView

fun TextView.gradient(startColor: Int, endColor: Int) {
    val width = paint.measureText(this.text.toString())

    val textShader = LinearGradient(
        0f,
        0f,
        width,
        this.textSize,
        intArrayOf(
            startColor,
            endColor
        ),
        null,
        Shader.TileMode.CLAMP
    )

    this.paint.shader = textShader
}