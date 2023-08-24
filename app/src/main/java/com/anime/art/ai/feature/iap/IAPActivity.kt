package com.anime.art.ai.feature.iap

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.addCallback
import androidx.lifecycle.lifecycleScope
import com.anime.art.ai.R
import com.anime.art.ai.common.Constraint
import com.anime.art.ai.common.extension.back
import com.anime.art.ai.common.extension.gradient
import com.anime.art.ai.common.extension.startMain
import com.anime.art.ai.data.Preferences
import com.anime.art.ai.databinding.ActivityIapactivityBinding
import com.anime.art.ai.domain.repository.ServerApiRepository
import com.anime.art.ai.feature.main.MainActivity
import com.anime.art.ai.inject.sinkin.UpdateCreditRequest
import com.basic.common.base.LsActivity
import com.basic.common.extension.clicks
import com.basic.common.extension.getDeviceId
import com.basic.common.extension.isNetworkAvailable
import com.basic.common.extension.makeToast
import com.basic.common.extension.tryOrNull
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class IAPActivity : LsActivity<ActivityIapactivityBinding>(ActivityIapactivityBinding::inflate) {
    @Inject lateinit var serverApiRepository: ServerApiRepository
    @Inject lateinit var pref : Preferences
    companion object{
        const val IS_FIRST_SCREEN_EXTRA = "IS_FIRST_SCREEN_EXTRA"
    }
    private var isFirstScreen = false
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
        onBackPressedDispatcher.addCallback {
            if(isFirstScreen){
                startMain()
                finish()
            }
            else{
                back()
            }
        }
        binding.close.clicks {
            if(isFirstScreen){
                startMain()
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
        binding.continueView.clicks {
            binding.continueView.isEnabled = false
            if(isNetworkAvailable()){
                lifecycleScope.launch(Dispatchers.IO) {
                    val request = UpdateCreditRequest(
                        title = if (iAPSelected == 1) Constraint.PURCHASED_WEEK else Constraint.PURCHASED_YEAR,
                        credit = if (iAPSelected == 1) 100 else 1200
                    )
                    serverApiRepository.updateCredit(getDeviceId(), request) { result ->
                        launch(Dispatchers.Main) {
                            if (result) {
                                pref.isPremium.set(true)
                                makeToast("Buy premium package successfully")
                                if (isFirstScreen) {
                                    startActivity(Intent(this@IAPActivity, MainActivity::class.java))
                                    tryOrNull {
                                        overridePendingTransition(
                                            R.anim.slide_in_left,
                                            R.anim.nothing
                                        )
                                    }
                                    finish()
                                } else {
                                    back()
                                }
                            }
                            else{
                                makeToast("An error has occurred")
                                binding.continueView.isEnabled = true
                            }
                        }
                    }
                }
            }
            else{
                makeToast("Please check your connect internet !")
                binding.continueView.isEnabled = true
            }
        }
        binding.teamOfUseView.clicks(withAnim = false) {
            startBrowser(Constraint.Info.TERMS_URL)
        }
        binding.privacyPolicyView.clicks(withAnim = false) {
            startBrowser(Constraint.Info.PRIVACY_URL)
        }
        binding.restoreView.clicks(withAnim = false) {
            makeToast("Restore failed")
        }
    }

    private fun initData() {

    }

    private fun initObservable() {

    }

    private fun initView() {
        binding.tvTittle.gradient(R.color.colorSecondary, R.color.colorPrimary)
        isFirstScreen = intent.getBooleanExtra(IS_FIRST_SCREEN_EXTRA, false)
    }

    private fun startBrowser(url : String){
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        try{
            startActivity(intent)
        }
        catch(e : Exception){
            makeToast("you don't have browser installed")
        }

    }
}