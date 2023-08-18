package com.anime.art.ai.domain.model.response


import com.google.gson.annotations.SerializedName

data class ImageResponseProcessing(
    @SerializedName("eta")
    val eta: Double = 0.0,
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("image_links")
    val imageLinks: List<String> = listOf(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("meta")
    val meta: Meta = Meta(),
    @SerializedName("output")
    val output: String = "",
    @SerializedName("status")
    val status: String = "",
    @SerializedName("tip")
    val tip: String = "",
    @SerializedName("webhook_status")
    val webhookStatus: String = ""
)