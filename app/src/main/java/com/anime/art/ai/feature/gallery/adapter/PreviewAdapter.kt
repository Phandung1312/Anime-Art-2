package com.anime.art.ai.feature.gallery.adapter

import androidx.constraintlayout.widget.ConstraintSet
import com.anime.art.ai.R
import com.anime.art.ai.databinding.ItemPreviewInDetailGalleryBinding
import com.anime.art.ai.domain.model.config.Gallery
import com.basic.common.base.LsAdapter
import com.basic.common.extension.clicks
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import javax.inject.Inject

class PreviewAdapter @Inject constructor(): LsAdapter<Gallery, ItemPreviewInDetailGalleryBinding>(ItemPreviewInDetailGalleryBinding::inflate) {

    val toggleFavouriteClicks: Subject<Gallery> = PublishSubject.create()

    override fun bindItem(
        item: Gallery,
        binding: ItemPreviewInDetailGalleryBinding,
        position: Int
    ) {
        ConstraintSet().apply {
            this.clone(binding.viewPreview)
            this.setDimensionRatio(binding.preview.id, item.ratio)
            this.applyTo(binding.viewPreview)
        }

        binding.display.text = item.display
        binding.favourite.setImageResource(if (item.favourite) R.drawable.favourite else R.drawable.unfavourite)

        Glide.with(binding.root)
            .load(item.avatar)
            .error(R.drawable.place_holder_image)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.avatar)

        Glide.with(binding.root)
            .load(item.preview)
            .error(R.drawable.place_holder_image)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.preview)

        binding.favourite.clicks {
            item.favourite = !item.favourite
            binding.favourite.setImageResource(if (item.favourite) R.drawable.favourite else R.drawable.unfavourite)

            toggleFavouriteClicks.onNext(item)
        }
    }

}