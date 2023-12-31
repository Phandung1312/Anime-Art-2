package com.anime.art.ai.feature.gallery

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.lifecycleScope
import com.anime.art.ai.R
import com.anime.art.ai.common.Constraint
import com.anime.art.ai.common.extension.back
import com.anime.art.ai.common.extension.getStatusBarHeight
import com.anime.art.ai.common.widget.transformer.ZoomInTransformer
import com.anime.art.ai.data.Preferences
import com.anime.art.ai.data.db.query.GalleryDao
import com.anime.art.ai.databinding.ActivityGalleryBinding
import com.anime.art.ai.feature.finalize.LoadingDialog
import com.anime.art.ai.feature.gallery.adapter.PreviewAdapter
import com.anime.art.ai.feature.main.create.CreateFragment
import com.basic.common.base.LsActivity
import com.basic.common.extension.clicks
import com.basic.common.extension.getDimens
import com.basic.common.extension.makeToast
import com.basic.common.extension.saveImageToGallery
import com.basic.common.extension.transparent
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.autoDispose
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.OutputStream
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class GalleryActivity : LsActivity<ActivityGalleryBinding>(ActivityGalleryBinding::inflate) {

    companion object {
        const val GALLERY_INDEX_EXTRA = "GALLERY_INDEX_EXTRA"
    }

    @Inject lateinit var prefs: Preferences
    @Inject lateinit var galleryDao: GalleryDao
    @Inject lateinit var previewAdapter: PreviewAdapter

    private val galleryIndex by lazy { intent.getIntExtra(GALLERY_INDEX_EXTRA, 0) }
    private var showedMore = false
        set(value) {
            field = value
            binding.viewMoreAction.isVisible = value
            binding.viewBackgroundActionClicks.isVisible = value
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(prefs.isDarkMode.get()){
            transparent()
        }
        else{
            transparent(true, true)
        }
        setContentView(binding.root)

        initView()
        initObservable()
        initData()
        listenerView()
    }

    private fun listenerView() {
        binding.back.clicks { back() }
        binding.more.clicks { showedMore = !showedMore }
        binding.viewBackgroundActionClicks.clicks(withAnim = false) { showedMore = false }
        binding.viewSave.clicks {
           lifecycleScope.launch {
               val loadingDialog = LoadingDialog()
               loadingDialog.show(supportFragmentManager, null)
               delay(500)
               val gallery = previewAdapter.data[binding.viewPager.currentItem]
               downloadAndSaveImage(gallery.preview,
                   width = gallery.ratio.split(":")[0].toInt(),
                   height = gallery.ratio.split(":")[1].toInt()){ result ->
                   if(result) loadingDialog.cancel()
                   else {
                       loadingDialog.dismiss()
                       makeToast("Save failed")
                   }

               }
           }
        }
        binding.viewReport.clicks {
            val galleryId = previewAdapter.data[binding.viewPager.currentItem].id
            openGmailCompose(galleryId)
        }
        binding.viewDislike.clicks { dislikeClicks() }
        binding.viewTry.clicks(withAnim = false) {
            val gallery = previewAdapter.data[binding.viewPager.currentItem]
            intent.putExtra(CreateFragment.PROMPT_EXTRA, gallery.prompt)
            intent.putExtra(CreateFragment.RATIO_EXTRA, gallery.ratio)
            intent.putExtra(CreateFragment.NEGATIVE_PROMPT, gallery.negative)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
    private fun downloadAndSaveImage(imageUrl: String,width : Int = 500, height : Int = 500, result : (Boolean) -> Unit) {
        Glide.with(this)
            .asBitmap()
            .load(imageUrl)
            .apply(RequestOptions().override(width, height))
            .into(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    this@GalleryActivity.saveImageToGallery(resource, result)
                }
            })
    }


    private fun startActivityExternal(intent: Intent) {
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            startActivity(Intent.createChooser(intent, null))
        }
    }
    private fun openGmailCompose(galleryId : Long) {

        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(Constraint.Info.MAIL_SUPPORT))
        intent.putExtra(Intent.EXTRA_SUBJECT, "${getString(R.string.app_name)} Report")
        intent.putExtra(
            Intent.EXTRA_TEXT, "Reason:\n\n Template ID:$galleryId"
        )
        startActivityExternal(intent)

    }
    private fun dislikeClicks() {
        val gallery = previewAdapter.data.getOrNull(binding.viewPager.currentItem) ?: return
        gallery.dislike = true
        galleryDao.update(gallery)

        previewAdapter.data = ArrayList(previewAdapter.data).apply {
            this.remove(gallery)
        }
        previewAdapter.notifyItemRemoved(binding.viewPager.currentItem)
        previewAdapter.notifyItemRangeChanged(binding.viewPager.currentItem, previewAdapter.data.size - binding.viewPager.currentItem)

        showedMore = false
        makeToast("We will not show you this template anymore")
    }

    private fun initData() {

    }

    private fun initObservable() {
        previewAdapter
            .toggleFavouriteClicks
            .autoDispose(scope())
            .subscribe { gallery ->
                galleryDao.update(gallery)
            }
    }

    private fun initView() {
        binding.viewPager.apply {
            this.setPageTransformer(ZoomInTransformer())
            this.adapter = previewAdapter.apply {
                this.data = galleryDao.getAllLike()
            }
            this.post { this.setCurrentItem(galleryIndex, false) }
        }

        binding.viewTop.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            this.topMargin = when (val height = getStatusBarHeight()) {
                0 -> getDimens(com.intuit.sdp.R.dimen._30sdp).toInt()
                else -> height
            }
        }
    }

    override fun onBackPressed() {
        back()
    }

}