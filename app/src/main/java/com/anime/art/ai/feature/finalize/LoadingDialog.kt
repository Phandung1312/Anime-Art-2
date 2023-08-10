package com.anime.art.ai.feature.finalize

import android.animation.Animator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.LottieDrawable
import com.anime.art.ai.R
import com.anime.art.ai.databinding.DialogLoadingBinding
import com.basic.common.extension.makeToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoadingDialog : DialogFragment() {
    private lateinit var  binding : DialogLoadingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogLoadingBinding.inflate(inflater, container, false)
        dialog?.setCanceledOnTouchOutside(false)
        initView()
        return binding.root
    }

    private fun initView(){
        binding.loadingView.setAnimation(R.raw.download)
        binding.loadingView.repeatCount = LottieDrawable.INFINITE
        binding.loadingView.playAnimation()
    }

    fun cancel(){
        binding.loadingView.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator) {
            }

            override fun onAnimationEnd(p0: Animator) {
            }

            override fun onAnimationCancel(p0: Animator) {
            }

            override fun onAnimationRepeat(p0: Animator) {
                binding.loadingView.cancelAnimation()
                lifecycleScope.launch {
                    playSecondAnimation()
                }
            }
        })
    }
    private suspend fun playSecondAnimation() {
        withContext(Dispatchers.Main) {
            binding.loadingView.setAnimation(R.raw.loadsuccessful)
            binding.loadingView.repeatCount = 0
            binding.loadingView.playAnimation()
            binding.loadingView.addAnimatorListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator) {
                }

                override fun onAnimationEnd(p0: Animator) {
                    lifecycleScope.launch {
                        delay(500)
                        binding.loadingView.isVisible = false
                        requireContext().makeToast("Image saved to gallery")
                        dismiss()
                    }
                }

                override fun onAnimationCancel(p0: Animator) {
                }

                override fun onAnimationRepeat(p0: Animator) {
                }
            })
        }
    }
    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog?.window?.setBackgroundDrawableResource(R.color.transparent)
    }

}