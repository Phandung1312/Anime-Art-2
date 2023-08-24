package com.anime.art.ai.common


import com.anime.art.ai.data.Preferences
import com.anime.art.ai.domain.model.config.ImageGenerationRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConfigApp @Inject constructor(
    prefs: Preferences
) {

    var versionGallery = prefs.versionGallery.get()
    var imageGenerationRequest: ImageGenerationRequest = ImageGenerationRequest()
    var url : String = ""
    var tabIndex = 0
    var localPrompt = ""
    var negativePrompt = Constraint.Sinkin.DEFAULT_NEGATIVE

}