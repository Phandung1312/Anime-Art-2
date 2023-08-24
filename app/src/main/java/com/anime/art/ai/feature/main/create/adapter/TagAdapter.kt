package com.anime.art.ai.feature.main.create.adapter


import com.anime.art.ai.R
import com.anime.art.ai.common.extension.margin
import com.anime.art.ai.databinding.ItemTagBinding
import com.anime.art.ai.domain.model.Tag
import com.basic.common.base.LsAdapter
import com.basic.common.extension.clicks
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import javax.inject.Inject

class TagAdapter @Inject constructor() :
    LsAdapter<Tag, ItemTagBinding>(ItemTagBinding::inflate) {
    val clicks: Subject<Tag> = PublishSubject.create()
    private var selectedIndex = -1
        set(value) {
            if (field == value){
                return
            }
            field.takeIf { it != -1 }?.let { notifyItemChanged(it) }
            value.takeIf { it != -1 }?.let { notifyItemChanged(it) }
            field = value
        }
    override fun bindItem(item: Tag, binding: ItemTagBinding, position: Int) {
        val marginStartResId = if (position == 0 || position == 1) {
            com.intuit.sdp.R.dimen._10sdp
        } else {
            com.intuit.sdp.R.dimen._2sdp
        }
        val marginEndResId = if (position == itemCount - 1 || position == itemCount - 2) {
            com.intuit.sdp.R.dimen._10sdp
        } else {
            com.intuit.sdp.R.dimen._2sdp
        }
        binding.root.margin(marginStartResId = marginStartResId, marginEndResId = marginEndResId)
        binding.viewTag.apply {
            if(position == selectedIndex) {
                setBackgroundResource(R.drawable.stroke_gradient_yellow_25)
            }
            else{
                setBackgroundColor(context.getColor(R.color.background_tag))
            }

        }
        binding.display.text = item.display

        binding.cardView.clicks(withAnim = false) {
            selectedIndex = position
            clicks.onNext(item)
        }


    }
    fun resetSelectedIndex(){
        selectedIndex = -1
    }
}