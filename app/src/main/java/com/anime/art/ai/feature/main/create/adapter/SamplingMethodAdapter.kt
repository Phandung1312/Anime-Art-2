package com.anime.art.ai.feature.main.create.adapter


import com.anime.art.ai.R
import com.anime.art.ai.common.extension.margin
import com.anime.art.ai.databinding.ItemTagBinding
import com.anime.art.ai.domain.model.SamplingMethod
import com.basic.common.base.LsAdapter
import com.basic.common.extension.clicks
import com.basic.common.extension.getColorCompat
import com.basic.common.extension.getDimens
import com.basic.common.extension.resolveAttrColor
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import javax.inject.Inject

class SamplingMethodAdapter @Inject constructor() :
    LsAdapter<SamplingMethod, ItemTagBinding>(ItemTagBinding::inflate) {
    val clicks: Subject<SamplingMethod> = PublishSubject.create()
    private var selectedIndex = -1
        set(value) {
            if (field == value){
                return
            }
            field.takeIf { it != -1 }?.let { notifyItemChanged(it) }
            value.takeIf { it != -1 }?.let { notifyItemChanged(it) }
            field = value
        }
    init {
        selectedIndex = 0
    }
    override fun bindItem(item: SamplingMethod, binding: ItemTagBinding, position: Int) {
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
        binding.root.margin(marginStartResId = marginStartResId, marginEndResId = marginEndResId)
        binding.viewTag.apply {
            if(position == selectedIndex) {
                setBackgroundResource(R.drawable.stroke_gradient_yellow_25)
            }
            else{
                setBackgroundColor(context.getColor(R.color.light_gray))
            }
        }

        binding.display.text = item.display

        binding.cardView.clicks(withAnim = false) { selectedIndex = position
        clicks.onNext(item)}
    }
}