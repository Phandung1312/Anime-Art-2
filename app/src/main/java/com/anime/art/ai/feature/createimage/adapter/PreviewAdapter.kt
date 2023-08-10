package com.anime.art.ai.feature.createimage.adapter

import android.util.Base64
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import com.anime.art.ai.R
import com.anime.art.ai.databinding.ItemPreviewInCreateImageBinding
import com.anime.art.ai.domain.model.response.ImageResponse
import com.basic.common.base.LsAdapter
import com.basic.common.extension.clicks
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import javax.inject.Inject

class PreviewAdapter @Inject constructor(): LsAdapter<ImageResponse, ItemPreviewInCreateImageBinding>(ItemPreviewInCreateImageBinding::inflate) {
    val unlockClicks : Subject<Unit> = PublishSubject.create()
    val zoomClicks : Subject<String> = PublishSubject.create()
    val saveClicks : Subject<String> = PublishSubject.create()
    var isPremium = false
        set(value){
            field = value
            notifyItemRangeChanged(
                1,data.size - 1
            )
        }

    var selectedIndex = 0
        set(value){
            if(field == value) return
            field.takeIf { it != -1 }?.let { notifyItemChanged(it) }
            value.takeIf { it != -1 }?.let { notifyItemChanged(it) }
            field = value
        }
    override fun bindItem(item: ImageResponse, binding: ItemPreviewInCreateImageBinding, position: Int) {
        ConstraintSet().apply {
            this.clone(binding.rootView)
            this.setDimensionRatio(binding.cardView.id, item.ratio)
            this.applyTo(binding.rootView)
        }
        val decodedBytes: ByteArray = Base64.decode(item.image, Base64.DEFAULT)
        val dataUrl = "data:image/jpeg;base64," + Base64.encodeToString(decodedBytes, Base64.DEFAULT)
        Glide.with(binding.root.context)
            .load(dataUrl)
            .error(R.drawable.place_holder_image)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.preview)

        if(position == 0 || isPremium){
            binding.saveView.isVisible = true
            binding.zoomView.isVisible = true
            binding.unlock.isVisible = false
        }
        else{
            binding.unlock.isVisible = true
        }
        binding.tickView.isVisible = (position == selectedIndex && isPremium) || position == 0

        binding.unlock.clicks {
            unlockClicks.onNext(Unit)
        }
        binding.saveView.clicks {
            saveClicks.onNext(item.image)
        }
        binding.zoomView.clicks {
            zoomClicks.onNext(item.image)
        }
    }


}