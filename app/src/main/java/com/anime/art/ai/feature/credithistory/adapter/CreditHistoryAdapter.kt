package com.anime.art.ai.feature.credithistory.adapter

import com.anime.art.ai.databinding.ItemCreditHistoryBinding
import com.anime.art.ai.domain.model.config.History
import com.basic.common.base.LsAdapter
import javax.inject.Inject

class CreditHistoryAdapter @Inject constructor(): LsAdapter<History, ItemCreditHistoryBinding>(ItemCreditHistoryBinding::inflate) {
    override fun bindItem(item: History, binding: ItemCreditHistoryBinding, position: Int) {

        binding.credit.text = item.credit.toString()
        binding.tvTitle.text = item.title
        binding.tvTimeCreate.text = item.createdAt
    }

}