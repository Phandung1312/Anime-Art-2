package com.anime.art.ai.inject.sinkin

import com.anime.art.ai.domain.model.config.Login
import com.anime.art.ai.domain.model.response.HistoryResponse
import com.anime.art.ai.domain.model.response.LoginResponse
import com.anime.art.ai.domain.model.response.MessageResponse
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ServerApi {
    @GET("user/{deviceId}")
    fun login(@Path("deviceId") deviceId: String): Call<LoginResponse?>

    @GET("historys/{deviceId}")
    fun getCreditHistory(@Path("deviceId") deviceId: String): Call<HistoryResponse?>

    @POST("user/update/{deviceId}")
    fun updateCredit(
        @Path("deviceId") deviceId: String,
        @Body request: UpdateCreditRequest
    ): Call<MessageResponse?>
    @POST("user/update/premium/{deviceId}")
    fun updatePremium(
        @Path("deviceId") deviceId: String,
        @Body updateCreditRequest: UpdateCreditRequest) : Call<MessageResponse?>
}

data class UpdateCreditRequest(
    @SerializedName("credit") val credit: Long? = null,
    @SerializedName("title") val title: String,

)