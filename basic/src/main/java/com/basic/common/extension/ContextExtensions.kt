package com.basic.common.extension

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Environment
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

fun Context.resizeImageToFit(uri: Uri): String {
    val inputStream = contentResolver.openInputStream(uri)
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeStream(inputStream, null, options)
    inputStream?.close()

    val maxWidth = 1024
    val maxHeight = 1024

    val imageWidth = options.outWidth
    val imageHeight = options.outHeight

    var scaleFactor = 1

    if (imageWidth > maxWidth || imageHeight > maxHeight) {
        val widthScale = imageWidth.toFloat() / maxWidth
        val heightScale = imageHeight.toFloat() / maxHeight

        scaleFactor = if (widthScale > heightScale) {
            Math.round(widthScale)
        } else {
            Math.round(heightScale)
        }
    }

    options.inJustDecodeBounds = false
    options.inSampleSize = scaleFactor

    val resizedBitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(uri), null, options)

    val finalWidth = if (resizedBitmap!!.width > maxWidth) maxWidth else resizedBitmap.width
    val finalHeight = if (resizedBitmap.height > maxHeight) maxHeight else resizedBitmap.height

    val outputBitmap =  Bitmap.createScaledBitmap(resizedBitmap, finalWidth, finalHeight, false)
    return convertImageToBase64(outputBitmap)
}

fun convertImageToBase64(bitmap: Bitmap): String {
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    val byteArray = outputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}



fun Context.convertDrawableToBase64(drawableResId: Int): String? {
    try {
        val options = BitmapFactory.Options()
        options.inScaled = false // Không tự động scale ảnh theo density
        val bitmap = BitmapFactory.decodeResource(this.resources, drawableResId, options)

        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream)

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
