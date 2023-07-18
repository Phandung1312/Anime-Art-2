package com.anime.art.ai.inject.sinkin

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

interface SinkinApi {

    @Multipart
    @POST("")
    @Streaming
    suspend fun text2image(
        @Part("prompt") prompt: RequestBody,
        @Part("negative_prompt") negativePrompt: RequestBody,
        @Part("guidance") guidance: RequestBody,
        @Part("upscale") upscale: RequestBody,
        @Part("sampler") sampler: RequestBody,
        @Part("steps") steps: RequestBody,
        @Part("model") model: RequestBody,
        @Part("width") width: RequestBody,
        @Part("height") height: RequestBody,
        @Part("seed") seed: RequestBody?
    ): ResponseBody

}