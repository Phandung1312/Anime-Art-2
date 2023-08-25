package com.anime.art.ai.feature.main.gallery

import android.text.TextUtils
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.anime.art.ai.R
import com.anime.art.ai.common.Constraint
import com.anime.art.ai.common.extension.convertToShortDate
import com.anime.art.ai.common.extension.dayBetween
import com.anime.art.ai.common.extension.gradient
import com.anime.art.ai.common.extension.startIAP
import com.anime.art.ai.common.getCurrentDay
import com.anime.art.ai.data.Preferences
import com.anime.art.ai.data.db.query.GalleryDao
import com.anime.art.ai.data.db.query.HistoryDao
import com.anime.art.ai.databinding.FragmentGalleryBinding
import com.anime.art.ai.domain.repository.ServerApiRepository
import com.anime.art.ai.domain.repository.SyncRepository
import com.anime.art.ai.feature.main.MainActivity
import com.anime.art.ai.feature.main.gallery.adapter.PreviewAdapter
import com.anime.art.ai.feature.main.gallery.dialog.DailyCreditDialog
import com.basic.common.base.LsFragment
import com.basic.common.extension.clicks
import com.basic.common.extension.getDeviceId
import com.basic.common.extension.makeToast
import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.autoDispose
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class GalleryFragment: LsFragment<FragmentGalleryBinding>(FragmentGalleryBinding::inflate) {
    @Inject lateinit var pref: Preferences
    @Inject lateinit var previewAdapter: PreviewAdapter
    @Inject lateinit var galleryDao: GalleryDao
    @Inject lateinit var historyDao : HistoryDao
    @Inject lateinit var syncRepo: SyncRepository
    @Inject lateinit var serverApiRepository: ServerApiRepository
    private var isToggleFavourite = false
    private var isSyncedData = false

    override fun onViewCreated() {
        initView()
        initData()
        initListener()
    }

    private fun initListener() {
       binding.premiumView.clicks {
           requireActivity().startIAP()
       }
    }

    override fun onResume() {
        initObservable()
        super.onResume()
    }

    private fun initObservable() {
        previewAdapter
            .clicks
            .autoDispose(scope())
            .subscribe { galleryIndex -> (activity as MainActivity).startDetailGallery(galleryIndex = galleryIndex) }

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

        pref
            .isPremium
            .asObservable()
            .autoDispose(scope())
            .subscribe {
                binding.premiumView.isVisible = !it
            }
    }

    private fun initData() {
        checkDailyCreditReceived()
        galleryDao.getAllLikeLive().observe(viewLifecycleOwner){ galleries ->
            if (!isToggleFavourite){
                previewAdapter.data = galleries
            }
            isToggleFavourite = false
        }
    }
    private fun checkDailyCreditReceived(){
        pref.isSynced.asObservable().autoDispose(scope()).subscribe {  isSynced ->
            if(isSynced){
                historyDao.getAll().observe(viewLifecycleOwner){ histories ->
                    if(pref.isSynced.get()){
                        val newList =  histories.filter { history -> TextUtils.equals(history.title, Constraint.DAILY_REWARD)  }
                            .map { it.createdAt.convertToShortDate()}
                            .reversed()
                        if (newList.isEmpty()) {
                            showDailyReward(1)
                            return@observe
                        }
                        var consecutiveSeries  = 1
                        if(newList.size > 1){
                            for(i in 0 ..   newList.size -2){
                                if(newList[i].dayBetween(newList[i + 1]) > 1L) break
                                consecutiveSeries += 1
                                if(consecutiveSeries > 7){
                                    consecutiveSeries = 7
                                    break
                                }
                            }
                        }
                        val currentDay = getCurrentDay()
                        if(currentDay.dayBetween(newList[0]) == 1L && newList.size == 1) {
                            showDailyReward(2)
                        }
                        else if(currentDay.dayBetween(newList[0]) == 1L){
                            showDailyReward(if(consecutiveSeries > 6) 1 else consecutiveSeries + 1)
                        }
                        else if(currentDay.dayBetween(newList[0]) > 1L) showDailyReward(1)
                        return@observe
                    }
                }
            }
            else{
                lifecycleScope.launch(Dispatchers.IO) {
                    serverApiRepository.getCreditHistory(requireContext().getDeviceId()){ result ->
                        lifecycleScope.launch(Dispatchers.Main) {
                            if(!result){
                                requireContext().makeToast("An error has occurred")
                            }
                        }
                    }
                }
            }
        }



    }
    private  fun showDailyReward(consecutiveSeries : Int){
        lifecycleScope.launch(Dispatchers.Main){
            val dailyCreditDialog  = DailyCreditDialog(consecutiveSeries)
            dailyCreditDialog.show(parentFragmentManager, null)
        }
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