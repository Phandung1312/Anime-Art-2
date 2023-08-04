package com.anime.art.ai.feature.main.create.adapter

import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsClient.getPackageName
import com.anime.art.ai.R
import com.anime.art.ai.common.extension.margin
import com.anime.art.ai.databinding.ItemSizeOfImageBinding
import com.anime.art.ai.domain.model.SizeOfImage
import com.basic.common.base.LsAdapter
import com.basic.common.extension.clicks
import com.basic.common.extension.getColorCompat
import com.basic.common.extension.getDimens
import com.basic.common.extension.resolveAttrColor
import com.bumptech.glide.Glide
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import javax.inject.Inject

class SizeOfImageAdapter @Inject constructor(): LsAdapter<SizeOfImage,ItemSizeOfImageBinding>(ItemSizeOfImageBinding::inflate) {
    val clicks : Subject<SizeOfImage> = PublishSubject.create()
    var selectedIndex = 0
        set(value) {
            if (field == value){
                return
            }
            field.takeIf { it != -1 }?.let { notifyItemChanged(it) }
            value.takeIf { it != -1 }?.let { notifyItemChanged(it) }
            field = value
        }
    override fun bindItem(item: SizeOfImage, binding: ItemSizeOfImageBinding, position: Int) {
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
        val imageResourceId = binding.root.context.resources.getIdentifier(item.describeImage,"drawable",binding.root.context.packageName)
        binding.ivSize.setImageResource(imageResourceId)
        binding.sizeLayout.apply {
            if(position == selectedIndex) {
                setBackgroundResource(R.drawable.stroke_gradient_yellow_25)
            }
            else{
                setBackgroundColor(context.getColor(R.color.background_dark_gray))
            }

        }
        binding.cardView.clicks { selectedIndex = position
        clicks.onNext(item)}
    }
}