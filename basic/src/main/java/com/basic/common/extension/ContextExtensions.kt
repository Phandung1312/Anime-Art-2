package com.basic.common.extension

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Uri
import android.provider.MediaStore
import android.provider.Settings
import android.util.Base64
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.DimenRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import timber.log.Timber
import java.io.BufferedWriter
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


fun Context.getColorCompat(colorRes: Int): Int {
    return tryOrNull { ContextCompat.getColor(this, colorRes) } ?: Color.BLACK
}

fun <T> tryOrNull(logOnError: Boolean = true, body: () -> T?): T? {
    return try {
        body()
    } catch (e: Exception) {
        if (logOnError) {
            Timber.e("Error: $e")
        }
        null
    }
}

@ColorInt
fun Context.resolveAttrColor(@AttrRes attr: Int): Int {
    val a = theme.obtainStyledAttributes(intArrayOf(attr))
    val color: Int
    try {
        color = a.getColor(0, 0)
    } finally {
        a.recycle()
    }
    return color
}
fun Context.saveStringToFile(fileName: String, content: String) {
    try {
        val file = File(this.filesDir, fileName)
        val fileWriter = FileWriter(file)
        val bufferedWriter = BufferedWriter(fileWriter)
        bufferedWriter.write(content)
        bufferedWriter.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
}
fun Context.convertImageToBase64(imageUri: Uri): String? {
    val inputStream = this.contentResolver.openInputStream(imageUri)
    val bitmap = BitmapFactory.decodeStream(inputStream)

    // Resize the bitmap if needed to ensure it's smaller than 1MB
    val maxSizeBytes = 1024 * 1024
    val outputStream = ByteArrayOutputStream()
    var quality = 100 // Starting quality value
    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
    while (outputStream.size() > maxSizeBytes && quality > 0) {
        outputStream.reset()
        quality -= 10
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
    }

    val byteArray = outputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}

fun Context.convertDrawableToBase64(drawableResId: Int): String? {
    try {
        val bitmap = BitmapFactory.decodeResource(this.resources, drawableResId)
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        val imageData: ByteArray = outputStream.toByteArray()
        outputStream.close()
        return Base64.encodeToString(imageData, Base64.DEFAULT)
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return null
}
fun Context.getDimens(@DimenRes dimenRes: Int): Float {
    return resources.getDimension(dimenRes)
}
@SuppressLint("HardwareIds")
fun Context.getDeviceId() : String{
    Timber.e("DeviceId = ${Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)} ")
    return Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
}
fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager =
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = connectivityManager.activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnected
}

fun Context.makeToast(@StringRes res: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, res, duration).show()
}

fun Context.makeToast(text: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, text, duration).show()
}
fun Context.saveBase64ImageToGallery(base64String: String) {
    try {
        val imageBytes: ByteArray = Base64.decode(base64String, Base64.DEFAULT)
        val bitmap: Bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size) ?: return
        saveBitmapToGallery(bitmap)
    } catch (e: Exception) {
        e.printStackTrace()

    }
}
 fun Context.saveBitmapToGallery(bitmap: Bitmap) {

    val resolver = this.contentResolver
     val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
     val imageFileName = "IMG_$timeStamp.jpg"
    val imagesCollection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
    val imageDetails = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, imageFileName)
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
    }

    val imageUri = resolver.insert(imagesCollection, imageDetails)
    imageUri?.let {
        try {
            val outputStream: OutputStream? = resolver.openOutputStream(it)
            outputStream?.use { stream ->

                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
