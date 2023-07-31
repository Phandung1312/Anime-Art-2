package com.anime.art.ai.domain.model.response

import android.support.annotation.Keep
import com.anime.art.ai.domain.model.config.History
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class ImageResponse(
    @SerializedName("image")
    @Expose
    var image : String = "",
    @SerializedName("seed")
    @Expose
    var seed : Long = 0,
    @SerializedName("cost")
    @Expose
    var cost : Double = 0.0,
)