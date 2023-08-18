package com.anime.art.ai.domain.model.config


import com.google.gson.annotations.SerializedName

data class ControlNetToImage(
    @SerializedName("auto_hint")
    val autoHint: String? = "yes",
    @SerializedName("controlnet_model")
    val controlnetModel: String? = null,
    @SerializedName("controlnet_type")
    val controlnetType: String? = null,
    @SerializedName("enhance_prompt")
    val enhancePrompt: String? = "yes",
    @SerializedName("guess_mode")
    val guessMode: String? = "no",
    @SerializedName("guidance_scale")
    val guidanceScale: Double? = null,
    @SerializedName("height")
    val height: String? = null,
    @SerializedName("init_image")
    val initImage: String? = null,
    @SerializedName("key")
    val key: String? = null,
    @SerializedName("mask_image")
    val maskImage: Any? = null,
    @SerializedName("model_id")
    val modelId: String? = null,
    @SerializedName("negative_prompt")
    val negativePrompt: Any? = null,
    @SerializedName("num_inference_steps")
    val numInferenceSteps: String? = null,
    @SerializedName("prompt")
    val prompt: String? = null,
    @SerializedName("safety_checker")
    val safetyChecker: String? = "no",
    @SerializedName("samples")
    val samples: String? = null,
    @SerializedName("scheduler")
    val scheduler: String? = null,
    @SerializedName("seed")
    val seed: Any? = null,
    @SerializedName("strength")
    val strength: Double? = null,
    @SerializedName("track_id")
    val trackId: Any? = null,
    @SerializedName("webhook")
    val webhook: Any? = null,
    @SerializedName("width")
    val width: String? = null
)