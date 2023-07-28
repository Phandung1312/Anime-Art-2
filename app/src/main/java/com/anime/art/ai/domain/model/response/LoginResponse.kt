package com.anime.art.ai.domain.model.response

import com.anime.art.ai.domain.model.config.History
import com.anime.art.ai.domain.model.config.Login
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("data")
    @Expose
    var data : Login = Login(),
    @SerializedName("message")
    @Expose
    var message : String = ""
)