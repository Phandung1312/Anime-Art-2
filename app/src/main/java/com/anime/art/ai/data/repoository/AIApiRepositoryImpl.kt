package com.anime.art.ai.data.repoository

import com.anime.art.ai.domain.model.config.ImageGenerationRequest
import com.anime.art.ai.domain.model.config.toControlNet
import com.anime.art.ai.domain.model.config.toImageToImage
import com.anime.art.ai.domain.model.config.toTextToImage
import com.anime.art.ai.domain.repository.AIApiRepository
import com.anime.art.ai.inject.sinkin.AIApi
import retrofit2.await
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AIApiRepositoryImpl @Inject constructor(
    private val aiApi: AIApi
) : AIApiRepository{
    override suspend fun generateImage(
        imageGenerationRequest: ImageGenerationRequest,
        progress: (AIApiRepository.APIResponse) -> Unit
    ) {
        progress(AIApiRepository.APIResponse.Loading)

        val responses = (0..1).mapNotNull {
            try {
                if(imageGenerationRequest.image.isEmpty()){
                    aiApi.generatorImageByText(imageGenerationRequest.toTextToImage()).await()
                } else if(imageGenerationRequest.controlNet.isEmpty()){
                    aiApi.generatorImageByImage(imageGenerationRequest.toImageToImage()).await()
                } else {

                    aiApi.generatorControlNet(imageGenerationRequest.toControlNet()).await()
                }
            } catch (e: Exception){
                null
            }
        }
        when {
            responses.isNotEmpty() -> progress(AIApiRepository.APIResponse.Success(responses))
            else -> progress(AIApiRepository.APIResponse.Error)
        }
    }
}