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
import com.anime.art.ai.common.extension.back
import com.anime.art.ai.common.extension.startFinalize
import com.anime.art.ai.common.widget.transformer.ZoomInTransformer
import com.anime.art.ai.data.Preferences
import com.anime.art.ai.data.db.query.CreatorDao
import com.anime.art.ai.databinding.ActivityCreateImageBinding
import com.anime.art.ai.domain.model.config.Creator
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
import com.basic.common.extension.saveImageToGallery
import com.basic.common.extension.transparent
import com.basic.common.extension.tryOrNull
import com.basic.common.util.ViewUtils.gone
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.autoDispose
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class CreateImageActivity : LsActivity<ActivityCreateImageBinding>(ActivityCreateImageBinding::inflate) {

    @Inject lateinit var previewAdapter : PreviewAdapter
    @Inject lateinit var aiApiRepository: AIApiRepository
    @Inject lateinit var configApp: ConfigApp
    @Inject lateinit var serverApiRepository: ServerApiRepository
    @Inject lateinit var preferences: Preferences

    @Inject lateinit var creatorDao : CreatorDao

    var numCallApiLimit = 3
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
            binding.apply {
                showLeft.isVisible = position > 0
                showRight.isVisible = position < previewAdapter.data.size - 1
            }
            val isReady = previewAdapter.data[position].isReady
  //          setCardViewClickable(clickable = isReady,binding.variationsView, binding.finalizeView )
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
            numCallApiLimit = 3
            if (isNetworkAvailable()) {
                generateImage(isMakeVariations = true)
            } else {
                makeToast("Please check your connect internet!")
            }
        }
        onBackPressedDispatcher.addCallback {
            onGiveUp()
        }
        binding.apply {
            showLeft.clicks {
                if (viewPager.currentItem > 0) {
                    viewPager.currentItem -= 1
                }
            }
            showRight.clicks {
                if (viewPager.currentItem < previewAdapter.data.size - 1) {
                    viewPager.currentItem += 1
                }
            }
        }
    }
    private fun onGiveUp(){
        val exitDialog = ExitDialog(
            "Do you want to give up?" ,
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

    private fun generateImage(isMakeVariations : Boolean){
        lifecycleScope.launch(Dispatchers.IO) {
            val request = UpdateCreditRequest(
                if (isMakeVariations) Constraint.Info.MAKE_VARIATIONS_COST.toLong() * -1 else Constraint.Info.CREATE_ART_WORK_COST.toLong() * -1,
                Constraint.CREATE_ARTWORK
            )
            serverApiRepository.updateCredit(
                getDeviceId(),
                request
            ) { result ->
                launch(Dispatchers.Main) {
                    if (result) {
                        val imageGenerationRequest =  configApp.imageGenerationRequest
                        lifecycleScope.launch(Dispatchers.IO) {
                            aiApiRepository.generateImage(imageGenerationRequest){ progress ->
                                launch(Dispatchers.Main) {
                                    when (progress) {
                                        is AIApiRepository.APIResponse.Loading -> {
                                            setCardViewClickable(
                                                clickable = false,
                                                binding.variationsView,
                                                binding.finalizeView
                                            )
                                            binding.apply {
                                                showLeft.gone()
                                                showRight.gone()
                                                loadingLayout.isVisible = true
                                                viewPager.isVisible = false
                                                lottieView
                                                    .animate()
                                                    .alpha(1f)
                                                    .setDuration(1000)
                                                    .start()
                                                lottieView.playAnimation()
                                            }
                                        }

                                        is AIApiRepository.APIResponse.Success -> {
                                            binding.loadingLayout.isVisible = false
                                            binding.viewPager.apply {
                                                this.orientation =
                                                    ViewPager2.ORIENTATION_HORIZONTAL
                                                this.setPageTransformer(ZoomInTransformer())
                                                this.adapter = previewAdapter.apply {

                                                    this.data = progress.responses
                                                }
                                            }
                                            binding.viewPager.isVisible = true
                                            setCardViewClickable(
                                                clickable = true,
                                                binding.variationsView,
                                                binding.finalizeView
                                            )
                                        }
                                        is AIApiRepository.APIResponse.Error ->{
                                            binding.loadingLayout.isVisible = false
                                            makeToast("An error has occurred, please try again")
                                            if(! isMakeVariations){
                                                delay(200)
                                                back()
                                            }
                                            else{
                                                setCardViewClickable(clickable = true,binding.variationsView, binding.finalizeView )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        if(numCallApiLimit > 0) {
                            generateImage(isMakeVariations)
                            numCallApiLimit--
                        }
                        else{
                            makeToast("Your network is unstable, please try again!")
                            delay(200)
                            back()
                        }
                    }
                }
            }
        }



    }

    private fun initData() {
        generateImage(isMakeVariations = false)
    }
    private fun setCardViewClickable(clickable : Boolean,vararg cardViews: MaterialCardView){
        cardViews.forEach {view ->
            view.isEnabled = clickable
            view.alpha = if(clickable) 1f else 0.5f
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
            .subscribe{ image ->
                lifecycleScope.launch(Dispatchers.Main) {
                    val loadingDialog = LoadingDialog()
                    loadingDialog.show(supportFragmentManager, null)
                    delay(500)
                    try {
                        val job = async {
                            withContext(Dispatchers.IO) {
                                Glide.with(this@CreateImageActivity)
                                    .asBitmap()
                                    .load(image)
                                    .submit()
                                    .get()
                            }
                        }
                        val resource = job.await()
                        this@CreateImageActivity.saveImageToGallery(resource) { result ->
                            if (result) {
                                loadingDialog.cancel()
                            } else {
                                loadingDialog.dismiss()
                                makeToast("Save failed")
                            }
                        }
                    } catch (e: Exception) {
                        loadingDialog.dismiss()
                        makeToast("Save failed")
                    }

                }

            }
        preferences.isPremium.asObservable().autoDispose(scope()).subscribe {
            previewAdapter.isPremium = it
        }
        previewAdapter.isReadySubject.autoDispose(scope())
            .subscribe { isReady ->
                setCardViewClickable(clickable = isReady,binding.variationsView, binding.finalizeView )
            }
    }

    private fun initView() {
        setCardViewClickable(clickable = false,binding.variationsView, binding.finalizeView )
    }
}