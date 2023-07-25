package com.anime.art.ai.common.extension

import android.app.Activity
import android.content.Intent
import com.anime.art.ai.R
import com.anime.art.ai.feature.createimage.CreateImageActivity
import com.anime.art.ai.feature.finalize.FinalizeActivity
import com.anime.art.ai.feature.gallery.GalleryActivity
import com.anime.art.ai.feature.main.MainActivity
import com.anime.art.ai.feature.main.create.EnterPromptActivity
import com.basic.common.extension.tryOrNull

fun Activity.startMain(){
    val intent = Intent(this, MainActivity::class.java)
    startActivity(intent)
    tryOrNull { overridePendingTransition(R.anim.slide_in_left, R.anim.nothing) }
}

fun Activity.startDetailGallery(galleryIndex: Int){
    val intent = Intent(this, GalleryActivity::class.java)
    intent.putExtra(GalleryActivity.GALLERY_INDEX_EXTRA, galleryIndex)
    startActivity(intent)
    tryOrNull { overridePendingTransition(R.anim.slide_in_left, R.anim.nothing) }
}
fun Activity.startCreateImage(){
    val intent = Intent(this, CreateImageActivity::class.java)
    startActivity(intent)
    tryOrNull { overridePendingTransition(R.anim.slide_in_left, R.anim.nothing) }
}



fun Activity.back(){
    finish()
    tryOrNull { overridePendingTransition(R.anim.nothing, R.anim.slide_out_left) }
}

fun Activity.backTopToBottom(){
    finish()
    tryOrNull { overridePendingTransition(R.anim.nothing, R.anim.slide_down) }
}