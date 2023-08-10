package com.anime.art.ai.feature.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import androidx.fragment.app.DialogFragment
import com.anime.art.ai.R
import com.anime.art.ai.databinding.DialogExitBinding
import com.basic.common.extension.clicks

class ExitDialog(
    val title : String,
    val positiveText : String,
    val positive : () -> Unit,
) : DialogFragment() {
    private lateinit var binding : DialogExitBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogExitBinding.inflate(inflater, container, false)
        initView()
        listenerView()
        return binding.root
    }
    fun initView(){
        binding.title.text = title
        binding.tvPositive.text = positiveText
    }
    private fun listenerView(){
        binding.positiveView.clicks(withAnim = false) {
            positive.invoke()
            dismiss()
        }

        binding.negativeView.clicks(withAnim = false) {
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        val layoutParams = dialog?.window?.attributes
        layoutParams?.width = (resources.displayMetrics.widthPixels * 0.95).toInt()
        dialog?.window?.attributes = layoutParams
        dialog?.window?.setBackgroundDrawableResource(R.color.transparent)
       dialog?.window?.setGravity(android.view.Gravity.BOTTOM)
    }
}