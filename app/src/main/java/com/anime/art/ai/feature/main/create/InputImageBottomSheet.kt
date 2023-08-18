package com.anime.art.ai.feature.main.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anime.art.ai.R
import com.anime.art.ai.data.Preferences
import com.anime.art.ai.databinding.BottomSheetInputImageBinding
import com.basic.common.extension.clicks
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class InputImageBottomSheet : BottomSheetDialogFragment() {
    private lateinit var binding : BottomSheetInputImageBinding
    @Inject
    lateinit var preferences: Preferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetInputImageBinding.inflate(inflater, container, false)
        listenerView()
        preferences.isFirstInputImage.set(false)
        return binding.root
    }

    private fun listenerView() {
        binding.agreeView.clicks(withAnim = false) {
            dismiss()
        }
    }
    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog?.window?.setBackgroundDrawableResource(R.drawable.rounded_bottom_sheet_dialog)
    }

}