package com.anime.art.ai.domain.model.config

import android.support.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
class History {
    @SerializedName("id")
    @Expose
    var id : Int = 0
    @SerializedName("title")
    @Expose
    var title : String = ""
    @SerializedName("credit")
    @Expose
    var credit : Long = 0
    @SerializedName("device_id")
    @Expose
    var deviceId : String = ""
    @SerializedName("created_at")
    @Expose
    var createdAt : String = ""
}