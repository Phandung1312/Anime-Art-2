package com.anime.art.ai.feature.gallery

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import com.anime.art.ai.R
import com.anime.art.ai.common.ConfigApp
import com.anime.art.ai.common.extension.back
import com.anime.art.ai.common.extension.getStatusBarHeight
import com.anime.art.ai.common.extension.startMain
import com.anime.art.ai.common.widget.transformer.ZoomInTransformer
import com.anime.art.ai.data.Preferences
import com.anime.art.ai.data.db.query.GalleryDao
import com.anime.art.ai.databinding.ActivityGalleryBinding
import com.anime.art.ai.feature.gallery.adapter.PreviewAdapter
import com.basic.common.base.LsActivity
import com.basic.common.extension.clicks
import com.basic.common.extension.getDimens
import com.basic.common.extension.transparent
import com.basic.common.extension.tryOrNull
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.autoDispose
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
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
        transparent()
        setContentView(binding.root)

        initView()
        initObservable()
        initData()
        listenerView()
    }

    private fun listenerView() {
        binding.back.clicks { onBackPressed() }
        binding.more.clicks { showedMore = !showedMore }
        binding.viewBackgroundActionClicks.clicks(withAnim = false) { showedMore = false }
        binding.viewSave.clicks {  }
        binding.viewReport.clicks {  }
        binding.viewDislike.clicks { dislikeClicks() }
        binding.viewTry.clicks {  }
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