package com.anime.art.ai.inject.sinkin

import com.anime.art.ai.domain.model.config.ImageGenerationRequest
import com.anime.art.ai.domain.model.response.ImageResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AIApi {
    @POST("text-to-image")
    fun generatorImage(@Body imageGenerationRequest: ImageGenerationRequest) : Call<ImageResponse?>
}