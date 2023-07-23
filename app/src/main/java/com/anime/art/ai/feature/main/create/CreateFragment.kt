package com.anime.art.ai.feature.main.create

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.text.SpannableStringBuilder
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.text.underline
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionManager
import com.anime.art.ai.R
import com.anime.art.ai.common.extension.gradient
import com.anime.art.ai.common.extension.startCreateImage
import com.anime.art.ai.data.db.query.PromptDao
import com.anime.art.ai.databinding.FragmentCreateBinding
import com.anime.art.ai.domain.model.ArtStyle
import com.anime.art.ai.domain.model.Character
import com.anime.art.ai.domain.model.CharacterAppearance
import com.anime.art.ai.domain.model.SamplingMethod
import com.anime.art.ai.domain.model.SizeOfImage
import com.anime.art.ai.domain.model.Tag
import com.anime.art.ai.domain.model.config.InputImage
import com.anime.art.ai.domain.model.config.Prompt
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
import com.basic.common.extension.getDimens
import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.autoDispose
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.roundToInt

@AndroidEntryPoint
class CreateFragment: LsFragment<FragmentCreateBinding>(FragmentCreateBinding::inflate) {
    @Inject lateinit var promptDao: PromptDao

    //Adapter
    @Inject lateinit var menuAdapter : MenuAdapter
    @Inject lateinit var sizeOfImageAdapter: SizeOfImageAdapter
    @Inject lateinit var artStyleAdapter: ArtStyleAdapter
    @Inject lateinit var charAppAdapter: CharAppAdapter
    @Inject lateinit var tagAdapter : TagAdapter
    @Inject lateinit var samplingMethodAdapter : SamplingMethodAdapter
    @Inject lateinit var controlNetAdapter: ControlNetAdapter
    @Inject lateinit var inputImageAdapter: InputImageAdapter


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
    override fun onViewCreated() {
        initView()
        initData()
        listenerView()
    }

