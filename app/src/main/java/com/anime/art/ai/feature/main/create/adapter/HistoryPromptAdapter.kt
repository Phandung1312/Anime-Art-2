package com.anime.art.ai.feature.main.create.adapter

import androidx.core.view.isVisible
import com.anime.art.ai.R
import com.anime.art.ai.common.extension.gradient
import com.anime.art.ai.databinding.ItemHistoryPromptBinding
import com.anime.art.ai.domain.model.config.Prompt
import com.basic.common.base.LsAdapter
import com.basic.common.extension.clicks
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import javax.inject.Inject

class HistoryPromptAdapter @Inject constructor() : LsAdapter<Prompt, ItemHistoryPromptBinding>(ItemHistoryPromptBinding::inflate) {
    var clicks : Subject<Prompt> = PublishSubject.create()
    var selectedId = -1
        set(value) {
            if (field == value){
                return
            }
            field.takeIf { it != -1 }?.let { notifyItemChanged(it) }
            value.takeIf { it != -1 }?.let { notifyItemChanged(it) }
            field = value
        }
    override fun bindItem(item: Prompt, binding: ItemHistoryPromptBinding, position: Int) {

        binding.display.text = item.text
        binding.rootView.clicks(withAnim = false) {
            selectedId = item.id
            clicks.onNext(item)
        }
        if(selectedId == item.id ){
            binding.display.gradient(R.color.colorSecondary, R.color.colorPrimary)
            binding.display.requestLayout()
        }
        else{
            binding.display.gradient(R.color.textPrimary, R.color.textPrimary)
        }
        binding.ivUnderLine.isVisible = (position != data.size - 1 )
    }

}