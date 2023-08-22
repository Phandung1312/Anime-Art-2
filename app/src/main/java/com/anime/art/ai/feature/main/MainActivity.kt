package com.anime.art.ai.feature.main

import android.app.Activity
import android.app.UiModeManager
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatImageView
import androidx.viewpager.widget.ViewPager
import com.anime.art.ai.R
import com.anime.art.ai.common.ConfigApp
import com.anime.art.ai.databinding.ActivityMainBinding
import com.anime.art.ai.domain.repository.SyncRepository
import com.anime.art.ai.feature.dialog.ExitDialog
import com.anime.art.ai.feature.gallery.GalleryActivity
import com.anime.art.ai.feature.main.create.CreateFragment
import com.anime.art.ai.feature.main.gallery.GalleryFragment
import com.anime.art.ai.feature.main.mine.MineFragment
import com.basic.common.base.LsActivity
import com.basic.common.base.LsPageAdapter
import com.basic.common.extension.clicks
import com.basic.common.extension.resolveAttrColor
import com.basic.common.extension.tryOrNull
import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.autoDispose
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : LsActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    @Inject lateinit var syncRepo: SyncRepository
    @Inject lateinit var configApp: ConfigApp

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

    private fun initObservable() {
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
                this.setTextColor(context.resolveAttrColor(android.R.attr.colorAccent))
            }
            else -> {
                this.setTextColor(context.resolveAttrColor(android.R.attr.textColorPrimary))
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
        (this.fragments.getOrNull(1) as CreateFragment ).setDataFromGallery(prompt, negativePrompt , ratio)
        binding.viewPager.currentItem = 1
        pageChanges.onNext(1)
        configApp.tabIndex = 1
    }
}