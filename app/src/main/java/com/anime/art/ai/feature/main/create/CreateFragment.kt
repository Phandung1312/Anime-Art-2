package com.anime.art.ai.feature.main.create

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.text.underline
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anime.art.ai.R
import com.anime.art.ai.common.ConfigApp
import com.anime.art.ai.common.Constraint
import com.anime.art.ai.common.cropBase64Image
import com.anime.art.ai.common.extension.enableScrollText
import com.anime.art.ai.common.extension.gradient
import com.anime.art.ai.common.extension.startCreateImage
import com.anime.art.ai.common.extension.startIAP
import com.anime.art.ai.common.removeWhitespace
import com.anime.art.ai.common.resizeBase64Image
import com.anime.art.ai.data.Preferences
import com.anime.art.ai.data.db.query.PromptDao
import com.anime.art.ai.databinding.FragmentCreateBinding
import com.anime.art.ai.domain.model.ArtStyle
import com.anime.art.ai.domain.model.Character
import com.anime.art.ai.domain.model.CharacterAppearance
import com.anime.art.ai.domain.model.SamplingMethod
import com.anime.art.ai.domain.model.SizeOfImage
import com.anime.art.ai.domain.model.Tag
import com.anime.art.ai.domain.model.config.ImageGenerationRequest
import com.anime.art.ai.domain.model.config.InputImage
import com.anime.art.ai.domain.model.config.Prompt
import com.anime.art.ai.feature.credithistory.BuyMoreDialog
import com.anime.art.ai.feature.main.create.adapter.ArtStyleAdapter
import com.anime.art.ai.feature.main.create.adapter.CharAppAdapter
import com.anime.art.ai.feature.main.create.adapter.ControlNetAdapter
import com.anime.art.ai.feature.main.create.adapter.InputImageAdapter
import com.anime.art.ai.feature.main.create.adapter.MenuAdapter
import com.anime.art.ai.feature.main.create.adapter.SamplingMethodAdapter
import com.anime.art.ai.feature.main.create.adapter.SizeOfImageAdapter
import com.anime.art.ai.feature.main.create.adapter.TagAdapter
import com.basic.common.base.LsFragment
import com.basic.common.extension.clicks
import com.basic.common.extension.convertDrawableToBase64
import com.basic.common.extension.getDimens
import com.basic.common.extension.hideKeyboard
import com.basic.common.extension.isNetworkAvailable
import com.basic.common.extension.makeToast
import com.basic.common.extension.resizeImageToFit
import com.basic.common.extension.setTint
import com.basic.common.extension.tryOrNull
import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.autoDispose
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.roundToInt

@AndroidEntryPoint
class CreateFragment: LsFragment<FragmentCreateBinding>(FragmentCreateBinding::inflate) {
    companion object{
        const val PROMPT_EXTRA = "PROMPT_EXTRA"
        const val RATIO_EXTRA = "RATIO_EXTRA"
        const val NEGATIVE_PROMPT= "NEGATIVE_PROMPT"
    }
    @Inject lateinit var promptDao: PromptDao
    @Inject lateinit var preferences: Preferences
    //Adapter
    @Inject lateinit var menuAdapter : MenuAdapter
    @Inject lateinit var sizeOfImageAdapter: SizeOfImageAdapter
    @Inject lateinit var artStyleAdapter: ArtStyleAdapter
    @Inject lateinit var charAppAdapter: CharAppAdapter
    @Inject lateinit var tagAdapter : TagAdapter
    @Inject lateinit var samplingMethodAdapter : SamplingMethodAdapter
    @Inject lateinit var controlNetAdapter: ControlNetAdapter
    @Inject lateinit var inputImageAdapter: InputImageAdapter
    @Inject lateinit var configApp: ConfigApp

    private var isShowSetting : Boolean = false
    private var isShowMore : Boolean = false
        set(value) {
           val spanMore =  SpannableStringBuilder()
                .underline { append( if(value) context?.getString(R.string.Hide) else context?.getString(R.string.See_more)) }
            binding.tvShowControlNet.text = spanMore
            animateHideOrShowControlNet(isShowed = value)
            field = value
        }
    private var inputImages : MutableList<InputImage> = ArrayList()

    private var selectedPromptId = -1
    private var isEnableCreate : Subject<Boolean> = BehaviorSubject.createDefault(false)
    private var imageGenerationRequest : ImageGenerationRequest = ImageGenerationRequest()

    private var qualityImage = 2

