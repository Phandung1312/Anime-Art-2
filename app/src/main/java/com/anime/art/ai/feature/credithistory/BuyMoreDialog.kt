package com.anime.art.ai.feature.credithistory

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import com.anime.art.ai.R
import com.anime.art.ai.common.extension.gradient
import com.anime.art.ai.databinding.DialogBuyMoreBinding
import com.basic.common.extension.clicks
import com.basic.common.extension.getDimens
import com.google.android.material.card.MaterialCardView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BuyMoreDialog(
    ) : DialogFragment() {
    private lateinit var  binding : DialogBuyMoreBinding
    private var selectedPackCredit = 0
        set(value){
            if(field == value) return
            setBackground(field, isSelected = false)
            setBackground(value, isSelected = true)
            field = value
        }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogBuyMoreBinding.inflate(inflater, container, false)
        initView()
        initListener()
        return binding.root
    }

    private fun initView() {
        selectedPackCredit = 5
    }
    private fun setBackground(index : Int, isSelected : Boolean){
        var cardView : MaterialCardView? = null
        var layout : ConstraintLayout? = null
        when(index){
            1 ->{
                cardView = binding.firstCreditView
                layout = binding.firstCreditLayout
            }
            2 ->{
                cardView = binding.secondCreditView
                layout = binding.secondCreditLayout
            }
            3 ->{
                cardView = binding.thirdCreditView
                layout = binding.thirdCreditLayout
            }
            4 ->{
                cardView = binding.fourthCreditView
                layout = binding.fourthCreditLayout
            }
            5 ->{
                cardView = binding.fifthCreditView
                layout = binding.fifthCreditLayout
            }
        }
       if(isSelected){
           cardView?.strokeWidth = 0
           layout?.setBackgroundResource(R.drawable.gradient_credit)

       }
        else{
           cardView?.strokeWidth = requireContext().getDimens(com.intuit.sdp.R.dimen._1sdp).toInt()
           layout?.setBackgroundColor(requireContext().getColor(R.color.background_dark_gray))
       }
    }
    private fun initListener() {
        binding.firstCreditView.clicks(withAnim = false) {
            selectedPackCredit = 1
        }
        binding.secondCreditView.clicks(withAnim = false) {
            selectedPackCredit = 2
        }
        binding.thirdCreditView.clicks(withAnim = false) {
            selectedPackCredit = 3
        }
        binding.fourthCreditView.clicks(withAnim = false) {
            selectedPackCredit = 4
        }
        binding.fifthCreditView.clicks(withAnim = false) {
            selectedPackCredit = 5
        }
        binding.close.clicks {
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog?.window?.setBackgroundDrawableResource(R.color.transparent)
    }
}