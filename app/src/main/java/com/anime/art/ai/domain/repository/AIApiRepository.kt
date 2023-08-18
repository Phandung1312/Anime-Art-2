package com.anime.art.ai.domain.repository

import com.anime.art.ai.domain.model.config.ImageGenerationRequest
import com.anime.art.ai.domain.model.response.ImagePreview


interface AIApiRepository {
    sealed class APIResponse {
        object Loading : APIResponse()
        data class Success(val responses: List<ImagePreview>) : APIResponse()
        object Error : APIResponse()
    }
    suspend fun generateImage(imageGenerationRequest: ImageGenerationRequest, progress: (APIResponse) -> Unit)
}