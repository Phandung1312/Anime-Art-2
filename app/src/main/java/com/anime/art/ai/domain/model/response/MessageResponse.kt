package com.anime.art.ai.domain.model.response

import android.support.annotation.Keep
import com.anime.art.ai.domain.model.config.Login
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
@Keep
data class MessageResponse(
    @SerializedName("message")
    @Expose
    var message : String = ""
)