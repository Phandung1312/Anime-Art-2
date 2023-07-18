package com.anime.art.ai.feature.main.gallery

import androidx.lifecycle.lifecycleScope
import com.anime.art.ai.data.db.query.GalleryDao
import com.anime.art.ai.databinding.FragmentGalleryBinding
import com.anime.art.ai.domain.repository.SyncRepository
import com.anime.art.ai.feature.main.MainActivity
import com.anime.art.ai.feature.main.gallery.adapter.PreviewAdapter
import com.basic.common.base.LsFragment
import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.autoDispose
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class GalleryFragment: LsFragment<FragmentGalleryBinding>(FragmentGalleryBinding::inflate) {

    @Inject lateinit var previewAdapter: PreviewAdapter
    @Inject lateinit var galleryDao: GalleryDao
    @Inject lateinit var syncRepo: SyncRepository

    override fun onViewCreated() {
        initView()
        initObservable()
        initData()
    }

    private fun initObservable() {
        (activity as? MainActivity)
            ?.pageChanges
            ?.filter { index -> index == 0 }
            ?.take(1)
            ?.autoDispose(scope())
            ?.subscribe {
                syncData()
            }
    }

    private fun initData() {
        galleryDao.getAllLive().observe(viewLifecycleOwner){ galleries ->
            previewAdapter.data = galleries
        }
    }

    private fun initView() {
        binding.recyclerView.adapter = previewAdapter
    }

    private fun syncData(){
        Timber.e("##### SYNC DATA #####")

        lifecycleScope.launch(Dispatchers.IO) {
            syncRepo.syncData { progress ->
                launch(Dispatchers.Main) {
                    val isRunning = progress is SyncRepository.Progress.Running
                    val isDone = progress is SyncRepository.Progress.DoneGallery

                    binding.lottieView
                        .animate()
                        .alpha(if (isRunning) 1f else 0f)
                        .setDuration(1000)
                        .start()

                    binding.recyclerView
                        .animate()
                        .alpha(if (isDone) 1f else 0f)
                        .setDuration(1000)
                        .start()
                }
            }
        }
    }

}