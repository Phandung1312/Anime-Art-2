package com.anime.art.ai.feature.main.create.adapter

import android.view.ViewGroup
import com.anime.art.ai.R
import com.anime.art.ai.common.extension.margin
import com.anime.art.ai.databinding.ItemArtStyleBinding
import com.anime.art.ai.domain.model.ArtStyle
import com.basic.common.base.LsAdapter
import com.basic.common.extension.clicks
import com.basic.common.extension.getColorCompat
import com.basic.common.extension.getDimens
import com.basic.common.extension.resolveAttrColor
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
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
    init {
        selectedIndex = 0
    }
    val click : Subject<ArtStyle> = PublishSubject.create()
    override fun bindItem(item: ArtStyle, binding: ItemArtStyleBinding, position: Int) {
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
        binding.root.margin(marginStartResId = marginStartResId, marginEndResId = marginEndResId)

        val imageResourceId = binding.root.context.resources.getIdentifier(item.sourceImage,"drawable",binding.root.context.packageName)
        binding.preview.setImageResource(imageResourceId)
        binding.rootView.apply {
            if (position == selectedIndex) setBackgroundResource(R.drawable.stroke_gradient_yellow_20)
            else setBackgroundColor(context.getColor(com.widget.R.color.backgroundDark))
        }
        binding.display.text = item.artStyleName
        binding.viewPreview.clicks(withAnim =  false) { selectedIndex = position
        click.onNext(item)}
    }
}