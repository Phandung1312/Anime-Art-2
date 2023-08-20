package com.anime.art.ai.feature.finalize


import android.content.ClipData
import android.content.ClipboardManager
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.lifecycleScope
import com.anime.art.ai.R
import com.anime.art.ai.common.ConfigApp
import com.anime.art.ai.common.decodeBase64ToBitmap
import com.anime.art.ai.common.extension.back
import com.anime.art.ai.common.extension.gradient
import com.anime.art.ai.common.processAndSaveImage
import com.anime.art.ai.common.saveBitmapToFile
import com.anime.art.ai.data.db.query.CreatorDao
import com.anime.art.ai.databinding.ActivityFinalizeBinding
import com.basic.common.base.LsActivity
import com.basic.common.extension.clicks
import com.basic.common.extension.makeToast
import com.basic.common.extension.saveImageToGallery
import com.basic.common.extension.saveStringToFile
import com.basic.common.extension.transparent
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.OutputStream
import javax.inject.Inject


@AndroidEntryPoint
class FinalizeActivity : LsActivity<ActivityFinalizeBinding>(ActivityFinalizeBinding::inflate) {
    @Inject lateinit var configApp: ConfigApp
    @Inject lateinit var creatorDao: CreatorDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transparent()
        setContentView(binding.root)
        initView()
        initObservable()
        initData()
        listenerView()
    }

    private fun listenerView() {
        binding.close.clicks {
            back()
        }
        binding.copyPromptView.clicks(withAnim = false) {
            copyToClipBoard(configApp.imageGenerationRequest.prompt)
        }
        binding.save.clicks {
            saveImage()
        }
        binding.share.clicks {
            val bitmap = binding.iv.drawable.toBitmap()
            shareImage(bitmap)
        }
        binding.instagram.clicks {
           shareImageOnApp("com.instagram.android", "Instagram")
        }
        binding.whatsApp.clicks {
            shareImageOnApp("com.whatsapp", "Whats app")
        }
        binding.facebook.clicks {
            shareImageOnApp("com.facebook.katana", "Facebook")
        }
    }

    private fun initData() {
        binding.tvArtStyle.text = configApp.imageGenerationRequest.artStyle
        binding.tvArtStyle.gradient(R.color.yellow, R.color.dark_yellow)
        binding.tvPrompt.text = configApp.imageGenerationRequest.prompt
    }

    private fun initObservable() {

    }

    private fun initView() {
        showImage()
    }
    private fun saveImage(){
        lifecycleScope.launch {
            val loadingDialog = LoadingDialog()
            loadingDialog.show(supportFragmentManager, null)
            delay(500)
            Glide.with(this@FinalizeActivity)
                .asBitmap()
                .load(configApp.url)
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        this@FinalizeActivity.saveImageToGallery(resource){result ->
                            if(result){
                                loadingDialog.cancel()
                            }
                            else{
                                loadingDialog.dismiss()
                                makeToast("Save failed")
                            }
                        }
                    }
                })


        }
    }
    private fun showImage(){
        ConstraintSet().apply {
            this.clone(binding.rootView)
            this.setDimensionRatio(binding.previewView.id, configApp.imageGenerationRequest.ratio)
            this.applyTo(binding.rootView)
        }

        Glide.with(binding.root.context)
            .load(configApp.url)
            .error(R.drawable.place_holder_image)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.iv)
    }
    private fun copyToClipBoard(text : String ){
        val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", text)
        clipboard.setPrimaryClip(clip)
        makeToast("Already copied text")
    }

    private fun shareImage(bitmap : Bitmap){
        val file = saveBitmapToFile(this, bitmap)
        if (file != null && file.exists()) {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "image/png"
            val uri = FileProvider.getUriForFile(this, "$packageName.provider", file)
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            startActivity(Intent.createChooser(intent, "Share Image using..."))
        } else {
            makeToast("Failed to create image file")
        }
    }
    private fun shareImageOnApp( appPackage : String, appName : String){
        val bitmap = binding.iv.drawable.toBitmap()
        try {
            val imageFile = saveBitmapToFile(this, bitmap)
            if(imageFile != null && imageFile.exists()){
                val imageUri = FileProvider.getUriForFile(this, "$packageName.provider", imageFile)
                val feedIntent = Intent(Intent.ACTION_SEND)
                feedIntent.type = "image/*"
                feedIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
                feedIntent.setPackage(appPackage)
                startActivity(feedIntent)
            }
        } catch (e: Exception) {
            makeToast("$appName is not installed")
        }
    }

}