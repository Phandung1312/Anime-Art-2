package com.anime.art.ai.inject.sinkin

import com.anime.art.ai.domain.model.config.ControlNetImage
import com.anime.art.ai.domain.model.config.ImageToImage
import com.anime.art.ai.domain.model.config.TextToImage
import com.anime.art.ai.domain.model.response.ImageResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AIApi {
    @POST("text-to-image")
    fun generatorImageByText(@Body textToImage: TextToImage) : Call<ImageResponse?>

    @POST("image-to-image")
    fun generatorImageByImage(@Body imageToImage: ImageToImage) : Call<ImageResponse?>

    @POST("controlnet")
    fun generatorControlNet(@Body controlNetImage: ControlNetImage) : Call<ImageResponse?>
}