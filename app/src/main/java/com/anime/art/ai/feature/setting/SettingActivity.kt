package com.anime.art.ai.feature.setting


import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.anime.art.ai.R
import com.anime.art.ai.common.Constraint
import com.anime.art.ai.common.extension.back
import com.anime.art.ai.common.extension.gradient
import com.anime.art.ai.common.extension.startCreditHistory
import com.anime.art.ai.data.Preferences
import com.anime.art.ai.databinding.ActivitySettingBinding
import com.anime.art.ai.domain.model.DailyReward
import com.anime.art.ai.domain.repository.ServerApiRepository
import com.anime.art.ai.feature.credithistory.BuyMoreDialog
import com.anime.art.ai.feature.main.gallery.adapter.DailyRewardAdapter
import com.anime.art.ai.inject.sinkin.UpdateCreditRequest
import com.basic.common.base.LsActivity
import com.basic.common.extension.clicks
import com.basic.common.extension.getColorCompat
import com.basic.common.extension.getDeviceId
import com.basic.common.extension.getDimens
import com.basic.common.extension.makeToast
import com.basic.common.extension.transparent
import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.autoDispose
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class SettingActivity : LsActivity<ActivitySettingBinding>(ActivitySettingBinding::inflate) {
    @Inject lateinit var serverApiRepository: ServerApiRepository
    @Inject lateinit var preferences: Preferences
    @Inject lateinit var dailyRewardAdapter : DailyRewardAdapter
    private var consecutiveSeries : Int = 0
    private var isPreSet = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transparent()
        setContentView(binding.root)
        initView()
        initListener()
        initData()
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
                //tv.setTextColor(getColorCompat(R.color.dark_yellow))
                tv.gradient(R.color.yellow, R.color.dark_yellow)
            }
            isPreSet = true
        }
        else {
//            listText[day - 1].setTextColor(getColorCompat(R.color.dark_yellow))
            listText[day - 1].gradient(R.color.yellow, R.color.dark_yellow)
//            binding.tvDay7.gradient(R.color.light_gray, R.color.dark_yellow)
        }

    }
    private fun initListener() {
       binding.back.clicks {
           back()
       }
        binding.creditHistoryView.clicks(withAnim = false) {
            startCreditHistory()
        }

        binding.creditView.clicks(withAnim = false) {
            val buyMoreDialog = BuyMoreDialog()
            buyMoreDialog.show(supportFragmentManager, null)
        }
        binding.receiveCardView.clicks(withAnim = false) {
//            binding.receiveCardView.isEnabled = false
//            lifecycleScope.launch(Dispatchers.IO) {
//                val todayReward = DailyReward.values().take(consecutiveSeries + 1).last().reward.toLong()
//                val request = UpdateCreditRequest(todayReward, Constraint.CREATE_ARTWORK)
//                serverApiRepository.updateCredit(getDeviceId(),request){
//                    lifecycleScope.launch(Dispatchers.Main) {
//                        setDayReward( consecutiveSeries + 1 )
//                        makeToast("You have received $todayReward credit")
//                        preferences.consecutiveSeries.set(consecutiveSeries + 1)
//                        preferences.isReceived.set(true)
//                    }
//                }
//            }
            setDayReward(7)
        }
    }
    private fun setDayReward(day : Int){
        setGradientReceivedDay(day)
        val imageResourceId = resources.getIdentifier("_${day}_tick","drawable",binding.root.context.packageName)
        binding.ivCreditSlider.setImageResource(imageResourceId)
        dailyRewardAdapter.data = DailyReward.values().take(day).toList()
    }
    private fun initData(){
        preferences.creditAmount.asObservable().autoDispose(scope()).subscribe {creditAmount ->
            binding.tvCreditAmount.text = creditAmount.toString()
        }
//        preferences.consecutiveSeries
//            .asObservable()
//            .autoDispose(scope())
//            .subscribe {
//                consecutiveSeries = it
//                setDayReward(consecutiveSeries)
//            }
//        preferences.isReceived
//            .asObservable()
//            .autoDispose(scope())
//            .subscribe{ isReceived ->
//                binding.receiveCardView.apply {
//                    isEnabled = !isReceived
//                    strokeWidth = if(isReceived) getDimens(com.intuit.sdp.R.dimen._1sdp).toInt() else 0
//                }
//                binding.receiveLayout.apply {
//                    if(isReceived) setBackgroundColor(getColor(R.color.gray_3D))
//                    else setBackgroundResource(R.drawable.button_gradient_yellow)
//                }
//                binding.tvReceive.setTextColor(if(isReceived) getColor(R.color.light_gray) else getColor(R.color.black))
//            }
        setDayReward(5)
    }
    private fun initView(){
        binding.rv.adapter = dailyRewardAdapter
    }

}