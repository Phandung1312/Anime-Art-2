package com.anime.art.ai.domain.model.config

import android.support.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class ImageToImage(
    @SerializedName("model")
    @Expose
    var model: String = "absolute-reality-v1-6",
    @SerializedName("prompt")
    @Expose
    var prompt: String = "",
    @SerializedName("negative_prompt")
    @Expose
    var negativePrompt: String = "",
    @SerializedName("strength")
    @Expose
    var strength: Double = 0.2,
    @SerializedName("image")
    @Expose
    var image: String = "",
    @SerializedName("steps")
    @Expose
    var steps: Int = 25,
    @SerializedName("guidance")
    @Expose
    var guidance: Double = 7.5,
    @SerializedName("sheduler")
    @Expose
    var scheduler: String = "dpmsolver++",
    @SerializedName("output_format")
    @Expose
    var outputFormat: String = "jpeg",
)
