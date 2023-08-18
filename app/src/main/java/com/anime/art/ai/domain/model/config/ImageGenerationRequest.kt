package com.anime.art.ai.domain.model.config

import android.support.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Keep
data class ImageGenerationRequest(
    var model: String = "dark-sushi-25d",
    var controlNet: String = "",
    var prompt: String = "",
    var negativePrompt: String = "",
    var strength: Double = 0.2,
    var image: String = "",
    var width: Int = 1024,
    var height: Int = 1024,
    var steps: Int = 25,
    var guidance: Double = 7.5,
    var scheduler: String = "UniPCMultistepScheduler",
    var artStyle : String = "Dark Shushi",
    var ratio : String = "1:1",
    var extraPrompt : String =""
)
fun ImageGenerationRequest.toTextToImage() : TextToImage{
    return TextToImage(
        modelId = this.model,
        prompt = this.prompt,
        negativePrompt = this.negativePrompt,
        width = this.width.toString(),
        height = this.height.toString(),
        numInferenceSteps = this.steps.toString(),
        guidanceScale = this.guidance,
        scheduler = this.scheduler,
        samples = "4"
    )
}

fun ImageGenerationRequest.toImageToImage() : ImageToImage{
    return ImageToImage(
        modelId = this.model,
        prompt = this.prompt,
        negativePrompt = this.negativePrompt,
        initImage = this.image,
        width = this.width.toString(),
        height = this.height.toString(),
        strength = this.strength,
        numInferenceSteps = this.steps.toString(),
        guidanceScale = this.guidance,
        scheduler = this.scheduler,
        samples = "4"
    )
}

fun ImageGenerationRequest.toControlNet() : ControlNetToImage{
    return ControlNetToImage(
        modelId = this.model,
        controlnetModel = this.controlNet,
        controlnetType = this.controlNet,
        prompt = this.prompt,
        negativePrompt = this.negativePrompt,
        width = this.width.toString(),
        height = this.height.toString(),
        initImage = this.image,
        numInferenceSteps = this.steps.toString(),
        guidanceScale = this.guidance,
        scheduler = this.scheduler,
        samples = "4"
    )
}