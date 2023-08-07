package com.anime.art.ai.common

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun decodeBase64ToBitmap(base64: String): Bitmap {
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

// Hàm thay đổi kích thước của Bitmap
fun resizeBitmap(bitmap: Bitmap, targetWidth: Int, targetHeight: Int): Bitmap {
    return Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true)
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

// Sử dụng các hàm đã định nghĩa để thực hiện việc chuyển đổi và lưu ảnh
fun processAndSaveImage(context: Context, base64Image: String, targetRatio: Float) {
    val originalBitmap = decodeBase64ToBitmap(base64Image)

    // Tính toán kích thước mới dựa trên tỉ lệ yêu cầu
    val width = originalBitmap.width
    val height = (width / targetRatio).toInt()

    val resizedBitmap = resizeBitmap(originalBitmap, width, height)

    saveBitmapToGallery(context, resizedBitmap, "image_${System.currentTimeMillis()}")
}



