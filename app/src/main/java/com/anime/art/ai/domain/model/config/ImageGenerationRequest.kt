package com.anime.art.ai.domain.model.config

import android.support.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class ImageGenerationRequest(
    @SerializedName("model")
    @Expose
    var model: String = "stable-diffusion-v1-5",
    @SerializedName("controlnet")
    @Expose
    var controlNet: String = "",
    @SerializedName("prompt")
    @Expose
    var prompt: String = "",
    @SerializedName("negative_prompt")
    @Expose
    var negativePrompt: String = "",
    @SerializedName("image")
    @Expose
    var image: String = "",
    @SerializedName("width")
    @Expose
    var width: Int = 512,
    @SerializedName("height")
    @Expose
    var height: Int = 512,
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
    var outputFormat: String = "jpeg"
)
