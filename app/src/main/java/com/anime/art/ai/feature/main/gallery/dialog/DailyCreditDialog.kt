package com.anime.art.ai.feature.main.gallery.dialog

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
import com.basic.common.extension.getColorCompat
import com.basic.common.extension.getDeviceId
import com.basic.common.extension.getDimens
import com.basic.common.extension.makeToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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
                tv.gradient(R.color.yellow, R.color.dark_yellow)
            }
            isPreSet = true
        }
        else {
            listText[day - 1].gradient(R.color.yellow, R.color.dark_yellow)
            listText[day - 1].requestLayout()
        }

    }
    private fun initListener() {
        binding.receiveCardView.clicks(withAnim = false) {
            binding.receiveCardView.isEnabled = false
            lifecycleScope.launch(Dispatchers.IO) {
                val todayReward = DailyReward.values().take(consecutiveSeries + 1).last().reward.toLong()
                val request = UpdateCreditRequest(todayReward, Constraint.CREATE_ARTWORK)
                serverApiRepository.updateCredit(requireContext().getDeviceId(),request){
                    lifecycleScope.launch(Dispatchers.Main) {
                        binding.receiveCardView.strokeWidth = requireContext().getDimens(com.intuit.sdp.R.dimen._1sdp).toInt()
                        binding.receiveLayout.setBackgroundColor(requireContext().getColor(R.color.gray_3D))
                        binding.tvReceive.setTextColor(requireContext().getColor(R.color.light_gray))
                        setDayReward( consecutiveSeries + 1 )
                        requireContext().makeToast("You have received $todayReward credit")
                        pref.consecutiveSeries.set(consecutiveSeries + 1)
                        pref.isReceived.set(true)
                        delay(300)
                        dismiss()
                    }
                }
            }
        }
    }
    private fun setDayReward(day : Int){
        setGradientReceivedDay(day)
        val imageResourceId = requireContext().resources.getIdentifier("_${day}_tick","drawable",binding.root.context.packageName)
        binding.ivCreditSlider.setImageResource(imageResourceId)
        dailyRewardAdapter.data = DailyReward.values().take(day).toList()
    }
    private fun initView() {
        binding.rv.adapter = dailyRewardAdapter
        setDayReward(consecutiveSeries)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog?.window?.setBackgroundDrawableResource(R.color.transparent)
    }
}