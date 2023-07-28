package com.anime.art.ai.feature.setting


import android.os.Bundle
import com.anime.art.ai.common.extension.back
import com.anime.art.ai.common.extension.startCreditHistory
import com.anime.art.ai.databinding.ActivitySettingBinding
import com.anime.art.ai.feature.credithistory.BuyMoreDialog
import com.basic.common.base.LsActivity
import com.basic.common.extension.clicks
import com.basic.common.extension.transparent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingActivity : LsActivity<ActivitySettingBinding>(ActivitySettingBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transparent()
        setContentView(binding.root)
        initListener()

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
    }
}