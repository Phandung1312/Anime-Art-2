package com.anime.art.ai.domain.model.config

import android.support.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Keep
data class ImageGenerationRequest(
    var model: String = "dark-sushi-mix-v2-25",
    var controlNet: String = "",
    var prompt: String = "",
    var negativePrompt: String = "",
    var strength: Double = 0.2,
    var image: String = "",
    var width: Int = 512,
    var height: Int = 512,
    var steps: Int = 25,
    var guidance: Double = 7.5,
    var scheduler: String = "euler_a",
    var outputFormat: String = "jpeg",
    var artStyle : String = "AGG",
    var ratio : String = "1:1"
)
fun ImageGenerationRequest.toTextToImage() : TextToImage{
    return TextToImage(
        model = this.model,
        prompt = this.prompt,
        negativePrompt = this.negativePrompt,
        width = this.width,
        height = this.height,
        steps = this.steps,
        guidance = this.guidance,
        scheduler = this.scheduler,
        outputFormat = this.outputFormat
    )
}

fun ImageGenerationRequest.toImageToImage() : ImageToImage{
    return ImageToImage(
        model = this.model,
        prompt = this.prompt,
        negativePrompt = this.negativePrompt,
        image = this.image,
        strength = this.strength,
        steps = this.steps,
        guidance = this.guidance,
        scheduler = this.scheduler,
        outputFormat = this.outputFormat
    )
}

fun ImageGenerationRequest.toControlNet() : ControlNetImage{
    return ControlNetImage(
        model = this.model,
        controlNet = this.controlNet,
        prompt = this.prompt,
        negativePrompt = this.negativePrompt,
        width = this.width,
        height = this.height,
        image = this.image,
        steps = this.steps,
        guidance = this.guidance,
        scheduler = this.scheduler,
        outputFormat = this.outputFormat
    )
}