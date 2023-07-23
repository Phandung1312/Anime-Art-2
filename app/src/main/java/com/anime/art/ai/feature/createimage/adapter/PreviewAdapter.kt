package com.anime.art.ai.feature.createimage.adapter

import android.view.View
import com.anime.art.ai.R
import com.anime.art.ai.databinding.ItemPreviewInCreateImageBinding
import com.anime.art.ai.domain.model.config.Gallery
import com.anime.art.ai.domain.model.config.ViewImage
import com.basic.common.base.LsAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import javax.inject.Inject

class PreviewAdapter @Inject constructor(): LsAdapter<ViewImage, ItemPreviewInCreateImageBinding>(ItemPreviewInCreateImageBinding::inflate) {
    override fun bindItem(item: ViewImage, binding: ItemPreviewInCreateImageBinding, position: Int) {


        Glide.with(binding.root.context)
            .load(item.display)
            .error(R.drawable.place_holder_image)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.preview)
    }


}