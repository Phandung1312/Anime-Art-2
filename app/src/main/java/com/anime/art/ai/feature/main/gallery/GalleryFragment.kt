package com.anime.art.ai.feature.main.gallery

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.anime.art.ai.data.db.query.GalleryDao
import com.anime.art.ai.databinding.FragmentGalleryBinding
import com.anime.art.ai.domain.repository.SyncRepository
import com.anime.art.ai.feature.main.gallery.adapter.PreviewAdapter
import com.basic.common.base.LsFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class GalleryFragment: LsFragment<FragmentGalleryBinding>(FragmentGalleryBinding::inflate) {

    private val galleryViewModel by viewModels<GalleryViewModel>()
    @Inject lateinit var previewAdapter: PreviewAdapter
    @Inject lateinit var galleryDao: GalleryDao
    @Inject lateinit var syncRepo: SyncRepository

    override fun onViewCreated() {
        initView()
        initData()

    }

    private fun initData() {
        galleryDao.getAllLive().observe(viewLifecycleOwner){ galleries ->
            previewAdapter.data = galleries
        }

    }

    private fun initView() {
        binding.recyclerView.adapter = previewAdapter
    }

    fun syncData(){
        Timber.e("##### SYNC DATA #####")

        lifecycleScope.launch(Dispatchers.IO) {
            syncRepo.syncData { progress ->
                launch(Dispatchers.Main) {
                    binding.lottieView
                        .animate()
                        .alpha(if (progress is SyncRepository.Progress.Running) 1f else 0f)
                        .setDuration(1000)
                        .start()

                    binding.recyclerView
                        .animate()
                        .alpha(if (progress is SyncRepository.Progress.DoneGallery) 1f else 0f)
                        .setDuration(1000)
                        .start()
                }
            }
        }
    }

}