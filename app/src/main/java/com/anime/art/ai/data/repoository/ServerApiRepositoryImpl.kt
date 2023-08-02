package com.anime.art.ai.data.repoository


import com.anime.art.ai.domain.model.config.Login
import com.anime.art.ai.domain.model.response.LoginResponse
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
    private val serverApi: ServerApi
): ServerApiRepository {

    override suspend fun login(deviceId: String ,result: (Login) -> Unit) {
        val response = serverApi.login(deviceId).await()
        if (response != null) {
            result.invoke(response.data)
        }
    }

    override suspend fun getCreditHistory(deviceId: String, progress: (ServerApiRepository.ServerApiResponse) -> Unit) {
        progress(ServerApiRepository.ServerApiResponse.Loading)
        serverApi.getCreditHistory(deviceId).await()?.let { historyResponse -> progress(ServerApiRepository.ServerApiResponse.Success(historyResponse.histories)) }
    }

    override suspend fun updateCredit(deviceId: String,request: UpdateCreditRequest, result: (Boolean) -> Unit) {
        val response = serverApi.updateCredit(deviceId, request)
        response.enqueue(object  : Callback<MessageResponse?>{
            override fun onResponse(
                call: Call<MessageResponse?>,
                response: Response<MessageResponse?>
            ) {
                if(response.isSuccessful){
                    response.body()?.let {
                        result.invoke(true)
                    }
                }
            }

            override fun onFailure(call: Call<MessageResponse?>, t: Throwable) {

            }

        })
    }
}