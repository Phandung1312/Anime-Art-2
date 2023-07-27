package com.anime.art.ai.feature.main.mine

import android.content.Intent
import androidx.core.view.isVisible
import com.anime.art.ai.R
import com.anime.art.ai.common.extension.startDetailGallery
import com.anime.art.ai.common.extension.startSetting
import com.anime.art.ai.data.db.query.GalleryDao
import com.anime.art.ai.data.db.query.PromptDao
import com.anime.art.ai.databinding.FragmentMineBinding
import com.anime.art.ai.feature.main.MainActivity
import com.anime.art.ai.feature.main.gallery.adapter.PreviewAdapter
import com.anime.art.ai.feature.setting.SettingActivity
import com.basic.common.base.LsFragment
import com.basic.common.extension.clicks
import com.basic.common.extension.tryOrNull
import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.autoDispose
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MineFragment : LsFragment<FragmentMineBinding>(FragmentMineBinding::inflate) {
    @Inject
    lateinit var galleryDao: GalleryDao
    @Inject
    lateinit var creatorAdapter: PreviewAdapter
    @Inject
    lateinit var favoriteAdapter: PreviewAdapter

    companion object {
        const val MY_CREATOR = 1
        const val FAVORITE = 2
    }

    private var screenSelected = 0
        set(value) {
            if (field == value) return
            binding.creatorView.apply {
                if (value == MY_CREATOR) setBackgroundResource(R.drawable.stroke_gradient_yellow)
                else setBackgroundColor(context.getColor(R.color.background_dark_gray))
            }
            binding.favoriteView.apply {
                if (value == FAVORITE) setBackgroundResource(R.drawable.stroke_gradient_yellow)
                else setBackgroundColor(context.getColor(R.color.background_dark_gray))
            }
            binding.recyclerView.adapter =
                if (value == MY_CREATOR) creatorAdapter else favoriteAdapter
            field = value
        }


    override fun onViewCreated() {
        initView()
        initData()
        initListener()
    }
    private fun initData() {
    }

    private fun initListener() {
        binding.creatorCardView.clicks(withAnim = false) {
            screenSelected = MY_CREATOR
        }
        binding.favoriteCardView.clicks(withAnim = false) {
            screenSelected = FAVORITE
        }
        binding.setting.clicks {
           activity?.startSetting()
        }
    }

    override fun onResume() {
        initObservable()
        super.onResume()
    }

    private fun initObservable() {
        creatorAdapter
            .clicks
            .autoDispose(scope())
            .subscribe { galleryIndex -> }
        favoriteAdapter
            .toggleFavouriteClicks
            .autoDispose(scope())
            .subscribe { gallery ->
                galleryDao.update(gallery)

            }
        (activity as? MainActivity)
            ?.pageChanges
            ?.filter { index -> index == 2 }
            ?.take(1)
            ?.autoDispose(scope())
            ?.subscribe {
                galleryDao.getAllFavorite().observe(viewLifecycleOwner) { galleries ->
                    binding.emptyLayout.isVisible = (galleries.isEmpty())
                    this.favoriteAdapter.data = galleries
                }
            }
    }

    private fun initView() {
        screenSelected = 1
        binding.recyclerView.apply {
            this.adapter = creatorAdapter
            this.itemAnimator = null
        }
    }
}