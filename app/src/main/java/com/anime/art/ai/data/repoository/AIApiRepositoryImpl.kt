package com.anime.art.ai.data.repoository

import com.anime.art.ai.domain.model.config.ImageGenerationRequest
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

        val responses = (0..3).mapNotNull {
            try {
                if(imageGenerationRequest.image.isEmpty()){
                    aiApi.generatorImageByText(imageGenerationRequest).await()
                } else {
                    aiApi.generatorImageByImage(imageGenerationRequest).await()
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