package com.anime.art.ai.feature.finalize


import android.R
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.Toast
import androidx.core.content.FileProvider
import com.anime.art.ai.common.ConfigApp
import com.anime.art.ai.common.decodeBase64ToBitmap
import com.anime.art.ai.common.extension.back
import com.anime.art.ai.common.extension.gradient
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
import java.io.ByteArrayOutputStream
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
            copyToClipBoard(configApp.imageGenerationRequest?.prompt!!)
        }
        binding.save.clicks {
            saveBase64ImageToGallery(configApp.imageBase64)
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
            val bitmap = decodeBase64ToBitmap(configApp.imageBase64)
            if (bitmap != null) {
                shareImageOnInstagram(bitmap)
            } else {
                makeToast("An error has occurred")
            }
        }
        binding.whatsApp.clicks {
            val bitmap = decodeBase64ToBitmap(configApp.imageBase64)
            if (bitmap != null) {
                shareImageToWhatsApp(bitmap)
            } else {
                makeToast("An error has occurred")
            }
        }
    }

    private fun initData() {
        binding.tvArtStyle.text = configApp.imageGenerationRequest?.artStyle
        binding.tvArtStyle.gradient(com.anime.art.ai.R.color.yellow, com.anime.art.ai.R.color.dark_yellow)
        binding.tvPrompt.text =  configApp.imageGenerationRequest?.prompt
    }

    private fun initObservable() {

    }

    private fun initView() {
        showImage()
    }

    private fun showImage(){
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
            Toast.makeText(this, "Failed to create image file", Toast.LENGTH_SHORT).show()
        }
    }
    private fun shareImageToWhatsApp(bitmap: Bitmap){
        val imageUri = getImageUri(bitmap)
        val sendIntent = Intent(Intent.ACTION_SEND)
        sendIntent.type = "image/*"
        sendIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
        sendIntent.setPackage("com.whatsapp")

        try {
            startActivity(sendIntent)
        } catch (e: Exception) {
            makeToast("Whats app is not installed")
        }
    }
    private fun shareImageOnInstagram(bitmap: Bitmap){
        try{
            val imageUri = getImageUri(bitmap)
            val feedIntent = Intent(Intent.ACTION_SEND)
            feedIntent.type = "image/*"
            feedIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
            feedIntent.setPackage("com.instagram.android")

            val storiesIntent = Intent("com.instagram.share.ADD_TO_STORY")
            storiesIntent.setDataAndType(imageUri, "jpg")
            storiesIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            storiesIntent.setPackage("com.instagram.android")

            grantUriPermission(
                "com.instagram.android", imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            val chooserIntent = Intent.createChooser(feedIntent, "abc")
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(storiesIntent))
            startActivity(chooserIntent)
        }
        catch (e : Exception){
            makeToast("Instagram is not installed")
        }
    }
    private fun getImageUri(bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(contentResolver, bitmap, "Title", null)
        return Uri.parse(path)
    }
}