package com.anime.art.ai.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.anime.art.ai.common.Constraint
import com.anime.art.ai.data.db.query.GalleryDao
import com.anime.art.ai.domain.model.config.Gallery
import com.anime.art.ai.data.db.converter.Converters

@Database(
    entities = [Gallery::class],
    version = Constraint.Info.DATA_VERSION
)
@TypeConverters(Converters::class)
abstract class Database : RoomDatabase() {

    abstract fun galleryDao(): GalleryDao

    companion object {
        const val DB_NAME = "App_database"
    }

}