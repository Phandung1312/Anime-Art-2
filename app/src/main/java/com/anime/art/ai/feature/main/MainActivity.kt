package com.anime.art.ai.feature.main

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.anime.art.ai.databinding.ActivityMainBinding
import com.anime.art.ai.domain.repository.SyncRepository
import com.anime.art.ai.feature.Abc
import com.anime.art.ai.feature.main.create.CreateFragment
import com.anime.art.ai.feature.main.gallery.GalleryFragment
import com.anime.art.ai.feature.main.mine.MineFragment
import com.basic.common.base.LsActivity
import com.basic.common.base.LsPageAdapter
import com.basic.common.extension.clicks
import com.basic.common.extension.resolveAttrColor
import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.autoDispose
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : LsActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    @Inject lateinit var syncRepo: SyncRepository

    private val fragments by lazy { listOf(GalleryFragment(), CreateFragment(), MineFragment()) }
    private val tabClicks: Subject<Int> = BehaviorSubject.createDefault(1) // Default Tab Create
    val pageChanges: Subject<Int> = BehaviorSubject.createDefault(1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
        initObservable()
        initData()
        listenerView()
    }

    private fun listenerView() {

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

    @Deprecated("Deprecated in Java", ReplaceWith("finish()"))
    override fun onBackPressed() {
        finish()
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
//                this.gradient(context.resolveAttrColor(com.google.android.material.R.attr.colorSecondary), context.resolveAttrColor(com.google.android.material.R.attr.colorPrimary))
                this.setTextColor(context.resolveAttrColor(android.R.attr.colorAccent))
            }
            else -> {
//                this.gradient(context.resolveAttrColor(android.R.attr.textColorPrimary), context.resolveAttrColor(android.R.attr.textColorPrimary))
                this.setTextColor(context.resolveAttrColor(android.R.attr.textColorPrimary))
            }
        }
    }

}