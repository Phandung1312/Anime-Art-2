package com.anime.art.ai.feature.main.create

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.anime.art.ai.R
import com.anime.art.ai.databinding.ActivityEnterPromptBinding
import com.anime.art.ai.domain.model.CharacterAppearance
import com.anime.art.ai.domain.model.Tag
import com.anime.art.ai.feature.gallery.GalleryActivity
import com.anime.art.ai.feature.main.create.adapter.CharAppAdapter
import com.anime.art.ai.feature.main.create.adapter.TagAdapter
import com.basic.common.base.LsActivity
import com.basic.common.extension.clicks
import com.basic.common.extension.hideKeyboard
import com.basic.common.extension.makeToast
import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.autoDispose
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class EnterPromptActivity :
    LsActivity<ActivityEnterPromptBinding>(ActivityEnterPromptBinding::inflate) {
    companion object {
        const val IS_PROMPT = "IS_PROMPT"
        const val PROMPT_EXTRA = "PROMPT_EXTRA"
        const val NEGATIVE_PROMPT_EXTRA = "NEGATIVE_PROMPT_EXTRA"
        const val RESULT_PROMPT = -3
        const val RESULT_NEGATIVE_PROMPT = -4
    }
    private val isPrompt by lazy { intent.getBooleanExtra(IS_PROMPT, true) }
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
        listenerView()
        initData()
    }

    @SuppressLint("SetTextI18n")
    private fun listenerView() {
        binding.edEnterPrompt.doOnTextChanged { text, _, _, _ ->
            binding.tvLengthPrompt.text = "${text?.length}/1000"
            binding.tvClear.apply {
                isEnabled = text?.isNotEmpty() == true
                setTextColor(context.getColor(if(text?.isNotEmpty() == true) R.color.white else com.widget.R.color.textTertiaryDark))
            }
            }

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
                if (isCanFinish) {
                    returnData()
                }
            }
        }
        binding.edEnterPrompt.addTextChangedListener (
            object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(p0: Editable?) {
                    val enteredText = p0.toString()
                    if (enteredText.length > 1000) {
                        binding.edEnterPrompt.text?.delete(1000, enteredText.length)
                        makeToast("Text is full box")
                    }
                }

            })
        onBackPressedDispatcher.addCallback {
            returnData()
        }
    }


    private fun returnData() {
        hideKeyboard()
        val prompt = binding.edEnterPrompt.text.toString()
        if (isPrompt)  {
            intent.putExtra(PROMPT_EXTRA, prompt)
            setResult(RESULT_PROMPT, intent)
        }
        else  {
            intent.putExtra(NEGATIVE_PROMPT_EXTRA, prompt)
            setResult(RESULT_NEGATIVE_PROMPT, intent)
        }
        finish()
    }

    private fun initView() {
       if(isPrompt){
           binding.apply {
               rvCharApps.adapter = charAppAdapter
               rvTags.adapter = tagAdapter
           }
           binding.edEnterPrompt.hint = "Enter prompt"
       }
        else{
           binding.edEnterPrompt.hint = "Enter negative prompt"
           binding.bottomView.setCardBackgroundColor(getColor(com.widget.R.color.backgroundDark))
       }
        binding.edEnterPrompt.requestFocus()
    }


    private fun initObservable() {
        if(isPrompt){
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
    }

    private fun initData() {
        var text : String? = ""
        if(isPrompt){
            charAppAdapter.data = CharacterAppearance.values().toList()
            tagAdapter.data = Tag.values().toList().filter { it.cAId == 1 }
             text = intent.getStringExtra(PROMPT_EXTRA)
        }
        else{
            text = intent.getStringExtra(NEGATIVE_PROMPT_EXTRA)
        }
        binding.edEnterPrompt.setText(text)
        binding.edEnterPrompt.setSelection(binding.edEnterPrompt.text.length)
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
        if (binding.edEnterPrompt.text.length + tag.length + 1 > 1000) {
            makeToast("Text is full box")
            return
        }
        binding.edEnterPrompt.text?.apply {
            if (isNotEmpty()) append(",")
            append(tag)
        }
    }

}