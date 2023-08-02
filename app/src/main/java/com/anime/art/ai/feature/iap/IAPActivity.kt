package com.anime.art.ai.feature.iap

import android.content.Intent
import android.os.Bundle
import com.anime.art.ai.R
import com.anime.art.ai.common.extension.back
import com.anime.art.ai.common.extension.gradient
import com.anime.art.ai.databinding.ActivityIapactivityBinding
import com.anime.art.ai.feature.gallery.GalleryActivity
import com.anime.art.ai.feature.main.MainActivity
import com.basic.common.base.LsActivity
import com.basic.common.extension.clicks
import com.basic.common.extension.tryOrNull
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IAPActivity : LsActivity<ActivityIapactivityBinding>(ActivityIapactivityBinding::inflate) {
    companion object{
        const val IS_FIRST_SCREEN_EXTRA = "IS_FIRST_SCREEN_EXTRA"
    }
    private val isFirstScreen by lazy { intent.getBooleanExtra(IS_FIRST_SCREEN_EXTRA, false) }
    private var iAPSelected = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
        initObservable()
        initData()
        listenerView()
    }

    private fun listenerView() {
        binding.close.clicks {
            if(isFirstScreen){
                startActivity(Intent(this, MainActivity::class.java))
                tryOrNull { overridePendingTransition(R.anim.slide_in_left, R.anim.nothing) }
                finish()
            }
            else{
                back()
            }
        }
        binding.oneWeekView.clicks {
            if(iAPSelected != 1){
                binding.ivBackgroundWeek.setImageResource(R.drawable.iap_selected)
                binding.ivBackgroundYear.setImageResource(R.drawable.iap_unselected)
                binding.tvOneWeek.setTextColor(binding.root.context.getColor(R.color.black))
                binding.tvOneYearly.setTextColor(binding.root.context.getColor(R.color.white))
                binding.tvBonusCredit.text = "Get 100 Credit"
                iAPSelected = 1
            }
        }
        binding.oneYearlyView.clicks {
            if(iAPSelected != 2){
                binding.ivBackgroundYear.setImageResource(R.drawable.iap_selected)
                binding.ivBackgroundWeek.setImageResource(R.drawable.iap_unselected)
                binding.tvOneYearly.setTextColor(binding.root.context.getColor(R.color.black))
                binding.tvOneWeek.setTextColor(binding.root.context.getColor(R.color.white))
                binding.tvBonusCredit.text = "Get 1200 Credit"
                iAPSelected = 2
            }
        }
    }

    private fun initData() {

    }

    private fun initObservable() {

    }

    private fun initView() {
        binding.tvTittle.gradient(R.color.yellow, R.color.dark_yellow)
    }
}