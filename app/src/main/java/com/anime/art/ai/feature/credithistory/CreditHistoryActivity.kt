package com.anime.art.ai.feature.credithistory

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.Settings
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.anime.art.ai.common.extension.back
import com.anime.art.ai.data.Preferences
import com.anime.art.ai.data.db.query.HistoryDao
import com.anime.art.ai.databinding.ActivityCreditHistoryBinding
import com.anime.art.ai.domain.repository.ServerApiRepository
import com.anime.art.ai.feature.credithistory.adapter.CreditHistoryAdapter
import com.basic.common.base.LsActivity
import com.basic.common.extension.clicks
import com.basic.common.extension.getDeviceId
import com.basic.common.extension.transparent
import com.basic.common.extension.tryOrNull
import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.autoDispose
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class CreditHistoryActivity : LsActivity<ActivityCreditHistoryBinding>(ActivityCreditHistoryBinding::inflate) {
    @Inject lateinit var serverApiRepo: ServerApiRepository
    @Inject lateinit var creditHistoryAdapter: CreditHistoryAdapter
    @Inject lateinit var pref : Preferences
    @Inject lateinit var historyDao: HistoryDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transparent()
        setContentView(binding.root)
        initView()
        initObservable()
        initData()
        initListener()

    }

    private fun initObservable() {
        pref.creditAmount.asObservable().autoDispose(scope()).subscribe {creditAmount ->
            binding.tvCreditAmount.text = creditAmount.toString()
        }
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
    private fun initData(){
        historyDao.getAll().observe(this){ histories ->
            binding.lottieView.isVisible = false
            creditHistoryAdapter.data = histories.reversed()
            binding.rv.isVisible = true
        }

    }
}