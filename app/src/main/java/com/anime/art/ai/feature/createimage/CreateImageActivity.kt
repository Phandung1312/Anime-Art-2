package com.anime.art.ai.feature.createimage


import android.content.Intent
import android.os.Bundle
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.anime.art.ai.R
import com.anime.art.ai.common.ConfigApp
import com.anime.art.ai.common.Constraint
import com.anime.art.ai.common.cropBase64Image
import com.anime.art.ai.common.extension.back
import com.anime.art.ai.common.extension.startFinalize
import com.anime.art.ai.common.processAndSaveImage
import com.anime.art.ai.common.widget.transformer.ZoomInTransformer
import com.anime.art.ai.data.Preferences
import com.anime.art.ai.data.db.query.CreatorDao
import com.anime.art.ai.databinding.ActivityCreateImageBinding
import com.anime.art.ai.domain.model.config.Creator
import com.anime.art.ai.domain.model.config.ImageGenerationRequest
import com.anime.art.ai.domain.repository.AIApiRepository
import com.anime.art.ai.domain.repository.ServerApiRepository
import com.anime.art.ai.feature.createimage.adapter.PreviewAdapter
import com.anime.art.ai.feature.dialog.ExitDialog
import com.anime.art.ai.feature.finalize.LoadingDialog
import com.anime.art.ai.feature.iap.IAPActivity
import com.anime.art.ai.inject.sinkin.UpdateCreditRequest
import com.basic.common.base.LsActivity
import com.basic.common.extension.clicks
import com.basic.common.extension.getDeviceId
import com.basic.common.extension.isNetworkAvailable
import com.basic.common.extension.makeToast
import com.basic.common.extension.transparent
import com.basic.common.extension.tryOrNull
import com.google.android.material.card.MaterialCardView
import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.autoDispose
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CreateImageActivity : LsActivity<ActivityCreateImageBinding>(ActivityCreateImageBinding::inflate) {

    @Inject lateinit var previewAdapter : PreviewAdapter
    @Inject lateinit var aiApiRepository: AIApiRepository
    @Inject lateinit var configApp: ConfigApp
    @Inject lateinit var serverApiRepository: ServerApiRepository
    @Inject lateinit var preferences: Preferences

    @Inject lateinit var creatorDao : CreatorDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transparent()
        setContentView(binding.root)

        initView()
        initObservable()
        initData()
        listenerView()
    }
    private val pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {

        }

        override fun onPageSelected(position: Int) {
            previewAdapter.selectedIndex = position
        }

        override fun onPageScrollStateChanged(state: Int) {

        }
    }


    private fun listenerView() {
        binding.close.clicks {
            onGiveUp()
        }
        binding.finalizeView.clicks {
            if(binding.viewPager.currentItem == 0 || preferences.isPremium.get()){
                configApp.url = previewAdapter.data[binding.viewPager.currentItem].url
                configApp.imageGenerationRequest.let { image ->
                    val creator = Creator(image = configApp.url,
                        prompt = image.prompt,
                        negative = image.negativePrompt,
                        artStyle = image.artStyle,
                        ratio = image.ratio)
                    lifecycleScope.launch(Dispatchers.IO) {
                        creatorDao.inserts(creator)
                        launch(Dispatchers.Main) {
                            startFinalize()
                            finish()
                        }
                    }
                }
            }
            else{
                makeToast("This template is only for premium package")
            }
        }
        binding.variationsView.clicks {
            lifecycleScope.launch{
                val imageGenerationRequest =  configApp.imageGenerationRequest
                if(isNetworkAvailable()){
                    generateImage(imageGenerationRequest, isMakeVariations = true)
                }
                else{
                   makeToast("Please check your connect internet !")
                }
            }
        }
        onBackPressedDispatcher.addCallback {
            onGiveUp()
        }
    }
    private fun onGiveUp(){
        val exitDialog = ExitDialog(
            "Do you want to give up" ,
            "Give Up"
        ){
            back()
        }
        exitDialog.show(supportFragmentManager, null)
    }
    override fun onResume() {
        binding.viewPager.registerOnPageChangeCallback(pageChangeCallback)
        super.onResume()
    }

    private suspend fun generateImage(imageGenerationRequest : ImageGenerationRequest, isMakeVariations : Boolean){

        lifecycleScope.launch(Dispatchers.IO) {
            aiApiRepository.generateImage(imageGenerationRequest){ progress ->
                launch(Dispatchers.Main) {
                    when (progress){
                        is AIApiRepository.APIResponse.Loading ->{
                            setCardViewClickable(clickable = false,binding.variationsView, binding.finalizeView )
                            binding.loadingLayout.isVisible = true
                            binding.viewPager.isVisible = false
                            binding.lottieView
                                .animate()
                                .alpha(1f)
                                .setDuration(1000)
                                .start()
                            binding.lottieView.playAnimation()
                        }
                        is AIApiRepository.APIResponse.Success ->{
                                launch(Dispatchers.IO) {
                                    val request = UpdateCreditRequest(if(isMakeVariations) Constraint.Info.MAKE_VARIATIONS_COST.toLong() * -1 else Constraint.Info.CREATE_ART_WORK_COST.toLong() * -1, Constraint.CREATE_ARTWORK)
                                    serverApiRepository.updateCredit(getDeviceId(),request){ result ->
                                        if(result){
                                        }
                                        else{
                                            launch(Dispatchers.Main) {
                                                makeToast("An error has occurred")
                                            }
                                        }
                                    }
                                }
                            binding.loadingLayout.isVisible = false
                            binding.viewPager.apply {
                                this.orientation = ViewPager2.ORIENTATION_HORIZONTAL
                                this.setPageTransformer(ZoomInTransformer())
                                this.adapter = previewAdapter.apply {

                                    this.data = progress.responses
                                }
                            }
                            binding.viewPager.isVisible = true
                            setCardViewClickable(clickable = true,binding.variationsView, binding.finalizeView )
                        }
                        is AIApiRepository.APIResponse.Error ->{
                            binding.loadingLayout.isVisible = false
                            makeToast("An error has occurred")
                                delay(100)
                                back()
                        }
                    }
                }
            }
        }
    }

    private fun initData() {
        lifecycleScope.launch{
             val imageGenerationRequest =  configApp.imageGenerationRequest
            generateImage(imageGenerationRequest, isMakeVariations = false)
        }
    }
    private fun setCardViewClickable(clickable : Boolean,vararg cardViews: MaterialCardView){
        cardViews.forEach {view ->
            view.isEnabled = clickable
        }
    }

    private fun initObservable() {
        previewAdapter
            .unlockClicks
            .autoDispose(scope())
            .subscribe {
                val intent = Intent(this , IAPActivity::class.java)
                startActivity(intent)
                tryOrNull { overridePendingTransition(R.anim.slide_in_left, R.anim.nothing) }
            }
        previewAdapter
            .zoomClicks
            .autoDispose(scope())
            .subscribe { image ->
                configApp.url = image
                startActivity(Intent(this, EnlargeImageActivity::class.java))
                tryOrNull { overridePendingTransition(R.anim.slide_in_left, R.anim.nothing) }
            }
        previewAdapter
            .saveClicks
            .autoDispose(scope())
            .subscribe{image ->
                lifecycleScope.launch {
                    val loadingDialog = LoadingDialog()
                    loadingDialog.show(supportFragmentManager, null)
                    delay(500)
                    processAndSaveImage(this@CreateImageActivity, image){ result ->
                        if(result){
                            loadingDialog.cancel()
                        }
                        else{
                            loadingDialog.dismiss()
                            makeToast("Save failed")
                        }
                    }
                }

            }
        preferences.isPremium.asObservable().autoDispose(scope()).subscribe {
            previewAdapter.isPremium = it
        }
    }

    private fun initView() {
        setCardViewClickable(clickable = false,binding.variationsView, binding.finalizeView )
    }
}