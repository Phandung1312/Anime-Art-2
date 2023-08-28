package com.anime.art.ai.feature.main

import android.app.Activity
import android.app.UiModeManager
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.anime.art.ai.R
import com.anime.art.ai.common.ConfigApp
import com.anime.art.ai.common.Constraint
import com.anime.art.ai.common.extension.convertToShortDate
import com.anime.art.ai.common.extension.dayBetween
import com.anime.art.ai.common.getCurrentDay
import com.anime.art.ai.data.Preferences
import com.anime.art.ai.data.db.query.HistoryDao
import com.anime.art.ai.databinding.ActivityMainBinding
import com.anime.art.ai.domain.repository.ServerApiRepository
import com.anime.art.ai.domain.repository.SyncRepository
import com.anime.art.ai.feature.dialog.ExitDialog
import com.anime.art.ai.feature.gallery.GalleryActivity
import com.anime.art.ai.feature.main.create.CreateFragment
import com.anime.art.ai.feature.main.gallery.GalleryFragment
import com.anime.art.ai.feature.main.gallery.dialog.DailyCreditDialog
import com.anime.art.ai.feature.main.mine.MineFragment
import com.basic.common.base.LsActivity
import com.basic.common.base.LsPageAdapter
import com.basic.common.extension.clicks
import com.basic.common.extension.getDeviceId
import com.basic.common.extension.makeToast
import com.basic.common.extension.resolveAttrColor
import com.basic.common.extension.transparent
import com.basic.common.extension.tryOrNull
import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.autoDispose
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : LsActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    @Inject lateinit var pref: Preferences
    @Inject lateinit var historyDao : HistoryDao
    @Inject lateinit var syncRepo: SyncRepository
    @Inject lateinit var configApp: ConfigApp
    @Inject lateinit var serverApiRepository: ServerApiRepository
    private val fragments by lazy { listOf(GalleryFragment(), CreateFragment(), MineFragment()) }
    private val tabClicks: Subject<Int> by lazy { BehaviorSubject.createDefault(configApp.tabIndex) }
    val pageChanges: Subject<Int> by lazy { BehaviorSubject.createDefault(configApp.tabIndex) }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initObservable()
        initView()
        initData()
        listenerView()
    }

    private fun listenerView() {
        onBackPressedDispatcher.addCallback {
            val exitDialog = ExitDialog(
                "Are you sure exit ${getString(R.string.app_name)}?" ,
                    "Exit ${getString(R.string.app_name)}"
            ){
                configApp.tabIndex = 0
                finish()
            }
            exitDialog.show(supportFragmentManager, null)
        }
    }


    private val pageChangeCallback by lazy {
        object: ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                pageChanges.onNext(position)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        }
    }

    override fun onResume() {
        binding.viewPager.addOnPageChangeListener(pageChangeCallback)
        super.onResume()
    }

    override fun onPause() {
        binding.viewPager.removeOnPageChangeListener(pageChangeCallback)
        super.onPause()
    }

    private fun initData() {

    }
    private  fun showDailyReward(consecutiveSeries : Int){
        lifecycleScope.launch(Dispatchers.Main){
            val dailyCreditDialog  = DailyCreditDialog(consecutiveSeries)
            dailyCreditDialog.show(supportFragmentManager, null)
        }
    }
    private fun initObservable() {
        pref.isSynced.asObservable()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(AndroidSchedulers.mainThread())
            .autoDispose(scope())
            .subscribe { isSynced ->
                if(isSynced){
                    val histories = historyDao.getAll()
                    if(pref.isSynced.get()){
                        val newList =  histories.filter { history -> TextUtils.equals(history.title, Constraint.DAILY_REWARD)  }
                            .map { it.createdAt.convertToShortDate()}
                            .reversed()
                        if (newList.isEmpty()) {
                            showDailyReward(1)
                            return@subscribe
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
                        return@subscribe
                    }
                }
                else{
                    lifecycleScope.launch(Dispatchers.IO) {
                        serverApiRepository.getCreditHistory(getDeviceId()){ result ->
                            lifecycleScope.launch(Dispatchers.Main) {
                                if(!result){
                                    makeToast("An error has occurred")
                                }
                            }
                        }
                    }
                }
            }

        tabClicks
            .autoDispose(scope())
            .subscribe { index ->
                configApp.tabIndex = index

                binding.viewPager.currentItem = index
            }

        pageChanges
            .autoDispose(scope())
            .subscribe { index ->
                binding.imageTab1.isSelected = index == 0
                binding.imageTab2.isSelected = index == 1
                binding.imageTab3.isSelected = index == 2

                binding.textTab1.changeTextColorSelected(index == 0)
                binding.textTab2.changeTextColorSelected(index == 1)
                binding.textTab3.changeTextColorSelected(index == 2)
            }
    }

    private fun initView() {
        tabBottoms
            .forEachIndexed { index, tab ->
                tab.viewClicks.clicks { tabClicks.onNext(index) }
            }

        binding
            .viewPager
            .apply {
                this.adapter = LsPageAdapter(supportFragmentManager).apply {
                    this.addFragment(fragments = fragments.toTypedArray())
                }
                this.offscreenPageLimit = this.adapter?.count ?: 0
            }
    }


    private val tabBottoms by lazy {
        listOf(
            Tab(binding.viewTab1, binding.imageTab1, binding.textTab1),
            Tab(binding.viewTab2, binding.imageTab2, binding.textTab2),
            Tab(binding.viewTab3, binding.imageTab3, binding.textTab3),
        )
    }
    private data class Tab(val viewClicks: View, val image: AppCompatImageView, val display: TextView)
    private fun TextView.changeTextColorSelected(isSelected: Boolean){
        when {
            isSelected -> {
                this.setTextColor(context.getColor(R.color.tab_bar_selected))
            }
            else -> {
                this.setTextColor(context.getColor(R.color.tab_bar_unselected))
            }
        }
    }
    fun startDetailGallery(galleryIndex: Int){
        val intent = Intent(this, GalleryActivity::class.java)
        intent.putExtra(GalleryActivity.GALLERY_INDEX_EXTRA, galleryIndex)
        getDataFromGalleryResult.launch(intent)
        tryOrNull { overridePendingTransition(R.anim.slide_in_left, R.anim.nothing) }
    }
    private var getDataFromGalleryResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if(result.resultCode == Activity.RESULT_OK){
            val prompt = result.data?.getStringExtra(CreateFragment.PROMPT_EXTRA)
            val ratio = result.data?.getStringExtra(CreateFragment.RATIO_EXTRA)
            val negativePrompt = result.data?.getStringExtra(CreateFragment.NEGATIVE_PROMPT)
            gotoGalleryFragment(prompt, negativePrompt  ,ratio)
        }
    }
    fun gotoGalleryFragment(prompt : String?,negativePrompt : String?, ratio : String?){
        configApp.localPrompt = prompt ?: ""
        configApp.negativePrompt = negativePrompt ?: ""
        binding.viewPager.currentItem = 1
        pageChanges.onNext(1)
        configApp.tabIndex = 1
    }
}