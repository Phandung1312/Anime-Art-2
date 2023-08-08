package com.anime.art.ai.feature.finalize


import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Base64
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.FileProvider
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
import com.basic.common.extension.saveBase64ImageToGallery
import com.basic.common.extension.transparent
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
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
            val targetWidthRatio = configApp.imageGenerationRequest.ratio.split(":")[0].toFloat()
            val targetHeightRatio = configApp.imageGenerationRequest.ratio.split(":")[1].toFloat()
            processAndSaveImage(this, configApp.imageBase64, targetWidthRatio/targetHeightRatio)
            makeToast("Image saved to gallery")
        }
        binding.share.clicks {
            val bitmap = decodeBase64ToBitmap(configApp.imageBase64)
            if (bitmap != null) {
                shareImage(bitmap)
            } else {
                makeToast("An error has occurred")
            }
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
        binding.tvArtStyle.gradient(com.anime.art.ai.R.color.yellow, com.anime.art.ai.R.color.dark_yellow)
        binding.tvPrompt.text = configApp.imageGenerationRequest.prompt
    }

    private fun initObservable() {

    }

    private fun initView() {
        showImage()
    }

    private fun showImage(){
        ConstraintSet().apply {
            this.clone(binding.rootView)
            this.setDimensionRatio(binding.previewView.id, configApp.imageGenerationRequest.ratio)
            this.applyTo(binding.rootView)
        }
        val decodedBytes: ByteArray = Base64.decode(configApp.imageBase64, Base64.DEFAULT)
        val dataUrl = "data:image/jpeg;base64," + Base64.encodeToString(decodedBytes, Base64.DEFAULT)
        Glide.with(binding.root.context)
            .load(dataUrl)
            .error(com.anime.art.ai.R.drawable.place_holder_image)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.iv)
    }
    private fun copyToClipBoard(text : String ){
        val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", text)
        clipboard.setPrimaryClip(clip)
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
        val bitmap = decodeBase64ToBitmap(configApp.imageBase64)
        if (bitmap != null) {
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
        } else {
            makeToast("An error has occurred")
        }
    }
}