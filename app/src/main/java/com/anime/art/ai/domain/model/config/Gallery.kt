package com.anime.art.ai.domain.model.config

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
@Entity(tableName = "Galleries")
class Gallery {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    @SerializedName("avatar")
    @Expose
    var avatar: String = ""
    @SerializedName("display")
    @Expose
    var display: String = ""
    @SerializedName("preview")
    @Expose
    var preview: String = ""
    @SerializedName("prompt")
    @Expose
    var prompt: String = ""
    @SerializedName("negative")
    @Expose
    var negative: String = ""
    @SerializedName("ratio")
    @Expose
    var ratio: String = "1:1"
    @SerializedName("favourite")
    @Expose
    var favourite: Boolean = false
}