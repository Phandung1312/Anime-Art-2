package com.anime.art.ai.data.repoository

import android.media.Image
import com.anime.art.ai.domain.model.config.ImageGenerationRequest
import com.anime.art.ai.domain.model.config.Login
import com.anime.art.ai.domain.model.response.ImageResponse
import com.anime.art.ai.domain.model.response.LoginResponse
import com.anime.art.ai.domain.repository.AIApiRepository
import com.anime.art.ai.inject.sinkin.AIApi
import retrofit2.Call
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton
import javax.security.auth.callback.Callback

@Singleton
class AIApiRepositoryImpl @Inject constructor(
    private val aiApi: AIApi
) : AIApiRepository{
    override suspend fun generateImage(
        imageGenerationRequest: ImageGenerationRequest,
        result: (ImageResponse) -> Unit
    ) {
        val response = aiApi.generatorImage(imageGenerationRequest)

        response.enqueue(object : retrofit2.Callback<ImageResponse?> {
            override fun onResponse(
                call: Call<ImageResponse?>,
                response: Response<ImageResponse?>
            ) {
                if(response.isSuccessful){
                    response.body()?.let { imageResponse ->
                        result.invoke(imageResponse)
                    }
                }
                else{
                    Timber.e("IsNotSuccessful")
                }
            }
            override fun onFailure(call: Call<ImageResponse?>, t: Throwable) {
                Timber.e("OnFailure")
            }


        })
    }
}