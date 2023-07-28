package com.anime.art.ai.domain.model.response

import android.support.annotation.Keep
import com.anime.art.ai.domain.model.config.History
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class HistoryResponse (
    @SerializedName("histories")
    @Expose
    var histories : List<History> = arrayListOf(),
    @SerializedName("message")
    @Expose
    var message : String = ""
)