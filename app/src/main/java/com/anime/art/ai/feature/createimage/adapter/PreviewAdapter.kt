package com.anime.art.ai.feature.createimage.adapter


import android.graphics.drawable.Drawable
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import com.anime.art.ai.R
import com.anime.art.ai.databinding.ItemPreviewInCreateImageBinding
import com.anime.art.ai.domain.model.response.ImagePreview
import com.basic.common.base.LsAdapter
import com.basic.common.extension.clicks
import com.basic.common.extension.isNetworkAvailable
import com.basic.common.extension.makeToast
import com.basic.common.util.ViewUtils.gone
import com.basic.common.util.ViewUtils.show
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import javax.inject.Inject

class PreviewAdapter @Inject constructor(): LsAdapter<ImagePreview, ItemPreviewInCreateImageBinding>(ItemPreviewInCreateImageBinding::inflate) {
    val unlockClicks : Subject<Unit> = PublishSubject.create()
    val zoomClicks : Subject<String> = PublishSubject.create()
    val saveClicks : Subject<String> = PublishSubject.create()
    val isReadySubject : Subject<Boolean> = PublishSubject.create()
    var isPremium = false
        set(value){
            field = value
            notifyItemRangeChanged(
                1,data.size - 1
            )
        }

    var selectedIndex = 0
        set(value){
            if(field == value) return
            field.takeIf { it != -1 }?.let { notifyItemChanged(it) }
            value.takeIf { it != -1 }?.let { notifyItemChanged(it) }
            field = value
        }
    override fun bindItem(item: ImagePreview, binding: ItemPreviewInCreateImageBinding, position: Int) {
        ConstraintSet().apply {
            this.clone(binding.rootView)
            this.setDimensionRatio(binding.cardView.id, item.ratio)
            this.applyTo(binding.rootView)
        }
        Glide.with(binding.root.context)
            .load(item.url)
            .error(R.drawable.place_holder_image)
            .transition(DrawableTransitionOptions.withCrossFade())
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.previewLayout.gone()
                    binding.errorLayout.show()
                    item.isReady = false
                    if(selectedIndex == position) isReadySubject.onNext(item.isReady)
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.previewLayout.show()
                    binding.errorLayout.gone()
                    if(position == 0 || isPremium){
                        binding.saveView.isVisible = true
                        binding.zoomView.isVisible = true
                        binding.unlock.isVisible = false
                    }
                    else{
                        binding.unlock.isVisible = true
                    }
                    binding.tickView.isVisible = (position == selectedIndex && isPremium) || position == 0
                    item.isReady = true
                    if(selectedIndex == position) isReadySubject.onNext(item.isReady)
                    return false
                }
            })
            .into(binding.preview)

        binding.unlock.clicks {
            unlockClicks.onNext(Unit)
        }
        binding.saveView.clicks {
            saveClicks.onNext(item.url)
        }
        binding.zoomView.clicks {
            zoomClicks.onNext(item.url)
        }
        binding.errorLayout.clicks {
            if(binding.rootView.context.isNetworkAvailable()){
                binding.errorLayout.gone()
                binding.previewLayout.show()
                notifyItemChanged(position)
            }
            else{
                binding.rootView.context.makeToast("Please check your connect internet!")
            }
        }
    }


}