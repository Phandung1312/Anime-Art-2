package com.anime.art.ai.data.db.query

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.anime.art.ai.domain.model.config.History

@Dao
interface HistoryDao {

    @Query("SELECT * FROM Histories")
    fun getAll() : LiveData<List<History>>

    @Insert
    fun inserts(vararg objects: History)

    @Query("DELETE FROM Histories")
    fun deleteAll()

    @Update
    fun update(vararg objects: History)
}