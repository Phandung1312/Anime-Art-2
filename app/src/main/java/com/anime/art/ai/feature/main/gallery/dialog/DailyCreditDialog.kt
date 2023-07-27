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
import com.basic.common.extension.getDimens
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
        binding.receiveCardView.clicks(withAnim = false) {
            binding.receiveCardView.strokeWidth = requireContext().getDimens(com.intuit.sdp.R.dimen._1sdp).toInt()
            binding.receiveLayout.setBackgroundColor(requireContext().getColor(R.color.gray_3D))
            val currentDay = pref.numOfDayReceivedCredit.get()
            pref.numOfDayReceivedCredit.set( currentDay + 1 )
            setDayReward(pref.numOfDayReceivedCredit.get())
        }
    }
    private fun setDayReward(day : Int){
        val imageResourceId = requireContext().resources.getIdentifier("_${day}_tick","drawable",binding.root.context.packageName)
        binding.ivCreditSlider.setImageResource(imageResourceId)
        binding.rv.adapter = dailyRewardAdapter
        dailyRewardAdapter.data = DailyReward.values().take(day).toList()
    }
    private fun initView() {
        binding.rv.adapter = dailyRewardAdapter
        setDayReward(pref.numOfDayReceivedCredit.get())
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog?.window?.setBackgroundDrawableResource(R.color.transparent)
    }
}