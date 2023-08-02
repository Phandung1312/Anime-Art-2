package com.anime.art.ai.feature.createimage


import android.os.Bundle
import android.util.Base64
import androidx.activity.addCallback
import com.anime.art.ai.R
import com.anime.art.ai.common.ConfigApp
import com.anime.art.ai.common.extension.back
import com.anime.art.ai.databinding.ActivityEnlargeImageBinding
import com.basic.common.base.LsActivity
import com.basic.common.extension.clicks
import com.basic.common.extension.transparent
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EnlargeImageActivity : LsActivity<ActivityEnlargeImageBinding>(ActivityEnlargeImageBinding::inflate) {
    @Inject lateinit var configApp: ConfigApp
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transparent()
        setContentView(binding.root)
        initView()
        initListener()
    }

    private fun initListener() {
        binding.close.clicks {
            back()
        }
        onBackPressedDispatcher.addCallback {
            back()
        }
    }
    private fun initView() {
        showImage()
    }

    private fun showImage(){
        val decodedBytes: ByteArray = Base64.decode(configApp.imageBase64, Base64.DEFAULT)
        val dataUrl = "data:image/jpeg;base64," + Base64.encodeToString(decodedBytes, Base64.DEFAULT)
        Glide.with(binding.root.context)
            .load(dataUrl)
            .error(R.drawable.place_holder_image)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.iv)
    }
}