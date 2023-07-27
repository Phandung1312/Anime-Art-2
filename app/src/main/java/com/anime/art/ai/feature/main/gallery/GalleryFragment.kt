package com.anime.art.ai.feature.main.gallery

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import com.anime.art.ai.common.extension.observeOnce
import com.anime.art.ai.common.extension.startDetailGallery
import com.anime.art.ai.data.Preferences
import com.anime.art.ai.data.db.query.GalleryDao
import com.anime.art.ai.databinding.FragmentGalleryBinding
import com.anime.art.ai.domain.repository.SyncRepository
import com.anime.art.ai.feature.main.MainActivity
import com.anime.art.ai.feature.main.gallery.adapter.PreviewAdapter
import com.anime.art.ai.feature.main.gallery.dialog.DailyCreditDialog
import com.basic.common.base.LsFragment
import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.autoDispose
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@AndroidEntryPoint
class GalleryFragment: LsFragment<FragmentGalleryBinding>(FragmentGalleryBinding::inflate) {
    @Inject lateinit var pref: Preferences
    @Inject lateinit var previewAdapter: PreviewAdapter
    @Inject lateinit var galleryDao: GalleryDao
    @Inject lateinit var syncRepo: SyncRepository

    private var isToggleFavourite = false
    private var isSyncedData = false

    override fun onViewCreated() {
        initView()
        initData()
    }

    override fun onResume() {
        initObservable()
        super.onResume()
    }

    private fun initObservable() {
        previewAdapter
            .clicks
            .autoDispose(scope())
            .subscribe { galleryIndex -> activity?.startDetailGallery(galleryIndex = galleryIndex) }

        previewAdapter
            .toggleFavouriteClicks
            .autoDispose(scope())
            .subscribe { gallery ->
                Timber.e("Gallery is favourite: ${gallery.favourite}")
                galleryDao.update(gallery)

                isToggleFavourite = true
            }


        (activity as? MainActivity)
            ?.pageChanges
            ?.filter { index -> index == 0 && !isSyncedData }
            ?.take(1)
            ?.autoDispose(scope())
            ?.subscribe { syncData() }
    }

    private fun initData() {
        galleryDao.getAllLikeLive().observe(viewLifecycleOwner){ galleries ->
            if (!isToggleFavourite){
                previewAdapter.data = galleries
            }

            isToggleFavourite = false
        }
        checkDailyCreditReceived()

    }
    private fun checkDailyCreditReceived(){
        val time = pref.timeGetDailyCredit.get()
        if(time.isNotEmpty()){
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val dateTime = LocalDateTime.parse(time, formatter)
            val currentDateTime = LocalDateTime.now()
            val minutesDiff = ChronoUnit.MINUTES.between(dateTime, currentDateTime)
            if (minutesDiff > 5 ) return
        }
        val dailyCreditDialog  = DailyCreditDialog()
        dailyCreditDialog.show(parentFragmentManager, null)
    }
    private fun initView() {
        binding.recyclerView.apply {
            this.adapter = previewAdapter
            this.itemAnimator = null
        }

    }

    private fun syncData(){
        Timber.e("##### SYNC DATA #####")

        lifecycleScope.launch(Dispatchers.IO) {
            syncRepo.syncData { progress ->
                launch(Dispatchers.Main) {
                    val isRunning = progress is SyncRepository.Progress.Running
                    val isDone = progress is SyncRepository.Progress.DoneGallery

                    if (isRunning){
                        binding.lottieView.playAnimation()
                    }

                    if (isDone){
                        isSyncedData = true
                    }

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