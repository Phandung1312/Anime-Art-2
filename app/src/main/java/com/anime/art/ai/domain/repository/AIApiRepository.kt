package com.anime.art.ai.domain.repository

import com.anime.art.ai.domain.model.config.ImageGenerationRequest
import com.anime.art.ai.domain.model.config.Login
import com.anime.art.ai.domain.model.response.ImageResponse

interface AIApiRepository {
    suspend fun generateImage(imageGenerationRequest: ImageGenerationRequest, result: (ImageResponse) -> Unit)
}