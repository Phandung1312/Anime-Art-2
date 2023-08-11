package com.anime.art.ai.feature.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.anime.art.ai.R
import com.anime.art.ai.databinding.DialogNotifyNetworkBinding
import com.basic.common.extension.clicks

class NotifyNetworkDialog(
   private val  positive : () -> Unit,
    private val negative : () -> Unit
) : DialogFragment() {
    private lateinit var binding : DialogNotifyNetworkBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogNotifyNetworkBinding.inflate(inflater, container, false)
        listenerView()
        return binding.root
    }

    fun listenerView(){
        binding.retryView.clicks(withAnim = false) {
            positive.invoke()
            dismiss()
        }

        binding.skipView.clicks(withAnim = false) {
            negative.invoke()
            dismiss()
        }
    }
    override fun onStart() {
        super.onStart()
        val layoutParams = dialog?.window?.attributes
        layoutParams?.width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        dialog?.window?.attributes = layoutParams
        dialog?.window?.setBackgroundDrawableResource(R.color.transparent)
        dialog?.window?.setGravity(android.view.Gravity.CENTER_VERTICAL)
    }
}