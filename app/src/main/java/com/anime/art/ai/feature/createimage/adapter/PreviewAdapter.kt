package com.anime.art.ai.feature.createimage.adapter

import android.util.Base64
import androidx.core.view.isVisible
import com.anime.art.ai.R
import com.anime.art.ai.databinding.ItemPreviewInCreateImageBinding
import com.anime.art.ai.domain.model.config.ViewImage
import com.anime.art.ai.domain.model.response.ImageResponse
import com.basic.common.base.LsAdapter
import com.basic.common.extension.clicks
import com.basic.common.extension.saveBase64ImageToGallery
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
    override fun bindItem(item: ImageResponse, binding: ItemPreviewInCreateImageBinding, position: Int) {
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
            binding.tickView.isVisible = true
        }
        else{
            binding.unlock.isVisible = true
        }
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