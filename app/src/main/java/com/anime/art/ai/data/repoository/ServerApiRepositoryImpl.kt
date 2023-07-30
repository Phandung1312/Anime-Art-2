package com.anime.art.ai.data.repoository


import com.anime.art.ai.domain.model.config.History
import com.anime.art.ai.domain.model.config.Login
import com.anime.art.ai.domain.model.response.HistoryResponse
import com.anime.art.ai.domain.model.response.LoginResponse
import com.anime.art.ai.domain.model.response.MessageResponse
import com.anime.art.ai.domain.repository.ServerApiRepository
import com.anime.art.ai.inject.sinkin.ServerApi
import com.anime.art.ai.inject.sinkin.UpdateCreditRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ServerApiRepositoryImpl @Inject constructor(
    private val serverApi: ServerApi
): ServerApiRepository {

    override suspend fun login(deviceId: String ,result: (Login) -> Unit) {
        val response = serverApi.login(deviceId)

        response.enqueue(object : Callback<LoginResponse?>{
            override fun onResponse(
                call: Call<LoginResponse?>,
                response: Response<LoginResponse?>
            ) {
                if(response.isSuccessful){
                    response.body()?.let { loginResponse ->
                        result.invoke(loginResponse.data)
                    }
                }
                else{

                }
            }

            override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {

            }

        })
    }

    override suspend fun getCreditHistory(deviceId: String, result: (List<History>) -> Unit) {
        val response = serverApi.getCreditHistory(deviceId)
        response.enqueue(object : Callback<HistoryResponse?>{
            override fun onResponse(
                call: Call<HistoryResponse?>,
                response: Response<HistoryResponse?>
            ) {
                if(response.isSuccessful){
                    response.body()?.let {historyResponse ->
                        result(historyResponse.histories)
                    }
                }
                else{

                }
            }

            override fun onFailure(call: Call<HistoryResponse?>, t: Throwable) {

            }

        })
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