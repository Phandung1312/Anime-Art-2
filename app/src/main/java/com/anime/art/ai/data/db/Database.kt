package com.anime.art.ai.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.anime.art.ai.common.Constraint
import com.anime.art.ai.data.db.query.GalleryDao
import com.anime.art.ai.domain.model.config.Gallery
import com.anime.art.ai.data.db.converter.Converters
import com.anime.art.ai.data.db.query.CreatorDao
import com.anime.art.ai.data.db.query.HistoryDao
import com.anime.art.ai.data.db.query.PromptDao
import com.anime.art.ai.domain.model.config.Creator
import com.anime.art.ai.domain.model.config.History
import com.anime.art.ai.domain.model.config.Prompt

@Database(
    entities = [Gallery::class, Prompt::class, Creator::class, History::class],
    version = Constraint.Info.DATA_VERSION
)
@TypeConverters(Converters::class)
abstract class Database : RoomDatabase() {

    abstract fun galleryDao(): GalleryDao

    abstract fun promptDao() : PromptDao

    abstract fun creatorDao() : CreatorDao

    abstract fun historyDao() : HistoryDao
    companion object {
        const val DB_NAME = "App_database"
    }

}