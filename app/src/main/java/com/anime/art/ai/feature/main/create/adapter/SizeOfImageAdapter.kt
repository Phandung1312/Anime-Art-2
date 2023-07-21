package com.anime.art.ai.feature.main.create.adapter

import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsClient.getPackageName
import com.anime.art.ai.R
import com.anime.art.ai.databinding.ItemSizeOfImageBinding
import com.anime.art.ai.domain.model.SizeOfImage
import com.basic.common.base.LsAdapter
import com.basic.common.extension.clicks
import com.basic.common.extension.getColorCompat
import com.basic.common.extension.getDimens
import com.basic.common.extension.resolveAttrColor
import com.bumptech.glide.Glide
import javax.inject.Inject

class SizeOfImageAdapter @Inject constructor(): LsAdapter<SizeOfImage,ItemSizeOfImageBinding>(ItemSizeOfImageBinding::inflate) {
    private var selectedIndex = 0
        set(value) {
            if (field == value){
                return
            }
            field.takeIf { it != -1 }?.let { notifyItemChanged(it) }
            value.takeIf { it != -1 }?.let { notifyItemChanged(it) }
            field = value
        }
    override fun bindItem(item: SizeOfImage, binding: ItemSizeOfImageBinding, position: Int) {
        val layoutParams = binding.root.layoutParams as ViewGroup.MarginLayoutParams
        val marginStartResId = if (position == 0) {
            com.intuit.sdp.R.dimen._10sdp
        } else {
            com.intuit.sdp.R.dimen._2sdp
        }
        val marginEndResId = if (position == itemCount - 1) {
            com.intuit.sdp.R.dimen._10sdp
        } else {
            com.intuit.sdp.R.dimen._2sdp
        }
        layoutParams.marginStart = binding.root.context.resources.getDimensionPixelSize(marginStartResId)
        layoutParams.marginEnd = binding.root.context.resources.getDimensionPixelSize(marginEndResId)


        val imageResourceId = binding.root.context.resources.getIdentifier(item.describeImage,"drawable",binding.root.context.packageName)
        binding.ivSize.setImageResource(imageResourceId)

        binding.cardView.apply {
            strokeWidth = if (position == selectedIndex) context.getDimens(com.intuit.sdp.R.dimen._1sdp).toInt() else 0
            setCardBackgroundColor(if (position == selectedIndex) context.getColorCompat(R.color.yellow_black) else context.resolveAttrColor(android.R.attr.colorBackground))
        }
        binding.cardView.clicks { selectedIndex = position }
    }
}