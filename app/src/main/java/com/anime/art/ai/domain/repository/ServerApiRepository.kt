package com.anime.art.ai.domain.repository

import com.anime.art.ai.domain.model.config.History
import com.anime.art.ai.domain.model.config.Login
import com.anime.art.ai.inject.sinkin.UpdateCreditRequest

interface ServerApiRepository {
    sealed class ServerApiResponse {
        object Loading : ServerApiResponse()
        data class Success(val response : List<History>) : ServerApiResponse()
    }
    suspend fun login(deviceId: String, result: (Login) -> Unit)

    suspend fun getCreditHistory(deviceId : String , progress: (ServerApiResponse) -> Unit)

    suspend fun updateCredit(deviceId: String, request: UpdateCreditRequest, result: (Boolean) -> Unit)
}