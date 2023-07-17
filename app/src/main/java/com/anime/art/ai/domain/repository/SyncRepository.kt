package com.anime.art.ai.domain.repository

interface SyncRepository {

    sealed class Progress {
        object Running: Progress()
        object DoneGallery: Progress()
    }

    suspend fun syncData(progress: (Progress) -> Unit)

    suspend fun syncGallery(progress: (Progress) -> Unit)

    suspend fun syncGalleryLocal(progress: (Progress) -> Unit)


}