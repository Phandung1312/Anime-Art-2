package com.anime.art.ai.feature.main.gallery.adapter

import com.anime.art.ai.databinding.ItemDailyRewardBinding
import com.anime.art.ai.domain.model.DailyReward
import com.basic.common.base.LsAdapter
import javax.inject.Inject

class DailyRewardAdapter @Inject constructor() : LsAdapter<DailyReward, ItemDailyRewardBinding>(ItemDailyRewardBinding::inflate)  {
    override fun bindItem(item: DailyReward, binding: ItemDailyRewardBinding, position: Int) {
        binding.tv.text = "+${item.reward}"
    }
}