    override fun onViewCreated() {
        initView()
        listenerView()
        initData()
    }
    private fun animateHideOrShowControlNet(isShowed: Boolean){
        activity?.let {activity ->
            val dp300 = activity.getDimens(com.intuit.sdp.R.dimen._300sdp).toInt()
            val dpFull = controlNetAdapter.data.size * activity.getDimens(com.intuit.sdp.R.dimen._100sdp).toInt()
            val valueAnimator = ValueAnimator.ofInt(
                if (isShowed) dp300 else dpFull,
                if (isShowed) dpFull else dp300
            )
            valueAnimator.duration = 1000L
            valueAnimator.addUpdateListener {
                val animatedValue = valueAnimator.animatedValue as Int
                binding.rvControlNet.updateLayoutParams<ViewGroup.LayoutParams> {
                    this.height = animatedValue
                }
            }
            valueAnimator.start()
        }
    }

    override fun onResume() {
        initObservable()
        binding.edEnterPrompt.setText(configApp.localPrompt)
        binding.edNegativePrompt.setText(configApp.negativePrompt)
        binding.edEnterPrompt.setSelection(binding.edEnterPrompt.text.length)
        if(preferences.isFirstLogin.get()){
            binding.controlNetLayout.isVisible = true
            preferences.isFirstLogin.set(false)
        }
        super.onResume()
    }
    private fun initView(){
        binding.tvTitle.gradient(R.color.colorSecondary, R.color.colorPrimary)
        binding.tvShowControlNet.gradient(R.color.colorSecondary, R.color.colorPrimary)
        binding.apply {
            rvCharacters.adapter = menuAdapter
            rvRatio.adapter = sizeOfImageAdapter
            rvArtStyles.adapter = artStyleAdapter
            rvCharApps.adapter = charAppAdapter
            rvTags.adapter = tagAdapter
            rvSamplingMethods.adapter = samplingMethodAdapter
            rvControlNet.apply {
                this.adapter = controlNetAdapter
                this.layoutManager = object: LinearLayoutManager(binding.root.context, VERTICAL, false){
                    override fun canScrollVertically(): Boolean {
                        return false
                    }
                }
            }
            rvInputImage.adapter = inputImageAdapter
        }
    }

