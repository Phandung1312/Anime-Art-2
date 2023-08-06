package com.anime.art.ai.common

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun decodeBase64ToBitmap(base64: String): Bitmap? {
    val decodedBytes = Base64.decode(base64, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
}

fun saveBitmapToFile(context: Context, bitmap: Bitmap): File? {
    val fileName = "image_to_share.png"
    var file: File? = null
    try {
        val cacheDir = context.externalCacheDir
        if (cacheDir != null) {
            file = File(cacheDir, fileName)
            val out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            out.flush()
            out.close()
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return file
}
fun removeWhitespace(input: String): String {
    return input.replace("\\s+".toRegex(), "")
}

fun getCurrentDay() : String {
    val currentDateTime = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return  currentDateTime.format(formatter)

}



