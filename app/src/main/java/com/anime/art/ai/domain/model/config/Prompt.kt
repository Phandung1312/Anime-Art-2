package com.anime.art.ai.domain.model.config

import android.support.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "Prompts")
data class Prompt(
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0,
    var text : String
)