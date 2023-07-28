package com.anime.art.ai.inject.sinkin

import com.anime.art.ai.domain.model.config.Login
import com.anime.art.ai.domain.model.response.HistoryResponse
import com.anime.art.ai.domain.model.response.LoginResponse
import com.anime.art.ai.domain.model.response.MessageResponse
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ServerApi {
    @GET("user/{deviceId}")
    fun login(@Path("deviceId") deviceId : String) : Call<LoginResponse?>

    @GET("historys/{deviceId}")
    fun getCreditHistory(@Path("deviceId") deviceId : String) : Call<HistoryResponse?>

    @POST("user/update/")
    fun updateCredit(@Path("deviceId") deviceId: String): Call<MessageResponse?>
}