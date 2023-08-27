package com.anime.art.ai.feature.main.gallery.dialog

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.anime.art.ai.R
import com.anime.art.ai.common.Constraint
import com.anime.art.ai.common.extension.gradient
import com.anime.art.ai.data.Preferences
import com.anime.art.ai.databinding.DialogDailyCreditBinding
import com.anime.art.ai.domain.model.DailyReward
import com.anime.art.ai.domain.repository.ServerApiRepository
import com.anime.art.ai.feature.main.gallery.adapter.DailyRewardAdapter
import com.anime.art.ai.inject.sinkin.UpdateCreditRequest
import com.basic.common.extension.clicks
import com.basic.common.extension.getDeviceId
import com.basic.common.extension.getDimens
import com.basic.common.extension.makeToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DailyCreditDialog(
    private val consecutiveSeries : Int
) : DialogFragment() {
    private lateinit var  binding : DialogDailyCreditBinding
    @Inject lateinit var pref : Preferences
    @Inject lateinit var dailyRewardAdapter: DailyRewardAdapter
    @Inject lateinit var serverApiRepository: ServerApiRepository

    private var isPreSet = false
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
    private fun setGradientReceivedDay(day : Int){
        val listText : List<TextView> = listOf(
            binding.tvDay1,
            binding.tvDay2,
            binding.tvDay3,
            binding.tvDay4,
            binding.tvDay5,
            binding.tvDay6,
            binding.tvDay7
        )
        if(!isPreSet){
            listText.take(day).forEach {  tv ->
                tv.gradient(R.color.colorSecondary, R.color.colorPrimary)
            }
            isPreSet = true
        }
        else {
            listText[day - 1].gradient(R.color.colorSecondary, R.color.colorPrimary)
            listText[day - 1].requestLayout()
        }

    }
    private fun initListener() {
        binding.receiveCardView.clicks(withAnim = false) {
            binding.receiveCardView.isEnabled = false
            lifecycleScope.launch(Dispatchers.IO) {
                val todayReward = DailyReward.values().take(consecutiveSeries).last().reward.toLong()
                val request = UpdateCreditRequest(todayReward, Constraint.DAILY_REWARD)
                serverApiRepository.updateCredit(requireContext().getDeviceId(),request){ result ->
                    lifecycleScope.launch(Dispatchers.Main) {
                        if(result){
                            setDayReward( consecutiveSeries, true )
                            requireContext().makeToast("You have received $todayReward credit")
                            delay(300)
                            dismiss()
                        }
                        else{
                            requireContext().makeToast("An error has occurred")
                            binding.receiveCardView.isEnabled = true
                        }
                    }
                }
            }
        }
    }
    @SuppressLint("DiscouragedApi")
    private fun setDayReward(day : Int, isReceived : Boolean){
        setGradientReceivedDay(day)
        val imageResourceId = if(isReceived) resources.getIdentifier("_${day}_tick","drawable",binding.root.context.packageName)
        else resources.getIdentifier("_${day}_tick_not_received","drawable",binding.root.context.packageName)
        binding.ivCreditSlider.setImageResource(imageResourceId)
        dailyRewardAdapter.data = DailyReward.values().take(day).toList()
        binding.receiveCardView.apply {
            isEnabled = !isReceived
            strokeWidth = if(isReceived) requireContext().getDimens(com.intuit.sdp.R.dimen._1sdp).toInt() else 0
        }
        binding.receiveLayout.apply {
            if(isReceived) setBackgroundColor(requireContext().getColor(R.color.background_received))
            else setBackgroundResource(R.drawable.button_gradient_yellow)
        }
        binding.tvReceive.setTextColor(if(isReceived) requireContext().getColor(R.color.white_35) else requireContext().getColor(R.color.textButton))
    }
    private fun initView() {
        binding.rv.adapter = dailyRewardAdapter
        setDayReward(consecutiveSeries, false)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog?.window?.setBackgroundDrawableResource(R.color.transparent)
    }
}