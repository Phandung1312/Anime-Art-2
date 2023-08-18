package com.anime.art.ai.domain.model.response


import com.google.gson.annotations.SerializedName

data class Meta(
    @SerializedName("algorithm_type")
    val algorithmType: String = "",
    @SerializedName("base64")
    val base64: String = "",
    @SerializedName("clip_skip")
    val clipSkip: Int = 0,
    @SerializedName("file_prefix")
    val filePrefix: String = "",
    @SerializedName("full_url")
    val fullUrl: String = "",
    @SerializedName("guidance_scale")
    val guidanceScale: Double = 0.0,
    @SerializedName("H")
    val h: Int = 0,
    @SerializedName("init_image")
    val initImage: String = "",
    @SerializedName("lora")
    val lora: Any? = null,
    @SerializedName("lora_strength")
    val loraStrength: Int = 0,
    @SerializedName("model_id")
    val modelId: String = "",
    @SerializedName("multi_lingual")
    val multiLingual: String = "",
    @SerializedName("n_samples")
    val nSamples: Int = 0,
    @SerializedName("negative_prompt")
    val negativePrompt: String = "",
    @SerializedName("outdir")
    val outdir: String = "",
    @SerializedName("prompt")
    val prompt: String = "",
    @SerializedName("safety_checker")
    val safetyChecker: String = "",
    @SerializedName("safety_checker_type")
    val safetyCheckerType: String = "",
    @SerializedName("scheduler")
    val scheduler: String = "",
    @SerializedName("seed")
    val seed: Int = 0,
    @SerializedName("steps")
    val steps: Int = 0,
    @SerializedName("strength")
    val strength: Double = 0.0,
    @SerializedName("temp")
    val temp: String = "",
    @SerializedName("tomesd")
    val tomesd: String = "",
    @SerializedName("upscale")
    val upscale: String = "",
    @SerializedName("use_karras_sigmas")
    val useKarrasSigmas: String = "",
    @SerializedName("vae")
    val vae: Any? = null,
    @SerializedName("W")
    val w: Int = 0
)