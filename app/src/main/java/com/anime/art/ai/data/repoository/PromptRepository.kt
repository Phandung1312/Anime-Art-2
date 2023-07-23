package com.anime.art.ai.data.repoository

import com.anime.art.ai.data.db.query.PromptDao
import com.anime.art.ai.domain.model.config.Prompt
import javax.inject.Inject

class PromptRepository @Inject constructor(
    private val promptDao: PromptDao
) {

    suspend fun insertData(prompt: Prompt){
        promptDao.inserts(prompt)
    }

    suspend fun getAllPrompts(){

    }
}