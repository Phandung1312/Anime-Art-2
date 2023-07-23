package com.anime.art.ai.feature.createimage


import android.content.Intent
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.anime.art.ai.R
import com.anime.art.ai.common.extension.back
import com.anime.art.ai.common.widget.transformer.ZoomInTransformer
import com.anime.art.ai.databinding.ActivityCreateImageBinding
import com.anime.art.ai.domain.model.config.ViewImage
import com.anime.art.ai.feature.createimage.adapter.PreviewAdapter
import com.anime.art.ai.feature.finalize.FinalizeActivity
import com.basic.common.base.LsActivity
import com.basic.common.extension.clicks
import com.basic.common.extension.transparent
import com.basic.common.extension.tryOrNull
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CreateImageActivity : LsActivity<ActivityCreateImageBinding>(ActivityCreateImageBinding::inflate) {

    @Inject lateinit var previewAdapter : PreviewAdapter
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
        binding.viewFinalize.clicks {
            val intent = Intent(this, FinalizeActivity::class.java)
            startActivity(intent)
            tryOrNull { overridePendingTransition(R.anim.slide_in_left, R.anim.nothing) }
        }

    }

    @Deprecated("Deprecated in Java", ReplaceWith("back()", "com.anime.art.ai.common.extension.back"))
    override fun onBackPressed() {
        back()
    }
    private fun initData() {
        val galleries = listOf(
            ViewImage(display = "https://firebasestorage.googleapis.com/v0/b/anime-art-2.appspot.com/o/v1%2F1_zzz_512__584_zzz.webp?alt=media&token=e12e729f-81f3-4c4e-9379-510209f6c818"),
            ViewImage(display = "https://firebasestorage.googleapis.com/v0/b/anime-art-2.appspot.com/o/v1%2F2_zzz_512__698_zzz.webp?alt=media&token=2021b922-431d-40ee-8952-aaf8fe4e7ec3"),
            ViewImage(display = "https://firebasestorage.googleapis.com/v0/b/anime-art-2.appspot.com/o/v1%2F3_zzz_512__698_zzz.webp?alt=media&token=fca4fe13-42bb-47fb-8d8c-c84fed89b73d"),
            ViewImage(display = "https://firebasestorage.googleapis.com/v0/b/anime-art-2.appspot.com/o/v1%2F4_zzz_512__584_zzz.webp?alt=media&token=eea7c6d1-bbc6-418e-8869-5d6efb152ee6")
        )
        binding.viewPager.apply {
            this.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            this.setPageTransformer(ZoomInTransformer())
            this.adapter = previewAdapter.apply {
                this.data = galleries
            }
            this.post{ this.setCurrentItem(1, false)}
        }
    }

    private fun initObservable() {

    }

    private fun initView() {

    }
}