package com.anime.art.ai.common

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import com.basic.common.extension.convertImageToBase64
import glimpse.core.crop
import glimpse.core.findCenter
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


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


// Hàm lưu Bitmap vào thư viện
fun saveBitmapToGallery(context: Context, bitmap: Bitmap, title: String) {
    val filename = "$title.jpg"
    val contentResolver = context.contentResolver

    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, filename)
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_DCIM)
    }

    val imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

    imageUri?.let { uri ->
        val outputStream: OutputStream? = contentResolver.openOutputStream(uri)
        outputStream?.use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, it)
        }
    }
}
// Hàm thay đổi kích thước của Bitmap
fun decodeBase64ToBitmap(base64: String): Bitmap {
    val decodedBytes = Base64.decode(base64, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
}


fun processAndSaveImage(context: Context, base64Image: String, result : (Boolean) -> Unit) {

 try{
     val bitmap = decodeBase64ToBitmap(base64Image)
     saveBitmapToGallery(context, bitmap, "image_${System.currentTimeMillis()}")
     result.invoke(true)
 }
 catch (e : Exception){
     e.printStackTrace()
     result.invoke(false)
 }
}

fun cropBase64Image(base64Image : String, targetWidthRatio: Float, targetHeightRatio: Float) : String{
    val originalBitmap = decodeBase64ToBitmap(base64Image)
    val originalWidth = originalBitmap.width
    val originalHeight = originalBitmap.height
    val targetWidth: Int
    val targetHeight: Int
    val targetRatio : Int

    val ratioWidth = (originalWidth / targetWidthRatio).toInt()
    val ratioHeight = (originalHeight / targetHeightRatio).toInt()

    targetRatio = if(ratioWidth > ratioHeight) ratioHeight else ratioWidth

    targetWidth = (targetRatio * targetWidthRatio).toInt()
    targetHeight = (targetRatio * targetHeightRatio).toInt()

    val (cropX, cropY) = originalBitmap.findCenter()
    val croppedBitmap = originalBitmap.crop(cropX, cropY, targetWidth, targetHeight)

    return convertImageToBase64(croppedBitmap)
}

fun resizeBase64Image(base64Image: String, targetWidth: Int, targetHeight: Int): String {
    // Decode base64 string to byte array
    val imageBytes = Base64.decode(base64Image, Base64.DEFAULT)

    // Decode byte array to bitmap
    val originalBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

    // Resize the bitmap to the target dimensions
    val resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, targetWidth, targetHeight, false)

    // Convert the resized bitmap back to base64
    val outputStream = ByteArrayOutputStream()
    resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    val resizedImageBytes = outputStream.toByteArray()

    return Base64.encodeToString(resizedImageBytes, Base64.DEFAULT)
}






