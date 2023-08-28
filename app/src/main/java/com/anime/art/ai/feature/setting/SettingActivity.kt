package com.anime.art.ai.feature.setting


import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.anime.art.ai.R
import com.anime.art.ai.common.Constraint
import com.anime.art.ai.common.extension.back
import com.anime.art.ai.common.extension.convertToShortDate
import com.anime.art.ai.common.extension.dayBetween
import com.anime.art.ai.common.extension.gradientYellowArray
import com.anime.art.ai.common.extension.startCreditHistory
import com.anime.art.ai.common.getCurrentDay
import com.anime.art.ai.data.Preferences
import com.anime.art.ai.data.db.query.HistoryDao
import com.anime.art.ai.databinding.ActivitySettingBinding
import com.anime.art.ai.domain.model.DailyReward
import com.anime.art.ai.domain.repository.ServerApiRepository
import com.anime.art.ai.feature.credithistory.BuyMoreDialog
import com.anime.art.ai.feature.main.gallery.adapter.DailyRewardAdapter
import com.anime.art.ai.inject.sinkin.UpdateCreditRequest
import com.basic.common.base.LsActivity
import com.basic.common.extension.clicks
import com.basic.common.extension.getDeviceId
import com.basic.common.extension.getDimens
import com.basic.common.extension.makeToast
import com.basic.common.extension.transparent
import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.autoDispose
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SettingActivity : LsActivity<ActivitySettingBinding>(ActivitySettingBinding::inflate) {

    @Inject lateinit var serverApiRepository: ServerApiRepository
    @Inject lateinit var preferences: Preferences
    @Inject lateinit var dailyRewardAdapter : DailyRewardAdapter
    @Inject lateinit var historyDao : HistoryDao
    private var consecutiveSeries : Int = 0

    private var isReceived = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(preferences.isDarkMode.get()){
            transparent()
        }
        else{
            transparent(true, true)
        }
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
        listText.take(day).forEach {  tv ->
                tv.gradientYellowArray()
                tv.requestLayout()
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
            binding.creditView.isEnabled = false
            val buyMoreDialog = BuyMoreDialog{
                binding.creditView.isEnabled = true
            }
            buyMoreDialog.show(supportFragmentManager, null)
        }
        binding.receiveCardView.clicks(withAnim = false) {
            if(isReceived){
                makeToast("You got your reward today")
            }
            else{
                lifecycleScope.launch(Dispatchers.IO) {
                    val todayReward = DailyReward.values().take(consecutiveSeries).last().reward.toLong()
                    val request = UpdateCreditRequest(todayReward, Constraint.DAILY_REWARD)
                    serverApiRepository.updateCredit(getDeviceId(),request){ result ->
                        lifecycleScope.launch(Dispatchers.Main) {
                            if(result){
                                makeToast("You have received $todayReward credit")
                            }
                            else{
                                binding.receiveCardView.isEnabled = true
                            }
                        }
                    }
                }
                setDayReward(consecutiveSeries , true)
            }
        }
        binding.shareView.clicks(withAnim = false) {
            val text ="abc"
            val intent  = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, text)
            startActivity(Intent.createChooser(intent, "Share using..."))
        }
        binding.privacyPolicyView.clicks(withAnim = false){
            startBrowser(Constraint.Info.PRIVACY_URL)
        }
        binding.termView.clicks(withAnim = false) {
            startBrowser(Constraint.Info.TERMS_URL)
        }
        binding.feedbackView.clicks(withAnim = false){
            feedBack()
        }
        binding.rateUsView.clicks(withAnim = false) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${packageName}"))
                .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)

            try {
                startActivityExternal(intent)
            } catch (e: ActivityNotFoundException) {
                val url = "http://play.google.com/store/apps/details?id=${packageName}"
                startActivityExternal(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            }
        }
    }
    private fun startActivityExternal(intent: Intent) {
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            startActivity(Intent.createChooser(intent, null))
        }
    }
    private fun feedBack(){
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(Constraint.Info.MAIL_SUPPORT))
        intent.putExtra(Intent.EXTRA_SUBJECT, "${getString(R.string.app_name)} Feedback")
        val deviceId = getDeviceId()
        intent.putExtra(
            Intent.EXTRA_TEXT, "user id:$deviceId"
        )
        startActivityExternal(intent)
    }
    private fun startBrowser(url : String){
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        try{
            startActivity(intent)
        }
        catch(e : Exception){
            makeToast("You don't have browser installed")
        }

    }
    @SuppressLint("DiscouragedApi")
    private fun setDayReward(day : Int, isReceived : Boolean){
        this.isReceived = isReceived
        consecutiveSeries = day
        setGradientReceivedDay(day)
        val imageResourceId = if(isReceived) resources.getIdentifier("_${day}_tick","drawable",binding.root.context.packageName)
        else resources.getIdentifier("_${day}_tick_not_received","drawable",binding.root.context.packageName)
        binding.ivCreditSlider.setImageResource(imageResourceId)
        dailyRewardAdapter.data = DailyReward.values().take(day).toList()
        binding.receiveCardView.apply {
            strokeWidth = if(isReceived) getDimens(com.intuit.sdp.R.dimen._1sdp).toInt() else 0
        }
        binding.receiveLayout.apply {
            if(isReceived) setBackgroundColor(getColor(R.color.background_received))
            else setBackgroundResource(R.drawable.button_gradient_yellow)
        }
        binding.tvReceive.setTextColor(if(isReceived) getColor(R.color.white_35) else getColor(R.color.textButton))
    }
    private fun initData(){
        preferences.creditAmount.asObservable().autoDispose(scope()).subscribe {creditAmount ->
            binding.tvCreditAmount.text = creditAmount.toString()
        }
        historyDao.getAllLive().observe(this){ histories ->
            if(preferences.isSynced.get()){
                lifecycleScope.launch(Dispatchers.Main) {
                    val newList =  histories.filter { history -> TextUtils.equals(history.title, Constraint.DAILY_REWARD)  }
                        .map { it.createdAt.convertToShortDate()}
                        .reversed()
                    if (newList.isEmpty()) {
                        setDayReward(1, isReceived = false)
                        return@launch
                    }
                    consecutiveSeries  = 1
                    if(newList.size > 1){
                        for(i in 0 ..   newList.size -2){
                            if(newList[i].dayBetween(newList[i + 1]) > 1L) break
                            consecutiveSeries += 1
                            if(consecutiveSeries > 7){
                                consecutiveSeries = 7
                                break
                            }
                        }
                    }
                    val currentDay = getCurrentDay()
                    if(currentDay.dayBetween(newList[0]) == 1L && newList.size == 1) {
                            consecutiveSeries = 2
                            setDayReward(consecutiveSeries, isReceived = false)
                    }
                    else if(currentDay.dayBetween(newList[0]) == 1L){
                        if(consecutiveSeries > 6) {
                            consecutiveSeries = 1
                        }
                        else consecutiveSeries++
                        setDayReward(consecutiveSeries, false)
                    }
                    else if(currentDay.dayBetween(newList[0]) > 1L) {
                        consecutiveSeries = 1
                        setDayReward(consecutiveSeries, isReceived = false)
                    }
                    else setDayReward(if(consecutiveSeries > 7) 1 else consecutiveSeries, isReceived = true)
                }
            }
        }


        preferences
            .isSynced
            .asObservable()
            .autoDispose(scope())
            .subscribe { isSyncedData ->
                if(!isSyncedData){
                    lifecycleScope.launch(Dispatchers.IO) {
                        serverApiRepository.getCreditHistory(getDeviceId()){result ->
                            if(!result){
                                makeToast("An error has occurred")
                            }
                        }
                    }
                }
            }
    }
    private fun initView(){
        binding.rv.adapter = dailyRewardAdapter
        setDayReward(1, true)
    }

}