    private fun initData(){
        menuAdapter.data = Character.values().toList()
        sizeOfImageAdapter.data = SizeOfImage.values().toList()
        artStyleAdapter.data = ArtStyle.values().toList()
        charAppAdapter.data = CharacterAppearance.values().toList()
        tagAdapter.data = Tag.values().toList().filter { it.cAId == 1 }
        samplingMethodAdapter.data = SamplingMethod.values().toList()

        initInputImageData()

        binding.sliderScale.value = 7.5f
        binding.sliderStep.value = 25f


    }
    private fun initInputImageData(){
        inputImages  = arrayListOf(
            InputImage(R.drawable.input_image, null),
            InputImage(R.drawable.sample_input_image_1, 0.6f),
            InputImage(R.drawable.sample_input_image_2, 0.6f),
            InputImage(R.drawable.sample_input_image_3, 0.6f),
            InputImage(R.drawable.sample_input_image_4, 0.6f),
        )
        inputImageAdapter.data = inputImages
    }
    @SuppressLint("SetTextI18n")
    private fun listenerView(){
        //Slider
        binding.sliderScale.addOnChangeListener { _, value, _ ->
            val roundedNumber = roundToNearestHalf(value.toDouble())
            binding.tvCfgScale.text = roundedNumber.toString()
            imageGenerationRequest.guidance = roundedNumber
        }
        binding.sliderStep.addOnChangeListener { _, value, _ ->
            binding.tvSamplingStep.text = value.toInt().toString()
            imageGenerationRequest.steps = value.toInt()
        }
        //Advanced setting
        binding.advancedSetting.clicks(withAnim = false) {
            binding.ivAdvancedSetting.setImageResource(if(isShowSetting) R.drawable.arrow_down else R.drawable.arrow_up)

            isShowSetting = !isShowSetting
            binding.apply {
                advancedSettingLayout.isVisible = isShowSetting
                controlNetLayout.isVisible = isShowSetting
            }
        }
        isShowMore = false
        binding.tvShowControlNet.clicks {
            isShowMore = !isShowMore
        }

        //Image Weight Slider
        binding.sliderWeight.addOnChangeListener { _, value, _ ->
            if(inputImageAdapter.selectedIndex > 0){
                val inputImage = inputImageAdapter.data[inputImageAdapter.selectedIndex]
                val roundedNumber = roundToNearestTenth(value.toDouble())
                binding.tvWeight.text = roundedNumber.toString()
                inputImage.weight = roundedNumber.toFloat()
                inputImages[inputImageAdapter.selectedIndex] = inputImage
                inputImageAdapter.data = inputImages
            }
        }

        binding.tvClear.clicks {
            binding.edEnterPrompt.text.clear()
            selectedPromptId = -1
        }

        binding.edEnterPrompt.clicks(withAnim = false) {
            requireActivity().hideKeyboard()
            openEnterPrompt()
        }
        binding.enterPromptView.clicks(withAnim = false) {
            requireActivity().hideKeyboard()
            openEnterPrompt()
        }
        binding.historyPromptView.clicks {
            val intent = Intent(activity, HistoryPromptActivity::class.java)
            intent.putExtra(HistoryPromptActivity.PROMPT_INDEX, selectedPromptId)
           getPromptFromHistoryResult.launch(intent)
            tryOrNull { activity?.overridePendingTransition(R.anim.slide_in_left, R.anim.nothing) }
        }
        binding.createView.clicks(withAnim = false) {
            createImage()
        }
        binding.creditView.clicks(withAnim = false) {
            val buyMoreDialog = BuyMoreDialog()
            buyMoreDialog.show(parentFragmentManager, null)
        }
        binding.edEnterPrompt.doOnTextChanged { text, _, _, _ ->
            binding.tvLengthPrompt.text = "${text?.length}/1000"
            imageGenerationRequest.prompt = text.toString()
            configApp.localPrompt = text.toString()
            isEnableCreate.onNext(text?.isNotEmpty() == true)
        }
        binding.edNegativePrompt.doOnTextChanged { text, _, _, _ ->
            imageGenerationRequest.negativePrompt = text.toString()
        }
        binding.qualityPrompt.setOnCheckedChangeListener { _, isChecked ->
            qualityImage = if(isChecked){
                2
            } else{
                1
            }
        }
        binding.viewPremiumNegative.clicks(withAnim = false) {
            activity?.startIAP()
        }
        binding.edNegativePrompt.clicks(withAnim = false) {
            openNegativePrompt()
        }
        binding.negativeView.clicks(withAnim = false) {
            openNegativePrompt()
        }

        binding.edNegativePrompt.enableScrollText()
    }
    private fun initObservable(){
        preferences.creditAmount.asObservable().autoDispose(scope()).subscribe { creditAmount ->
            binding.tvCreditAmount.text = creditAmount.toString()
        }
        preferences
            .isPremium
            .asObservable()
            .autoDispose(scope())
            .subscribe { isPremium ->
                controlNetAdapter.isPremium = isPremium
                binding.viewPremiumNegative.isVisible = !isPremium
        }
        menuAdapter
            .clicks
            .autoDispose(scope())
            .subscribe { character ->
                binding.edEnterPrompt.setText(character.promptText)
                binding.edEnterPrompt.setSelection(binding.edEnterPrompt.text.length)
                scrollToMiddle(binding.rvCharacters, menuAdapter.data.indexOf(character))
            }
        charAppAdapter
            .clicks
            .autoDispose(scope())
            .subscribe { charApp ->
                charAppAdapter.selectedIndex = charAppAdapter.data.indexOf(charApp)
                tagAdapter.data = Tag.values().toList().filter{ it.cAId == charApp.id}
                tagAdapter.resetSelectedIndex()

                scrollToMiddle(binding.rvCharApps, charAppAdapter.selectedIndex)
            }
        inputImageAdapter
            .clicks
            .autoDispose(scope())
            .subscribe{inputImage ->
                val selectedIndex = inputImageAdapter.data.indexOf(inputImage)
                if(selectedIndex != inputImageAdapter.selectedIndex ){
                    if(selectedIndex != 0) inputImageAdapter.selectedIndex = selectedIndex
                    when(selectedIndex ){
                        0 ->{
                            openGallery()
                        }
                        else ->{
                            setImageWeight(inputImage)
                        }
                    }
                }
                else{
                    binding.imageWeightLayout.isVisible = false
                    inputImageAdapter.selectedIndex = -1
                }
            }
        tagAdapter
            .clicks
            .autoDispose(scope())
            .subscribe { tag->
                appendEnterPromptText(tag.display)
            }
        isEnableCreate
            .autoDispose(scope())
            .subscribe { isEnable ->
                binding.createView.isEnabled = isEnable
                val colorRes = if (isEnable) R.color.black else R.color.white
                val backgroundRes = if (isEnable) R.drawable.button_gradient_yellow else R.drawable.button_gradient_yellow_inactive
                binding.createLayout.setBackgroundResource(backgroundRes)
                binding.tvCreate.setTextColor(requireContext().getColor(colorRes))
                binding.tvCost.setTextColor(requireContext().getColor(colorRes))
                binding.ivCreate.setTint(requireContext().getColor(colorRes))

                binding.tvClear.apply {
                    isEnabled = isEnable
                    setTextColor(context.getColor(if(isEnable) R.color.textPrimary else R.color.textTertiary))
                }
            }

        artStyleAdapter
            .click
            .autoDispose(scope())
            .subscribe {artStyle ->
                imageGenerationRequest.model = artStyle.model
                imageGenerationRequest.artStyle = artStyle.artStyleName
                imageGenerationRequest.extraPrompt = artStyle.extraPrompt
                scrollToMiddle(binding.rvArtStyles, artStyleAdapter.data.indexOf(artStyle))
            }

        controlNetAdapter
            .click
            .autoDispose(scope())
            .subscribe { controlNet ->
                if (preferences.isPremium.get()) imageGenerationRequest.controlNet = controlNet.apiString
                else {
                    requireActivity().startIAP()
                }
            }

        sizeOfImageAdapter
            .clicks
            .autoDispose(scope())
            .subscribe {item ->
                imageGenerationRequest.ratio = item.size
                scrollToMiddle(binding.rvRatio, sizeOfImageAdapter.selectedIndex)
            }

        samplingMethodAdapter
            .clicks
            .autoDispose(scope())
            .subscribe { samplingMethod ->
                imageGenerationRequest.scheduler = samplingMethod.apiString
                scrollToMiddle(binding.rvSamplingMethods, samplingMethodAdapter.data.indexOf(samplingMethod))
            }

    }

