package com.anime.art.ai.domain.model.config

import android.support.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
@Entity(tableName = "Creators")
data class Creator(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var image: String = "",
    var prompt: String = "",
    var negative: String = "",
    var artStyle : String = "AGG",
    var ratio : String = "1:1"
)
