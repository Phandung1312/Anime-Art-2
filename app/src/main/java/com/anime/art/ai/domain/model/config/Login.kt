package com.anime.art.ai.domain.model.config

import android.support.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class Login(
    @SerializedName("device_id")
    @Expose
    var id : Long = 0,
    @SerializedName("is_premium")
    @Expose
    var title : Int = 0,
    @SerializedName("is_premium")
    @Expose
    var credit : Long = 0,
    @SerializedName("is_premium")
    @Expose
    var totalImages : Int = 0,
    @SerializedName("is_premium")
    @Expose
    var totalImagesDay  : Int = 0,
    @SerializedName("is_premium")
    @Expose
    var createAt : String = "",
    @SerializedName("is_premium")
    @Expose
    var updateAt : String = "",
    )
