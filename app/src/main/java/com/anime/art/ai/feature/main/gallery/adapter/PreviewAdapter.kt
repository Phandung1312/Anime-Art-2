package com.anime.art.ai.feature.main.gallery.adapter

import androidx.constraintlayout.widget.ConstraintSet
import com.anime.art.ai.R
import com.anime.art.ai.databinding.ItemPreviewInGalleryBinding
import com.anime.art.ai.domain.model.config.Gallery
import com.basic.common.base.LsAdapter
import com.bumptech.glide.Glide
import timber.log.Timber
import javax.inject.Inject

class PreviewAdapter @Inject constructor(): LsAdapter<Gallery, ItemPreviewInGalleryBinding>(ItemPreviewInGalleryBinding::inflate) {

    override fun bindItem(item: Gallery, binding: ItemPreviewInGalleryBinding, position: Int) {
        ConstraintSet().apply {
            this.clone(binding.root)
            this.setDimensionRatio(binding.viewPreview.id, item.ratio)
            this.applyTo(binding.root)
        }

        Timber.e("Ratio: ${item.ratio} --- $position")

        binding.display.text = item.display

        Glide.with(binding.root)
            .load(item.preview)
            .error(R.drawable.place_holder_image)
            .into(binding.preview)
    }

}