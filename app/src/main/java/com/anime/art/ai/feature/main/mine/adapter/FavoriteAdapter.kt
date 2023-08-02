package com.anime.art.ai.feature.main.mine.adapter

import androidx.constraintlayout.widget.ConstraintSet
import com.anime.art.ai.R
import com.anime.art.ai.databinding.ItemPreviewInGalleryBinding
import com.anime.art.ai.domain.model.config.Gallery
import com.basic.common.base.LsAdapter
import com.basic.common.extension.clicks
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import javax.inject.Inject

class FavoriteAdapter @Inject constructor() : LsAdapter<Gallery, ItemPreviewInGalleryBinding>(
    ItemPreviewInGalleryBinding::inflate
) {
    val clicks: Subject<Gallery> = PublishSubject.create()
    val toggleFavouriteClicks: Subject<Gallery> = PublishSubject.create()
    override fun bindItem(item: Gallery, binding: ItemPreviewInGalleryBinding, position: Int) {
        ConstraintSet().apply {
            this.clone(binding.viewPreview)
            this.setDimensionRatio(binding.preview.id, item.ratio)
            this.applyTo(binding.viewPreview)
        }

        binding.display.text = item.display
        binding.favourite.setImageResource(if (item.favourite) R.drawable.heart else R.drawable.unheart)
        binding.favourite.clicks {
            item.favourite = !item.favourite
            binding.favourite.setImageResource(if (item.favourite) R.drawable.heart else R.drawable.unheart)
            toggleFavouriteClicks.onNext(item)
        }

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

        binding.viewClicks.clicks(withAnim = false) { clicks.onNext(item) }
    }

}