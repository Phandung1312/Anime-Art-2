package com.anime.art.ai.feature.iap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.anime.art.ai.R
import com.anime.art.ai.databinding.ActivityIapactivityBinding
import com.basic.common.base.LsActivity

class IAPActivity : LsActivity<ActivityIapactivityBinding>(ActivityIapactivityBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
        initObservable()
        initData()
        listenerView()
    }

    private fun listenerView() {

    }

    private fun initData() {

    }

    private fun initObservable() {

    }

    private fun initView() {

    }
}