    private fun animateHideOrShowControlNet(isShowed: Boolean){
        activity?.let {activity ->
            val dp200 = activity.getDimens(com.intuit.sdp.R.dimen._200sdp).toInt()
            val dpFull = controlNetAdapter.data.size * activity.getDimens(com.intuit.sdp.R.dimen._100sdp).toInt()
            val valueAnimator = ValueAnimator.ofInt(
                if (isShowed) dp200 else dpFull,
                if (isShowed) dpFull else dp200
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
        super.onResume()
    }
    private fun initView(){
        binding.tvTitle.gradient(R.color.yellow, R.color.dark_yellow)
        binding.tvShowControlNet.gradient(R.color.yellow, R.color.dark_yellow)
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
    }
    private fun initInputImageData(){
        inputImages  = arrayListOf(
            InputImage(R.drawable.input_image, null),
            InputImage(R.drawable.sample_input_image_1, 0.5f),
            InputImage(R.drawable.sample_input_image_2, 0.5f),
            InputImage(R.drawable.sample_input_image_3, 0.5f)
        )
        inputImageAdapter.data = inputImages
    }
    private fun listenerView(){
        //Slider
        binding.sliderScale.addOnChangeListener { _, value, _ ->
            val roundedNumber = roundToNearestHalf(value.toDouble())
            binding.tvCfgScale.text = roundedNumber.toString()
        }
        binding.sliderStep.addOnChangeListener { _, value, _ ->
            val roundedNumber = roundToNearestHalf(value.toDouble())
            binding.tvSamplingStep.text = roundedNumber.toString()
        }
        //Advanced setting
        binding.advancedSetting.clicks(withAnim = false) {
            binding.ivAdvancedSetting.setImageResource(if(isShowSetting) R.drawable.arrow_up else R.drawable.arrow_down)
            TransitionManager.beginDelayedTransition(binding.advancedSettingView)
            isShowSetting = !isShowSetting
            binding.advancedSettingView.isVisible = isShowSetting
            isShowMore = false
        }
        binding.tvShowControlNet.clicks {
            isShowMore = !isShowMore
        }

        //Image Weight Slider
        binding.sliderWeight.addOnChangeListener { _, value, _ ->
            var inputImage = inputImageAdapter.data[inputImageAdapter.selectedIndex]
            val roundedNumber = roundToNearestTenth(value.toDouble())
            binding.tvWeight.text = roundedNumber.toString()
            inputImage.weight = roundedNumber.toFloat()
            inputImages[inputImageAdapter.selectedIndex] = inputImage
            inputImageAdapter.data = inputImages
        }

        binding.tvClear.clicks {
            binding.edEnterPrompt.text?.clear()
        }

        binding.edEnterPrompt.clicks(withAnim = false) {
            openEnterPrompt()
        }
        binding.enterPromptView.clicks(withAnim = false) {
            openEnterPrompt()
        }
        binding.historyPromptView.clicks {
            val intent = Intent(activity, HistoryPromptActivity::class.java)
            intent.putExtra(HistoryPromptActivity.PROMPT_INDEX, selectedPromptId)
           getPromptFromHistoryResult.launch(intent)
        }
        binding.createView.clicks {
            createImage()
        }
    }
    private fun initObservable(){
        charAppAdapter
            .clicks
            .autoDispose(scope())
            .subscribe { charApp ->
                charAppAdapter.selectedIndex = charAppAdapter.data.indexOf(charApp)
                tagAdapter.data = Tag.values().toList().filter{ it.cAId == charApp.id}
                tagAdapter.resetSelectedIndex()

                scrollToMiddle()
            }
        inputImageAdapter
            .clicks
            .autoDispose(scope())
            .subscribe{inputImage ->
                val selectedIndex = inputImageAdapter.data.indexOf(inputImage)
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
        tagAdapter
            .clicks
            .autoDispose(scope())
            .subscribe { tag->
                appendEnterPromptText(tag.display)
            }
    }

    private fun appendEnterPromptText(tag : String) {

        binding.edEnterPrompt.text?.apply {
            if(isNotEmpty()) append(",")
            append("(${tag}:1.3)")
        }
    }
    private fun openEnterPrompt(){
        val prompt = binding.edEnterPrompt.text.toString()
        val intent = Intent(activity, EnterPromptActivity::class.java)
        intent.putExtra(EnterPromptActivity.PROMPT_EXTRA, prompt)
        enterPromptResult.launch(intent)

    }
    private fun openGallery(){
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

    private fun roundToNearestHalf(number: Double): Double {
        return (number * 2).roundToInt() / 2.0
    }
    private fun roundToNearestTenth(number: Double): Double {
        return (number * 10).roundToInt() / 10.0
    }
    private val enterPromptResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->
        if(result.resultCode == Activity.RESULT_OK){
            val promptResult =  result.data?.getStringExtra(EnterPromptActivity.PROMPT_EXTRA)
            binding.edEnterPrompt.setText(promptResult)
        }
    }

    private val getPromptFromHistoryResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if(result.resultCode == Activity.RESULT_OK){
            selectedPromptId = result.data?.getIntExtra(HistoryPromptActivity.PROMPT_INDEX, -1) ?: -1
            binding.edEnterPrompt.setText(result.data?.getStringExtra(HistoryPromptActivity.PROMPT))
        }

    }

    private fun createImage(){

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val prompt : String  = binding.edEnterPrompt.text.toString()
                promptDao.inserts(Prompt(text = prompt))
                lifecycleScope.launch(Dispatchers.IO) {
                    activity?.startCreateImage()
                }
            }
            catch (e : Exception){
                Timber.e("Insert prompt error")
            }
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private val pickImageFromGalleryResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if(result.resultCode == Activity.RESULT_OK){
            result.data?.data?.let {
                lifecycleScope.launch(Dispatchers.Main){
                    val inputImage = InputImage(it, 0.5f)
                    inputImages.add(1, inputImage)
                     inputImageAdapter.data = inputImages
                    inputImageAdapter.notifyDataSetChanged()
                    inputImageAdapter.clicks.onNext(inputImage)
                }
            }
        }
    }

}