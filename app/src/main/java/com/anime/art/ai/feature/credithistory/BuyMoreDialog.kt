package com.anime.art.ai.feature.credithistory

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.anime.art.ai.R
import com.anime.art.ai.common.Constraint
import com.anime.art.ai.data.Preferences
import com.anime.art.ai.databinding.DialogBuyMoreBinding
import com.anime.art.ai.domain.model.CreditPackage
import com.anime.art.ai.domain.repository.ServerApiRepository
import com.anime.art.ai.inject.sinkin.UpdateCreditRequest
import com.basic.common.extension.clicks
import com.basic.common.extension.getDeviceId
import com.basic.common.extension.getDimens
import com.basic.common.extension.isNetworkAvailable
import com.basic.common.extension.makeToast
import com.google.android.material.card.MaterialCardView
import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.autoDispose
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BuyMoreDialog(
    val callback : () -> Unit,
) : DialogFragment() {
    private lateinit var  binding : DialogBuyMoreBinding
    @Inject lateinit var preferences: Preferences
    @Inject lateinit var serverApiRepository: ServerApiRepository
    private var selectedCreditPackage = 0
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
        initData()
        return binding.root
    }

    private fun initView() {
        selectedCreditPackage = 5
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
           layout?.setBackgroundColor(requireContext().getColor(R.color.backgroundSecondary))
       }
    }
    private fun initListener() {
        binding.firstCreditView.clicks(withAnim = false) {
            selectedCreditPackage = 1
        }
        binding.secondCreditView.clicks(withAnim = false) {
            selectedCreditPackage = 2
        }
        binding.thirdCreditView.clicks(withAnim = false) {
            selectedCreditPackage = 3
        }
        binding.fourthCreditView.clicks(withAnim = false) {
            selectedCreditPackage = 4
        }
        binding.fifthCreditView.clicks(withAnim = false) {
            selectedCreditPackage = 5
        }
        binding.close.clicks {
            dismiss()
        }
        binding.continueView.clicks {
            binding.continueView.isEnabled = false
            if(requireContext().isNetworkAvailable()){
                lifecycleScope.launch(Dispatchers.IO) {
                    val credit = CreditPackage.values()[selectedCreditPackage - 1].credit
                    serverApiRepository.updateCredit(requireContext().getDeviceId(),
                        UpdateCreditRequest(credit = credit.toLong(), Constraint.PURCHASED_CREDITS)
                    ){ result ->
                        launch(Dispatchers.Main) {
                            if(result){
                                requireContext().makeToast("Buy credit successfully")
                            }
                            else{
                                requireContext().makeToast("An error has occurred")
                            }
                            delay(100)
                            dismiss()
                        }
                    }
                }
            }
            else{
                requireContext().makeToast("Please check your connect internet !")
                binding.continueView.isEnabled = true
            }
        }

    }
    private fun initData(){
        preferences.creditAmount.asObservable().autoDispose(scope()).subscribe {creditAmount ->
            binding.tvCreditAmount.text = creditAmount.toString()
        }
    }
    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog?.window?.setBackgroundDrawableResource(R.color.transparent)
        dialog?.window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        dialog?.window?.navigationBarColor = ContextCompat.getColor(requireContext(), com.widget.R.color.backgroundDark)
    }

    override fun onDismiss(dialog: DialogInterface) {
        callback.invoke()
        super.onDismiss(dialog)
    }
}