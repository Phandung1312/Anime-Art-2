package com.anime.art.ai.feature.main.create

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Instrumentation.ActivityResult
import android.content.Intent
import android.text.SpannableStringBuilder
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.text.underline
import androidx.core.view.isVisible
import androidx.core.view.size
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionManager
import com.anime.art.ai.R
import com.anime.art.ai.common.ItemRvClickListener
import com.anime.art.ai.databinding.FragmentCreateBinding
import com.anime.art.ai.domain.model.ArtStyle
import com.anime.art.ai.domain.model.Character
import com.anime.art.ai.domain.model.CharacterAppearance
import com.anime.art.ai.domain.model.ControlNet
import com.anime.art.ai.domain.model.SamplingMethod
import com.anime.art.ai.domain.model.SizeOfImage
import com.anime.art.ai.domain.model.Tag
import com.anime.art.ai.domain.model.config.InputImage
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
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.autoDispose
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.round
import kotlin.math.roundToInt

@AndroidEntryPoint
class CreateFragment: LsFragment<FragmentCreateBinding>(FragmentCreateBinding::inflate) {
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
            controlNetAdapter.data  = if(value) ControlNet.values().toList() else ControlNet.values().toList().take(2)
            field = value
        }
    private var inputImages : MutableList<InputImage> = ArrayList()
    override fun onViewCreated() {
        initView()
        initData()
        listenerView()
    }

    override fun onResume() {
        initObservable()
        super.onResume()
    }
    private fun initView(){
        binding.apply {
            rvCharacters.adapter = menuAdapter
            rvRatio.adapter = sizeOfImageAdapter
            rvArtStyles.adapter = artStyleAdapter
            rvCharApps.adapter = charAppAdapter
            rvTags.adapter = tagAdapter
            rvSamplingMethods.adapter = samplingMethodAdapter
            rvControlNet.adapter = controlNetAdapter
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
            TransitionManager.beginDelayedTransition(binding.advancedSettingView)
            isShowSetting = !isShowSetting

            binding.ivAdvancedSetting.setImageResource(if(isShowSetting) R.drawable.arrow_up else R.drawable.arrow_down)
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
    }
    private fun openGallery(){
        Dexter.withContext(activity).withPermission(
            android.Manifest.permission.READ_MEDIA_IMAGES,
        ).withListener(object  : PermissionListener{
            override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                    type ="image/*"
                }
                pickImageFromGalleryResult.launch(intent)
            }

            override fun onPermissionDenied(p0: PermissionDeniedResponse?) {

            }

            override fun onPermissionRationaleShouldBeShown(
                p0: PermissionRequest?,
                p1: PermissionToken?
            ) {

            }
        }).onSameThread().check()
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