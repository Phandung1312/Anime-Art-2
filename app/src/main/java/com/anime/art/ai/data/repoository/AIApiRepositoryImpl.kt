package com.anime.art.ai.data.repoository

import android.util.Base64
import com.anime.art.ai.common.Constraint
import com.anime.art.ai.domain.model.config.ImageGenerationRequest
import com.anime.art.ai.domain.model.config.toControlNet
import com.anime.art.ai.domain.model.config.toImageToImage
import com.anime.art.ai.domain.model.config.toTextToImage
import com.anime.art.ai.domain.model.response.ImagePreview
import com.anime.art.ai.domain.model.response.ImageResponseProcessing
import com.anime.art.ai.domain.model.response.ImageResponseSuccess
import com.anime.art.ai.domain.repository.AIApiRepository
import com.anime.art.ai.inject.sinkin.AIApi
import com.basic.common.extension.tryOrNull
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import com.google.gson.JsonElement
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import retrofit2.await
import timber.log.Timber
import java.io.ByteArrayInputStream
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

        val firstResponse =  callApi(imageGenerationRequest, Constraint.AIGeneration.KEY2)

        val firstResponseSuccess = tryOrNull { Gson().fromJson(firstResponse.toString(), ImageResponseSuccess::class.java) }
        val firstResponseProcessing = tryOrNull { Gson().fromJson(firstResponse.toString(), ImageResponseProcessing::class.java) }
            firstResponseSuccess?.let {
                Timber.e("Call 1 success")
                val imagePreviewList = it.output.map { response -> ImagePreview(url = response, ratio = "${it.meta.w}:${it.meta.h}") }
                progress.invoke(AIApiRepository.APIResponse.Success(imagePreviewList))
                return
            }
        firstResponseProcessing?.let { firstProcessing ->
            if(firstProcessing.eta > 60f){
                val secondResponse = callApi(imageGenerationRequest, Constraint.AIGeneration.KEY3)
                val secondResponseSuccess = tryOrNull { Gson().fromJson(secondResponse.toString(), ImageResponseSuccess::class.java) }
                val secondResponseProcessing = tryOrNull { Gson().fromJson(secondResponse.toString(), ImageResponseProcessing::class.java) }
                secondResponseSuccess?.let {
                    Timber.e("Call 2 in 1 success")
                    val imagePreviewList = it.output.map { response -> ImagePreview(url = response, ratio = "${it.meta.w}:${it.meta.h}") }
                    progress.invoke(AIApiRepository.APIResponse.Success(imagePreviewList))
                    return
                }

                secondResponseProcessing?.let { secondProcessing ->
                    if(secondProcessing.eta > firstProcessing.eta){
                        Timber.e("Call 2 > 1 processing")
                        val imagePreviewList = firstProcessing.imageLinks.map { response -> ImagePreview(url = response, ratio = "${firstProcessing.meta.w}:${firstProcessing.meta.h}") }
                        progress.invoke(AIApiRepository.APIResponse.Success(imagePreviewList))
                        return
                    }
                    Timber.e("Call 1 > 2 processing")
                    val imagePreviewList = secondProcessing.imageLinks.map { response -> ImagePreview(url = response, ratio = "${secondProcessing.meta.w}:${secondProcessing.meta.h}") }
                    progress.invoke(AIApiRepository.APIResponse.Success(imagePreviewList))
                    return
                }
                Timber.e("Call Failed 2 in 1")
            }
            Timber.e("Call 1 success when 2 failed")
            delay(firstProcessing.eta.toLong())
            val imagePreviewList = firstProcessing.imageLinks.map { response -> ImagePreview(url = response, ratio = "${firstProcessing.meta.w}:${firstProcessing.meta.h}") }
            progress.invoke(AIApiRepository.APIResponse.Success(imagePreviewList))
            return
        }

        Timber.e("Call Failed 1")
        val secondResponse = callApi(imageGenerationRequest, Constraint.AIGeneration.KEY3)
        val secondResponseSuccess = tryOrNull { Gson().fromJson(secondResponse.toString(), ImageResponseSuccess::class.java) }
        val secondResponseProcessing = tryOrNull { Gson().fromJson(secondResponse.toString(), ImageResponseProcessing::class.java) }
        secondResponseSuccess?.let {
            Timber.e("Call 2 success when 1 failed")
            val imagePreviewList = it.output.map { response -> ImagePreview(url = response, ratio = "${it.meta.w}:${it.meta.h}") }
            progress.invoke(AIApiRepository.APIResponse.Success(imagePreviewList))
            return
        }

        secondResponseProcessing?.let {
            Timber.e("Call 2 processing when 1 failed")
            delay(it.eta.toLong())
            val imagePreviewList = it.imageLinks.map { response -> ImagePreview(url = response, ratio = "${it.meta.w}:${it.meta.h}") }
            progress.invoke(AIApiRepository.APIResponse.Success(imagePreviewList))
            return
        }
        Timber.e("Call 2 Failed when 1 Failed ")
        progress.invoke(AIApiRepository.APIResponse.Error)



    }

   private suspend fun callApi(imageGenerationRequest: ImageGenerationRequest, key : String) : JsonElement? =
        try {
            if(imageGenerationRequest.image.isNotEmpty()){
                val imageBytes = Base64.decode(imageGenerationRequest.image, Base64.DEFAULT)
                val fileName = "image_${System.currentTimeMillis()}.png"
                val storageRef = Firebase.storage.reference.child("images/$fileName")
                storageRef.putStream(
                    ByteArrayInputStream(imageBytes) ).await()
                val uri = storageRef.downloadUrl.await()
                imageGenerationRequest.image = uri.toString()
                if(imageGenerationRequest.controlNet.isEmpty()){
                    aiApi.generatorImageByImage(imageGenerationRequest.toImageToImage().copy(key = key)).await()
                } else {
                    aiApi.generatorControlNet(imageGenerationRequest.toControlNet().copy(key = key)).await()
                }
            }
            else{
                aiApi.generatorImageByText(imageGenerationRequest.toTextToImage().copy(key = key)).await()
            }
        }
        catch (e  :Exception){
            null
        }
}