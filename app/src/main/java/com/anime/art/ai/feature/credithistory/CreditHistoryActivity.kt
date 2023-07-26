package com.anime.art.ai.feature.credithistory

import android.os.Bundle
import com.anime.art.ai.databinding.ActivityCreditHistoryBinding
import com.basic.common.base.LsActivity
import com.basic.common.extension.clicks
import com.basic.common.extension.transparent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreditHistoryActivity : LsActivity<ActivityCreditHistoryBinding>(ActivityCreditHistoryBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transparent()
        setContentView(binding.root)
        initListener()
    }

    private fun initListener() {
        binding.buyMoreView.clicks(withAnim = false) {
            val buyMoreDialog = BuyMoreDialog()
            buyMoreDialog.show(supportFragmentManager, null)
        }
    }
}