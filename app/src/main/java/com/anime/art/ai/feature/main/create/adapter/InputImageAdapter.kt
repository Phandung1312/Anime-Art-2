package com.anime.art.ai.feature.main.create.adapter

import android.net.Uri
import android.view.ViewGroup
import com.anime.art.ai.R
import com.anime.art.ai.databinding.ItemInputImageBinding
import com.anime.art.ai.domain.model.config.InputImage
import com.basic.common.base.LsAdapter
import com.basic.common.extension.clicks
import com.basic.common.extension.getDimens
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import javax.inject.Inject

class InputImageAdapter @Inject constructor() : LsAdapter<InputImage, ItemInputImageBinding>(ItemInputImageBinding::inflate) {
    val clicks : Subject<InputImage> = PublishSubject.create()
   var selectedIndex = -1
        set(value) {
            if (field == value){
                return
            }
            field.takeIf { it != -1 }?.let { notifyItemChanged(it) }
            value.takeIf { it != -1 }?.let { notifyItemChanged(it) }

            field = value
        }
    override fun bindItem(item: InputImage, binding: ItemInputImageBinding, position: Int) {

        val layoutParams = binding.root.layoutParams as ViewGroup.MarginLayoutParams
        val marginStartResId = if (position == 0) {
            com.intuit.sdp.R.dimen._10sdp
        } else {
            com.intuit.sdp.R.dimen._4sdp
        }
        val marginEndResId = if (position == itemCount - 1) {
            com.intuit.sdp.R.dimen._10sdp
        } else {
            com.intuit.sdp.R.dimen._4sdp
        }
        layoutParams.marginStart = binding.root.context.resources.getDimensionPixelSize(marginStartResId)
        layoutParams.marginEnd = binding.root.context.resources.getDimensionPixelSize(marginEndResId)

        if(position != 0) binding.rootView.apply {
            radius = context.getDimens(com.intuit.sdp.R.dimen._20sdp)
        }

        when(val image = item.imageLink){
            is Int -> {
                binding.ivDisplay.setImageResource(image)
            }

            is Uri -> {
                Glide.with(binding.root)
                    .load(image)
                    .error(R.drawable.place_holder_image)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(binding.ivDisplay)
            }
            else -> {

            }
        }
        binding.rootView.apply {
            strokeWidth = if (position == selectedIndex && selectedIndex != 0 ) context.getDimens(com.intuit.sdp.R.dimen._2sdp).toInt() else 0
        }
        binding.ivDisplay.clicks(withAnim = false) {
            clicks.onNext(item)
        }

    }
}