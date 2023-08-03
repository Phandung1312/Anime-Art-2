package com.anime.art.ai.data.repoository


import com.anime.art.ai.data.Preferences
import com.anime.art.ai.data.db.query.HistoryDao
import com.anime.art.ai.domain.model.config.Login
import com.anime.art.ai.domain.model.response.MessageResponse
import com.anime.art.ai.domain.repository.ServerApiRepository
import com.anime.art.ai.inject.sinkin.ServerApi
import com.anime.art.ai.inject.sinkin.UpdateCreditRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ServerApiRepositoryImpl @Inject constructor(
    private val serverApi: ServerApi,
    private val preferences: Preferences,
    private val historyDao: HistoryDao
): ServerApiRepository {

    override suspend fun login(deviceId: String ,result: (Login) -> Unit) {
        val response = serverApi.login(deviceId).await()
        if (response != null) {
            result.invoke(response.data)
        }
    }

    override suspend fun getCreditHistory(deviceId: String, result: (Boolean) -> Unit) {
       try {
           serverApi.getCreditHistory(deviceId).await()?.let { historyResponse ->
               historyDao.deleteAll()
               historyDao.inserts(*historyResponse.histories.toTypedArray())
               preferences.isSynced.set(true)
               result.invoke(true)
           }
       }
       catch (e : Exception){
           result.invoke(false)
       }
    }

    override suspend fun updateCredit(deviceId: String,request: UpdateCreditRequest, result: (Boolean) -> Unit) {
        try {
            val response = serverApi.updateCredit(deviceId, request).await()
            preferences.isSynced.set(false)
            result.invoke(true)
        }
        catch (e : Exception){
            result.invoke(false)
        }
    }

    override suspend fun updatePremium(
        deviceId: String,
        request: UpdateCreditRequest,
        result: (Boolean) -> Unit
    ) {
        try{
            val response = serverApi.updatePremium(deviceId, request).await()
            result.invoke(true)
        }
        catch (e : Exception){
            result.invoke(false)
        }
    }
}