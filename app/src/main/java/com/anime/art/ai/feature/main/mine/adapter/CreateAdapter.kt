package com.anime.art.ai.feature.main.mine.adapter


import androidx.constraintlayout.widget.ConstraintSet
import com.anime.art.ai.R
import com.anime.art.ai.databinding.ItemPreviewInGalleryBinding
import com.anime.art.ai.domain.model.config.Creator
import com.basic.common.base.LsAdapter
import com.basic.common.extension.clicks
import com.basic.common.util.ViewUtils.gone
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import javax.inject.Inject

class CreateAdapter @Inject constructor() : LsAdapter<Creator, ItemPreviewInGalleryBinding>(ItemPreviewInGalleryBinding::inflate) {
    val clicks: Subject<Creator> = PublishSubject.create()
    override fun bindItem(item: Creator, binding: ItemPreviewInGalleryBinding, position: Int) {
        ConstraintSet().apply {
            this.clone(binding.viewPreview)
            this.setDimensionRatio(binding.preview.id, "9:16")
            this.applyTo(binding.viewPreview)
        }
        Glide.with(binding.root)
            .load(item.image)
            .error(R.drawable.place_holder_image)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.preview)

        binding.viewClicks.clicks(withAnim = false) {
            clicks.onNext(item)
        }
        binding.avatar.gone()
        binding.display.gone()
    }
}