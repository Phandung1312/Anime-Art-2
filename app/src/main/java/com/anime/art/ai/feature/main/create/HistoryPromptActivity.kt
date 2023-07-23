package com.anime.art.ai.feature.main.create


import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.anime.art.ai.data.db.query.PromptDao
import com.anime.art.ai.databinding.ActivityHistoryPromptBinding
import com.anime.art.ai.feature.main.create.adapter.HistoryPromptAdapter
import com.basic.common.base.LsActivity
import com.basic.common.extension.clicks
import com.basic.common.extension.transparent
import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.autoDispose
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HistoryPromptActivity : LsActivity<ActivityHistoryPromptBinding>(ActivityHistoryPromptBinding::inflate){
    companion object {
        const val PROMPT_INDEX = "PROMPT_INDEX"
        const val PROMPT = "PROMPT"
    }
    private val promptIndex by lazy { intent.getIntExtra(PROMPT_INDEX, -1) }
    @Inject lateinit var promptDao: PromptDao
    @Inject lateinit var historyPromptAdapter: HistoryPromptAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transparent()
        setContentView(binding.root)

        initView()
        initObservable()
        initData()
        listenerView()
    }

    private fun initView() {
        binding.rv.adapter = historyPromptAdapter
    }

    private fun initObservable() {
        historyPromptAdapter.clicks
            .autoDispose(scope())
            .subscribe{ prompt ->
                intent.putExtra(PROMPT_INDEX, prompt.id)
                intent.putExtra(PROMPT, prompt.text)
                setResult(RESULT_OK, intent)
                finish()
            }
    }

    private fun initData() {
        lifecycleScope.launch(Dispatchers.IO) {
            val prompts = promptDao.getAll()
            lifecycleScope.launch(Dispatchers.Main) {
                historyPromptAdapter.data = prompts.reversed()
                historyPromptAdapter.selectedId = promptIndex
            }
        }
    }

    private fun listenerView() {
        binding.back.clicks(withAnim = false) {
            finish()
        }
    }
}