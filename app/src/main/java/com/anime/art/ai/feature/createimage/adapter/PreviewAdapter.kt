package com.anime.art.ai.feature.createimage.adapter

import com.anime.art.ai.R
import com.anime.art.ai.databinding.ItemPreviewInCreateImageBinding
import com.anime.art.ai.domain.model.config.ViewImage
import com.basic.common.base.LsAdapter
import com.basic.common.extension.clicks
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import javax.inject.Inject

class PreviewAdapter @Inject constructor(): LsAdapter<ViewImage, ItemPreviewInCreateImageBinding>(ItemPreviewInCreateImageBinding::inflate) {
    val unlockClicks : Subject<Unit> = PublishSubject.create()
    override fun bindItem(item: ViewImage, binding: ItemPreviewInCreateImageBinding, position: Int) {
        Glide.with(binding.root.context)
            .load(item.display)
            .error(R.drawable.place_holder_image)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.preview)
        binding.unlock.clicks {
            unlockClicks.onNext(Unit)
        }
    }


}