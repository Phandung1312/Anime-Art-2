package com.anime.art.ai.feature.createimage


import android.os.Bundle
import android.util.Base64
import androidx.activity.addCallback
import androidx.constraintlayout.widget.ConstraintSet
import com.anime.art.ai.R
import com.anime.art.ai.common.ConfigApp
import com.anime.art.ai.common.extension.back
import com.anime.art.ai.data.Preferences
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
    @Inject lateinit var pref : Preferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(pref.isDarkMode.get()){
            transparent()
        }
        else{
            transparent(true, true)
        }
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
        ConstraintSet().apply {
            this.clone(binding.rootView)
            this.setDimensionRatio(binding.iv.id, configApp.imageGenerationRequest.ratio)
            this.applyTo(binding.rootView)
        }
        Glide.with(binding.root.context)
            .load(configApp.url)
            .error(R.drawable.place_holder_image)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.iv)
    }
}