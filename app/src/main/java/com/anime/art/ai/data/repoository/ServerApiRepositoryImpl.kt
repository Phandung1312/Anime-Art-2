package com.anime.art.ai.data.repoository


import com.anime.art.ai.data.Preferences
import com.anime.art.ai.data.db.query.HistoryDao
import com.anime.art.ai.domain.model.config.History
import com.anime.art.ai.domain.model.config.Login
import com.anime.art.ai.domain.model.response.MessageResponse
import com.anime.art.ai.domain.repository.ServerApiRepository
import com.anime.art.ai.inject.sinkin.ServerApi
import com.anime.art.ai.inject.sinkin.UpdateCreditRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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
               preferences.isSynced.set(true)
               historyDao.inserts(*historyResponse.histories.toTypedArray())
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
            val currentDateTime = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val formattedDateTime = currentDateTime.format(formatter)

            val history = History(title = request.title, credit =  request.credit!!.toLong(), deviceId = "", createdAt = formattedDateTime)
            historyDao.inserts(history)
            request.credit.let {
                val currentCredit = preferences.creditAmount.get()
                preferences.creditAmount.set(currentCredit + it)
            }
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
            preferences.isPremium.set(true)
            result.invoke(true)
        }
        catch (e : Exception){
            result.invoke(false)
        }
    }
}