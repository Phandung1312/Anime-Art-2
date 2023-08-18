package com.anime.art.ai.domain.model.config


import com.google.gson.annotations.SerializedName

data class TextToImage(
    @SerializedName("enhance_prompt")
    val enhancePrompt: String? = "yes",
    @SerializedName("guidance_scale")
    val guidanceScale: Double? = null,
    @SerializedName("height")
    val height: String? = null,
    @SerializedName("key")
    val key: String? = null,
    @SerializedName("model_id")
    val modelId: String? = null,
    @SerializedName("negative_prompt")
    val negativePrompt: String? = null,
    @SerializedName("num_inference_steps")
    val numInferenceSteps: String? = null,
    @SerializedName("prompt")
    val prompt: String? = null,
    @SerializedName("samples")
    val samples: String? = null,
    @SerializedName("scheduler")
    val scheduler: String? = null,
    @SerializedName("width")
    val width: String? = null
)