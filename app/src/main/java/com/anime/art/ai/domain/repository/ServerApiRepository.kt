package com.anime.art.ai.domain.repository

import android.content.Context
import com.anime.art.ai.domain.model.config.History
import com.anime.art.ai.domain.model.config.Login
import com.anime.art.ai.domain.model.response.MessageResponse

interface ServerApiRepository {

    suspend fun login(deviceId: String, result: (Login) -> Unit)

    suspend fun getCreditHistory(deviceId : String , result: (List<History>) -> Unit)

    suspend fun updateCredit(deviceId: String,result: (MessageResponse) -> Unit)
}