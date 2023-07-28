package com.anime.art.ai.feature.main.gallery.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.anime.art.ai.R
import com.anime.art.ai.common.extension.gradient
import com.anime.art.ai.data.Preferences
import com.anime.art.ai.databinding.DialogDailyCreditBinding
import com.anime.art.ai.domain.model.DailyReward
import com.anime.art.ai.feature.main.gallery.adapter.DailyRewardAdapter
import com.basic.common.extension.clicks
import com.basic.common.extension.getColorCompat
import com.basic.common.extension.getDimens
import com.basic.common.extension.makeToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
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
                tv.setTextColor(requireContext().getColorCompat(R.color.dark_yellow))
            }
            isPreSet = true
        }
        else {
            listText[day - 1].setTextColor(requireContext().getColorCompat(R.color.dark_yellow))
        }

        Timber.tag("Main12345").e("Index: $day")
    }
    private fun initListener() {
        binding.receiveCardView.clicks(withAnim = false) {

            binding.receiveCardView.strokeWidth = requireContext().getDimens(com.intuit.sdp.R.dimen._1sdp).toInt()
            binding.receiveLayout.setBackgroundColor(requireContext().getColor(R.color.gray_3D))
            val currentDay = pref.numOfDayReceivedCredit.get()
            pref.numOfDayReceivedCredit.set( currentDay + 1 )
            setDayReward(pref.numOfDayReceivedCredit.get())
            // Lấy ngày và giờ hiện tại
            val currentDateTime = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val formattedDateTime = currentDateTime.format(formatter)
            pref.timeGetDailyCredit.set(formattedDateTime)
            val receivedCredit = DailyReward.values().take(pref.numOfDayReceivedCredit.get()).last().reward
            requireContext().makeToast("You have received $receivedCredit credit")
            lifecycleScope.launch {
                delay(500)
                dismiss()
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