package com.anime.art.ai.data.db.query

import androidx.lifecycle.LiveData
import androidx.room.*
import com.anime.art.ai.domain.model.config.Gallery

@Dao
interface GalleryDao {

    // Query

    @Query("SELECT * FROM Galleries")
    fun getAll(): List<Gallery>

    @Query("SELECT * FROM Galleries WHERE dislike = :dislike")
    fun getAllLike(dislike: Boolean = false): List<Gallery>

    @Query("SELECT * FROM Galleries")
    fun getAllLive(): LiveData<List<Gallery>>

    @Query("SELECT * FROM Galleries WHERE dislike = :dislike")
    fun getAllLikeLive(dislike: Boolean = false): LiveData<List<Gallery>>

    // Inserts or deletes

    @Insert
    fun inserts(vararg objects: Gallery): List<Long>

    @Query("DELETE FROM Galleries")
    fun deleteAll()


    // Update

    @Update
    fun update(vararg objects: Gallery)

    // Find

    @Query("SELECT * FROM Galleries WHERE id = :id LIMIT 1")
    fun findById(id: Long): Gallery?

}