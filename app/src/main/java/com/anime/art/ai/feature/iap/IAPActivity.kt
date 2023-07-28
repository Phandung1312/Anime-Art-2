package com.anime.art.ai.feature.iap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.anime.art.ai.R
import com.anime.art.ai.common.extension.back
import com.anime.art.ai.common.extension.gradient
import com.anime.art.ai.databinding.ActivityIapactivityBinding
import com.anime.art.ai.feature.createimage.adapter.PreviewAdapter
import com.basic.common.base.LsActivity
import com.basic.common.extension.clicks
import com.basic.common.extension.getColorCompat
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class IAPActivity : LsActivity<ActivityIapactivityBinding>(ActivityIapactivityBinding::inflate) {
    companion object{
        const val ONE_WEEK = 1
        const val ONE_YEARLY = 2
    }

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
            back()
        }
        binding.oneWeekView.clicks {
            if(iAPSelected != 1){
                binding.ivBackgroundWeek.setImageResource(R.drawable.iap_selected)
                binding.ivBackgroundYear.setImageResource(R.drawable.iap_unselected)
                binding.tvOneWeek.setTextColor(binding.root.context.getColor(R.color.black))
                binding.tvOneYearly.setTextColor(binding.root.context.getColor(R.color.white))
                iAPSelected = 1
            }
        }
        binding.oneYearlyView.clicks {
            if(iAPSelected != 2){
                binding.ivBackgroundYear.setImageResource(R.drawable.iap_selected)
                binding.ivBackgroundWeek.setImageResource(R.drawable.iap_unselected)
                binding.tvOneYearly.setTextColor(binding.root.context.getColor(R.color.black))
                binding.tvOneWeek.setTextColor(binding.root.context.getColor(R.color.white))
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