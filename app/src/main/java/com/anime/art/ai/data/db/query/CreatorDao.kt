package com.anime.art.ai.data.db.query

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.anime.art.ai.domain.model.config.Creator


@Dao
interface CreatorDao {
    @Query("SELECT * FROM Creators")
    fun getAll() : LiveData<List<Creator>>

    @Insert
    fun inserts(vararg objects: Creator)

    @Query("SELECT * FROM Creators WHERE id = :id")
    fun getAllLike(id : Long): List<Creator>

}