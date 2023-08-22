package com.anime.art.ai.domain.model.response


import com.google.gson.annotations.SerializedName

data class ImageResponseSuccess(
    @SerializedName("generationTime")
    val generationTime: Double = 0.0,
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("meta")
    val meta: Meta = Meta(),
    @SerializedName("nsfw_content_detected")
    val nsfwContentDetected: Boolean = false,
    @SerializedName("output")
    val output: List<String> = listOf(),
    @SerializedName("status")
    val status: String = "",
    @SerializedName("webhook_status")
    val webhookStatus: String = ""
)
data class ImagePreview(
    val url : String = "",
    val ratio : String = "1:1",
    var isReady : Boolean = false
)
