package com.anime.art.ai.feature.main.create.adapter

import android.view.View
import android.view.ViewGroup
import com.anime.art.ai.R
import com.anime.art.ai.common.extension.gradient
import com.anime.art.ai.databinding.ItemCharacterAppearanceBinding
import com.anime.art.ai.domain.model.CharacterAppearance
import com.basic.common.base.LsAdapter
import com.basic.common.extension.clicks
import com.basic.common.extension.getColorCompat
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import javax.inject.Inject


class CharAppAdapter @Inject constructor() : LsAdapter<CharacterAppearance, ItemCharacterAppearanceBinding>(ItemCharacterAppearanceBinding::inflate) {
    val clicks: Subject<CharacterAppearance> = PublishSubject.create()
    var selectedIndex = 0
        set(value) {
            if (field == value){
                return
            }
            field.takeIf { it != -1 }?.let { notifyItemChanged(it) }
            value.takeIf { it != -1 }?.let { notifyItemChanged(it) }
            field = value
        }
    override fun bindItem(
        item: CharacterAppearance,
        binding: ItemCharacterAppearanceBinding,
        position: Int
    ) {
        binding.display.text = item.display
        val layoutParams = binding.root.layoutParams as ViewGroup.MarginLayoutParams
        val marginStartResId = if (position == 0) {
            com.intuit.sdp.R.dimen._10sdp
        } else {
            com.intuit.sdp.R.dimen._1sdp
        }
        val marginEndResId = if (position == itemCount - 1) {
            com.intuit.sdp.R.dimen._1sdp
        } else {
            com.intuit.sdp.R.dimen._2sdp
        }
        layoutParams.marginStart = binding.root.context.resources.getDimensionPixelSize(marginStartResId)
        layoutParams.marginEnd = binding.root.context.resources.getDimensionPixelSize(marginEndResId)

        if(selectedIndex == position) {
            binding.display.gradient(R.color.yellow, R.color.dark_yellow)
            }
            else{
            binding.display.gradient(R.color.white, R.color.white)
            }


        binding.underline.visibility = if(selectedIndex == position) View.VISIBLE else View.GONE
        binding.viewCharApp.clicks(withAnim = false) {
            clicks.onNext(item)
        }
    }
}