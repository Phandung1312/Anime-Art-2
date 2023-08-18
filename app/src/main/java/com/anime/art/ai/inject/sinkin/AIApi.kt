package com.anime.art.ai.inject.sinkin

import com.anime.art.ai.domain.model.config.ControlNetToImage
import com.anime.art.ai.domain.model.config.ImageToImage
import com.anime.art.ai.domain.model.config.TextToImage
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AIApi {
    @POST("text2img")
    fun generatorImageByText(@Body textToImage: TextToImage) : Call<JsonElement?>

    @POST("img2img")
    fun generatorImageByImage(@Body imageToImage: ImageToImage) : Call<JsonElement?>

    @POST("controlnet")
    fun generatorControlNet(@Body controlNetImage: ControlNetToImage) : Call<JsonElement?>
}