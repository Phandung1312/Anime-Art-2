package com.anime.art.ai.feature.main.create.adapter

import com.anime.art.ai.R
import com.anime.art.ai.databinding.ItemControlNetBinding
import com.anime.art.ai.domain.model.ControlNet
import com.basic.common.base.LsAdapter
import com.basic.common.extension.clicks
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import javax.inject.Inject

class ControlNetAdapter @Inject constructor() : LsAdapter<ControlNet, ItemControlNetBinding>(ItemControlNetBinding::inflate) {
    val click : Subject<ControlNet> = PublishSubject.create()
    init {
        data = ControlNet.values().toList()
    }
    var isPremium = false
    private var selectedIndex = -1
        set(value) {
            if (field == value){
                return
            }
            field.takeIf { it != -1 }?.let { notifyItemChanged(it) }
            value.takeIf { it != -1 }?.let { notifyItemChanged(it) }
            field = value
        }

    override fun bindItem(item: ControlNet, binding: ItemControlNetBinding, position: Int) {
        binding.tvDisplay.text = item.controlNetName
        val imageResourceId = binding.root.context.resources.getIdentifier(item.sourceImage,"drawable",binding.root.context.packageName)
        binding.ivControlNet.setImageResource(imageResourceId)

        binding.checkbox.setImageResource(if(selectedIndex == position) R.drawable.raido_checked else R.drawable.radio_unchecked)
        binding.rootView.clicks(withAnim = false) {
            if (isPremium) selectedIndex = position
            click.onNext(item)
        }
    }
}