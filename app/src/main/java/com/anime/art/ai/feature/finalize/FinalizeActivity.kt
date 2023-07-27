package com.anime.art.ai.feature.finalize

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.anime.art.ai.R
import com.anime.art.ai.common.extension.back
import com.anime.art.ai.common.extension.gradient
import com.anime.art.ai.databinding.ActivityFinalizeBinding
import com.basic.common.base.LsActivity
import com.basic.common.extension.clicks
import com.basic.common.extension.transparent
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FinalizeActivity : LsActivity<ActivityFinalizeBinding>(ActivityFinalizeBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transparent()
        setContentView(binding.root)
        initView()
        initObservable()
        initData()
        listenerView()
    }

    private fun listenerView() {
        binding.close.clicks {
            back()
        }
    }

    private fun initData() {
        Glide.with(binding.iv)
            .load("https://firebasestorage.googleapis.com/v0/b/anime-art-2.appspot.com/o/v1%2F1_zzz_512__584_zzz.webp?alt=media&token=e12e729f-81f3-4c4e-9379-510209f6c818")
            .into(binding.iv)
    }

    private fun initObservable() {

    }

    private fun initView() {
        binding.tvArtStyle.gradient(R.color.yellow, R.color.dark_yellow)
    }
}