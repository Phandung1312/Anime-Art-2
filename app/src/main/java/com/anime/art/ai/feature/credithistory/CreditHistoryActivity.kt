package com.anime.art.ai.feature.credithistory

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.Settings
import androidx.lifecycle.lifecycleScope
import com.anime.art.ai.common.extension.back
import com.anime.art.ai.databinding.ActivityCreditHistoryBinding
import com.anime.art.ai.domain.repository.ServerApiRepository
import com.anime.art.ai.feature.credithistory.adapter.CreditHistoryAdapter
import com.basic.common.base.LsActivity
import com.basic.common.extension.clicks
import com.basic.common.extension.getDeviceId
import com.basic.common.extension.transparent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CreditHistoryActivity : LsActivity<ActivityCreditHistoryBinding>(ActivityCreditHistoryBinding::inflate) {
    @Inject lateinit var serverApiRepo: ServerApiRepository
    @Inject lateinit var creditHistoryAdapter: CreditHistoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transparent()
        setContentView(binding.root)
        initView()
        initData()
        initListener()
    }
    private fun initView(){
        binding.rv.adapter = creditHistoryAdapter
    }
    private fun initListener() {
        binding.back.clicks {
            back()
        }
        binding.buyMoreView.clicks(withAnim = false) {
            val buyMoreDialog = BuyMoreDialog()
            buyMoreDialog.show(supportFragmentManager, null)
        }
    }
    @SuppressLint("HardwareIds")
    private fun initData(){
        lifecycleScope.launch(Dispatchers.IO) {
            serverApiRepo.getCreditHistory(getDeviceId()){ histories ->
                creditHistoryAdapter.data = histories.reversed()
            }
        }
    }
}