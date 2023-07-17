package com.anime.art.ai.feature.main.gallery

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.anime.art.ai.data.db.query.GalleryDao
import com.anime.art.ai.data.repoository.SyncRepositoryImpl
import com.anime.art.ai.domain.model.config.Gallery
import com.anime.art.ai.domain.repository.SyncRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val syncRepository: SyncRepository,
    private val galleryDao: GalleryDao
) : ViewModel(){
    var progress = MutableLiveData<SyncRepository.Progress>()
        private set
    fun getLocalData() :LiveData<List<Gallery>>{
        return galleryDao.getAllLive()
    }
    fun syncData() {
        viewModelScope.launch(Dispatchers.IO) {
            syncRepository.syncData {
                progress.postValue(it)
            }
        }
    }

}