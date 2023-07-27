package com.anime.art.ai.feature.main.gallery.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.anime.art.ai.R
import com.anime.art.ai.data.Preferences
import com.anime.art.ai.databinding.DialogBuyMoreBinding
import com.anime.art.ai.databinding.DialogDailyCreditBinding
import com.anime.art.ai.domain.model.DailyReward
import com.anime.art.ai.feature.main.gallery.adapter.DailyRewardAdapter
import com.basic.common.extension.clicks
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DailyCreditDialog : DialogFragment() {
    private lateinit var  binding : DialogDailyCreditBinding
    @Inject lateinit var pref : Preferences
    @Inject lateinit var dailyRewardAdapter: DailyRewardAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogDailyCreditBinding.inflate(inflater, container, false)
        initView()
        initListener()
        return binding.root
    }

    private fun initListener() {
        binding.receiveView.clicks(withAnim = false) {

        }
    }

    private fun initView() {
        val imageResourceId = requireContext().resources.getIdentifier("_${pref.numOfDayReceivedCredit.get()}_tick","drawable",binding.root.context.packageName)
        binding.ivCreditSlider.setImageResource(imageResourceId)
        binding.rv.adapter = dailyRewardAdapter
        dailyRewardAdapter.data = DailyReward.values().take(pref.numOfDayReceivedCredit.get()).toList()

    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog?.window?.setBackgroundDrawableResource(R.color.transparent)
    }
}