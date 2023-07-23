package com.anime.art.ai.data.db.query

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.anime.art.ai.domain.model.config.Prompt
import java.util.Objects

@Dao
interface PromptDao {
    @Query("SELECT * FROM Prompts")
    fun getAll() : List<Prompt>

    @Insert
    fun inserts(vararg objects: Prompt)
}