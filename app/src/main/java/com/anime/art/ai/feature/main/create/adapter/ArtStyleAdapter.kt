package com.anime.art.ai.feature.main.create.adapter

import android.view.ViewGroup
import com.anime.art.ai.R
import com.anime.art.ai.databinding.ItemArtStyleBinding
import com.anime.art.ai.domain.model.ArtStyle
import com.basic.common.base.LsAdapter
import com.basic.common.extension.clicks
import com.basic.common.extension.getColorCompat
import com.basic.common.extension.getDimens
import com.basic.common.extension.resolveAttrColor
import javax.inject.Inject

class ArtStyleAdapter @Inject constructor(): LsAdapter<ArtStyle, ItemArtStyleBinding>(ItemArtStyleBinding::inflate) {

    private var selectedIndex = -1
        set(value) {
            if (field == value){
                return
            }
            field.takeIf { it != -1 }?.let { notifyItemChanged(it) }
            value.takeIf { it != -1 }?.let { notifyItemChanged(it) }
            field = value
        }
    override fun bindItem(item: ArtStyle, binding: ItemArtStyleBinding, position: Int) {
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

        binding.display.text = item.artStyleName
        val imageResourceId = binding.root.context.resources.getIdentifier(item.sourceImage,"drawable",binding.root.context.packageName)
        binding.preview.setImageResource(imageResourceId)

        binding.viewPreview.apply {
            strokeWidth = if (position == selectedIndex) context.getDimens(com.intuit.sdp.R.dimen._2sdp).toInt() else 0
        }

        binding.viewPreview.clicks { selectedIndex = position }
    }
}