package com.anime.art.ai.data.db.converter

import androidx.room.TypeConverter
import com.basic.common.extension.tryOrNull
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun promptsToJson(value: List<String>) = tryOrNull { Gson().toJson(value) } ?: ""

    @TypeConverter
    fun jsonToChildPrompts(value: String): List<String>{
        return when {
            value.isEmpty() -> arrayListOf()
            else -> tryOrNull { Gson().fromJson(value, object : TypeToken<List<String>>() {}.type) } ?: listOf()
        }
    }

}