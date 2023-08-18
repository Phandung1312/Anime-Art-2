package com.anime.art.ai.data.repoository

import android.content.Context
import com.anime.art.ai.common.ConfigApp
import com.anime.art.ai.data.Preferences
import com.anime.art.ai.data.db.query.GalleryDao
import com.anime.art.ai.domain.model.config.Gallery
import com.anime.art.ai.domain.repository.SyncRepository
import com.basic.common.extension.tryOrNull
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncRepositoryImpl @Inject constructor(
    private val context: Context,
    private val configApp: ConfigApp,
    private val prefs: Preferences,
    private val galleryDao: GalleryDao
): SyncRepository {

    override suspend fun syncData(progress: (SyncRepository.Progress) -> Unit) {
        progress(SyncRepository.Progress.Running)

        if (prefs.versionGallery.get() < configApp.versionGallery || galleryDao.getAll().isEmpty()) {
            syncGallery(progress)
        } else {
            delay(500)

            progress(SyncRepository.Progress.DoneGallery)
        }
    }

    override suspend fun syncGallery(progress: (SyncRepository.Progress) -> Unit) {
        Timber.e("##### SYNC GALLERY #####")

        try {
            val snapshot = Firebase
                .database
                .reference
                .child("v2/gallery")
                .get()
                .await()

            val genericTypeIndicator = object : GenericTypeIndicator<List<Gallery>>() {}
            val galleries = tryOrNull { snapshot.getValue(genericTypeIndicator) } ?: emptyList()

            when {
                galleries.isNotEmpty() -> {
                    galleries.forEach { gallery ->
                        gallery.ratio = tryOrNull { gallery.preview.split("zzz").getOrNull(1)?.let { tryOrNull { it.substring(1, it.lastIndex) } }?.replace("__",":") } ?: "1:1"
                    }

                    galleryDao.deleteAll()
                    galleryDao.inserts(*galleries.toTypedArray())
                }
                else -> syncGalleryLocal(progress)
            }

            prefs.versionGallery.set(configApp.versionGallery)

            progress(SyncRepository.Progress.DoneGallery)
        } catch (e: Exception){
            syncGalleryLocal(progress)
        }
    }

    override suspend fun syncGalleryLocal(progress: (SyncRepository.Progress) -> Unit) {
        val inputStream = context.assets.open("gallery.json")
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val data = tryOrNull { Gson().fromJson(bufferedReader, Array<Gallery>::class.java) } ?: arrayOf()

        data.forEach { item ->
            item.ratio = tryOrNull { item.preview.split("zzz").getOrNull(1)?.let { tryOrNull { it.substring(1, it.lastIndex) } }?.replace("__",":") } ?: "1:1"
        }

        galleryDao.deleteAll()
        galleryDao.inserts(*data)

        progress(SyncRepository.Progress.DoneGallery)
    }




}