    private fun appendEnterPromptText(tag : String) {
        binding.edEnterPrompt.text?.apply {
            if(this.length + tag.length + 1 > 1000){
                requireContext().makeToast("Text is full box")
                return
            }
            val newText = if (this.isNotEmpty()) {
                "${this},${tag}"
            } else {
                tag
            }
            binding.edEnterPrompt.setText( newText)
            binding.edEnterPrompt.setSelection(newText.length)
        }
    }
    private fun openEnterPrompt(){
        val prompt = binding.edEnterPrompt.text.toString()
        val intent = Intent(activity, EnterPromptActivity::class.java)
        intent.putExtra(EnterPromptActivity.PROMPT_EXTRA, prompt)
        enterPromptResult.launch(intent)

    }
    private fun openNegativePrompt(){
        val negativePrompt = binding.edNegativePrompt.text.toString()
        val intent = Intent(activity, EnterPromptActivity::class.java)
        intent.putExtra(EnterPromptActivity.NEGATIVE_PROMPT_EXTRA, negativePrompt)
        intent.putExtra(EnterPromptActivity.IS_PROMPT, false)
        enterPromptResult.launch(intent)
    }
    private fun openGallery(){
//        if(preferences.isFirstInputImage.get()){
//            val bottomSheet = InputImageBottomSheet()
//            bottomSheet.show(parentFragmentManager, null)
//        }
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type ="image/*"
        }
        pickImageFromGalleryResult.launch(intent)
    }
    private fun setImageWeight(inputImage: InputImage){
        binding.imageWeightLayout.isVisible = true
        inputImage.weight?.let {
            binding.tvWeight.text = it.toString()
            binding.sliderWeight.value = it
        }
    }

    private fun scrollToMiddle(recyclerView: RecyclerView, selectedIndex: Int) {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager

        val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()
        val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

        if (selectedIndex in firstVisibleItem..lastVisibleItem) {
            val centerView = layoutManager.findViewByPosition(selectedIndex)
            val centerViewWidth = centerView?.width ?: 0
            val centerX = recyclerView.width / 2 - centerViewWidth / 2
            layoutManager.scrollToPositionWithOffset(recyclerView.getChildLayoutPosition(centerView!!), centerX)
        } else {
            recyclerView.smoothScrollToPosition(selectedIndex)
        }
    }

    private fun roundToNearestHalf(number: Double): Double {
        return (number * 2).roundToInt() / 2.0
    }
    private fun roundToNearestTenth(number: Double): Double {
        return (number * 10).roundToInt() / 10.0
    }
    private val enterPromptResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->
        if(result.resultCode == EnterPromptActivity.RESULT_PROMPT){
            val promptResult =  result.data?.getStringExtra(EnterPromptActivity.PROMPT_EXTRA)
            binding.edEnterPrompt.setText(promptResult)
            binding.edEnterPrompt.setSelection(binding.edEnterPrompt.text.length)
        }
        else if(result.resultCode == EnterPromptActivity.RESULT_NEGATIVE_PROMPT){
            val promptResult =  result.data?.getStringExtra(EnterPromptActivity.NEGATIVE_PROMPT_EXTRA)
            binding.edNegativePrompt.setText(promptResult)
            binding.edNegativePrompt.setSelection(binding.edNegativePrompt.text.length)
        }
    }

    fun setDataFromGallery(prompt : String?,negativePrompt  : String?, ratio : String?){

        prompt?.let {
            binding.edEnterPrompt.setText(it)
        }
        negativePrompt?.let {
            binding.edNegativePrompt.setText(it)
        }
    }

    private val getPromptFromHistoryResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                selectedPromptId =
                    result.data?.getIntExtra(HistoryPromptActivity.PROMPT_INDEX, -1) ?: -1
                binding.edEnterPrompt.setText(result.data?.getStringExtra(HistoryPromptActivity.PROMPT))
            }

        }

    private fun createImage() {
        val inputImage =
            if (inputImageAdapter.selectedIndex > 0) inputImageAdapter.data[inputImageAdapter.selectedIndex] else null
        try {
            inputImage?.let {
                imageGenerationRequest.image = when (val image = it.imageLink) {
                    is Int -> {
                        requireContext().convertDrawableToBase64(image) ?: ""
                    }

                    is Uri -> {
                        requireContext().resizeImageToFit(image) ?: ""
                    }

                    else -> {
                        ""
                    }
                }
                imageGenerationRequest.apply {
                    val targetWidthRatio = imageGenerationRequest.ratio.split(":")[0].toFloat()
                    val targetHeightRatio = imageGenerationRequest.ratio.split(":")[1].toFloat()
                    imageGenerationRequest.image = cropBase64Image(imageGenerationRequest.image, targetWidthRatio, targetHeightRatio)
                    imageGenerationRequest.image = removeWhitespace(imageGenerationRequest.image)
                    imageGenerationRequest.strength = it.weight?.toDouble() ?: 0.5
                }
            } ?: kotlin.run {
                imageGenerationRequest.image = ""
            }
        }catch (e : Exception){
            Timber.e(e)
        }
        SizeOfImage.values().forEach { imageSize ->
            if(TextUtils.equals(imageGenerationRequest.ratio, imageSize.size)){
                imageGenerationRequest.width = imageSize.width.toInt() * qualityImage
                imageGenerationRequest.height = imageSize.height.toInt() * qualityImage
            }
        }
        if(imageGenerationRequest.image.isNotEmpty()){
            imageGenerationRequest.apply {
                image = resizeBase64Image(image, width, height)
                //requireContext().saveStringToFile("image.txt", image)
            }
        }
        if (preferences.creditAmount.get() < Constraint.Info.CREATE_ART_WORK_COST) {
            requireContext().makeToast("You don't have enough credit")
            val buyMoreDialog = BuyMoreDialog()
            buyMoreDialog.show(parentFragmentManager, null)
        } else {
            if(!requireContext().isNetworkAvailable()) {
                requireContext().makeToast("Please check your connect internet !")
                return
            }
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val prompt: String = binding.edEnterPrompt.text.toString()
                    val prompts = promptDao.getAll()
                    if (prompts.none { TextUtils.equals(it.text, prompt) }) {
                        promptDao.inserts(Prompt(text = prompt))
                    }
                    launch(Dispatchers.Main) {
                        imageGenerationRequest.apply {
                            this.prompt =extraPrompt + prompt
                        }
                        configApp.imageGenerationRequest = imageGenerationRequest
                        activity?.startCreateImage()
                    }
                } catch (e: Exception) {
                    Timber.e("Insert prompt error")
                }
            }
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private val pickImageFromGalleryResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if(result.resultCode == Activity.RESULT_OK){
            result.data?.data?.let {
                lifecycleScope.launch(Dispatchers.Main){
                    val inputImage = InputImage(it, 0.6f)
                    inputImages.add(1, inputImage)
                     inputImageAdapter.data = inputImages
                    inputImageAdapter.notifyDataSetChanged()
                    inputImageAdapter.clicks.onNext(inputImage)
                }
            }
        }
    }

}