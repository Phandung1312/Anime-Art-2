package com.anime.art.ai.feature.main.create.adapter

import android.view.ViewGroup
import com.anime.art.ai.R
import com.anime.art.ai.databinding.ItemTagBinding
import com.anime.art.ai.domain.model.Character
import com.basic.common.base.LsAdapter
import com.basic.common.extension.clicks
import com.basic.common.extension.getColorCompat
import com.basic.common.extension.getDimens
import com.basic.common.extension.resolveAttrColor
import javax.inject.Inject

class MenuAdapter @Inject constructor() :
    LsAdapter<Character, ItemTagBinding>(ItemTagBinding::inflate) {

    private var selectedIndex = -1
        set(value) {
            if (field == value){
                return
            }
            field.takeIf { it != -1 }?.let { notifyItemChanged(it) }
            value.takeIf { it != -1 }?.let { notifyItemChanged(it) }
            field = value
        }

    override fun bindItem(item: Character, binding: ItemTagBinding, position: Int) {
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

        binding.cardView.apply {
            strokeWidth = if (position == selectedIndex) context.getDimens(com.intuit.sdp.R.dimen._1sdp).toInt() else 0
            setCardBackgroundColor(if (position == selectedIndex) context.getColorCompat(R.color.yellow_black) else context.resolveAttrColor(android.R.attr.colorBackground))
        }

        layoutParams.marginStart = binding.root.context.resources.getDimensionPixelSize(marginStartResId)
        layoutParams.marginEnd = binding.root.context.resources.getDimensionPixelSize(marginEndResId)
        binding.display.text = item.characterName

        binding.cardView.clicks { selectedIndex = position }
    }
}