package com.anime.art.ai.feature.main.create

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.anime.art.ai.databinding.ActivityEnterPromptBinding
import com.anime.art.ai.domain.model.CharacterAppearance
import com.anime.art.ai.domain.model.Tag
import com.anime.art.ai.feature.main.create.adapter.CharAppAdapter
import com.anime.art.ai.feature.main.create.adapter.TagAdapter
import com.basic.common.base.LsActivity
import com.basic.common.extension.clicks
import com.basic.common.extension.hideKeyboard
import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.autoDispose
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class EnterPromptActivity :
    LsActivity<ActivityEnterPromptBinding>(ActivityEnterPromptBinding::inflate) {
    companion object {
        const val PROMPT_EXTRA = "PROMPT_EXTRA"
    }

    @Inject
    lateinit var charAppAdapter: CharAppAdapter
    @Inject
    lateinit var tagAdapter: TagAdapter

    var isCanFinish = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
        initObservable()
        initData()
        listenerView()
    }

    private fun listenerView() {
        binding.tvClear.clicks(withAnim = false) {
            binding.edEnterPrompt.text?.clear()
        }

        binding.root.clicks(withAnim = false) {
            returnData()
        }
        //handle keyboard
        binding.root.viewTreeObserver.addOnGlobalLayoutListener {
            val heightDiff = binding.root.rootView.height - binding.root.height
            if (heightDiff > 200) {
                isCanFinish = true
            } else {
               if(isCanFinish){
                   finish()
               }
            }
        }
    }


    private fun returnData() {
        val prompt = binding.edEnterPrompt.text.toString()
        intent.putExtra(PROMPT_EXTRA, prompt)
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun initView() {
        binding.apply {
            rvCharApps.adapter = charAppAdapter
            rvTags.adapter = tagAdapter
        }
        binding.edEnterPrompt.requestFocus()
    }


    private fun initObservable() {
        charAppAdapter
            .clicks
            .autoDispose(scope())
            .subscribe { charApp ->
                charAppAdapter.selectedIndex = charAppAdapter.data.indexOf(charApp)
                tagAdapter.data = Tag.values().toList().filter { it.cAId == charApp.id }
                tagAdapter.resetSelectedIndex()

                scrollToMiddle()
            }

        tagAdapter
            .clicks
            .autoDispose(scope())
            .subscribe { tag ->
                appendEnterPromptText(tag.display)
            }
    }

    private fun initData() {
        charAppAdapter.data = CharacterAppearance.values().toList()
        tagAdapter.data = Tag.values().toList().filter { it.cAId == 1 }

        val prompt = intent.getStringExtra(PROMPT_EXTRA)
        binding.edEnterPrompt.setText(prompt)
    }

    private fun scrollToMiddle() {
        val layoutManager = binding.rvCharApps.layoutManager as LinearLayoutManager

        val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()
        val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

        val position = charAppAdapter.selectedIndex

        if (position in firstVisibleItem..lastVisibleItem) {
            val centerView = layoutManager.findViewByPosition(position)
            val centerViewWidth = centerView?.width ?: 0
            val centerX = binding.rvCharApps.width / 2 - centerViewWidth / 2
            binding.rvCharApps.smoothScrollBy(centerView!!.left - centerX, 0)
        } else {
            binding.rvCharApps.smoothScrollToPosition(position)
        }

    }

    private fun appendEnterPromptText(tag: String) {

        binding.edEnterPrompt.text?.apply {
            if (isNotEmpty()) append(",")
            append("(${tag}:1.3)")
        }
    }

}