package com.basic.common.extension

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Uri
import android.provider.Settings
import android.util.Base64
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.DimenRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream


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
fun Context.convertImageToBase64(imageUri: Uri): String? {
    try {
        val inputStream: InputStream? = this.contentResolver.openInputStream(imageUri)
        val buffer = ByteArrayOutputStream()
        val bufferSize = 1024
        val bufferData = ByteArray(bufferSize)
        var length: Int
        while (inputStream?.read(bufferData).also { length = it!! } != -1) {
            buffer.write(bufferData, 0, length)
        }
        buffer.flush()
        val imageData: ByteArray = buffer.toByteArray()
        inputStream?.close()
        buffer.close()
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
