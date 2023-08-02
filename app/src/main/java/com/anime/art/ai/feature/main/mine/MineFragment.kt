package com.anime.art.ai.feature.main.mine

import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.anime.art.ai.R
import com.anime.art.ai.common.ConfigApp
import com.anime.art.ai.common.extension.startFinalize
import com.anime.art.ai.common.extension.startSetting
import com.anime.art.ai.data.db.query.CreatorDao
import com.anime.art.ai.data.db.query.GalleryDao
import com.anime.art.ai.databinding.FragmentMineBinding
import com.anime.art.ai.feature.main.MainActivity
import com.anime.art.ai.feature.main.gallery.adapter.PreviewAdapter
import com.anime.art.ai.feature.main.mine.adapter.CreateAdapter
import com.anime.art.ai.feature.main.mine.adapter.FavoriteAdapter
import com.basic.common.base.LsFragment
import com.basic.common.extension.clicks
import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.autoDispose
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MineFragment : LsFragment<FragmentMineBinding>(FragmentMineBinding::inflate) {
    @Inject
    lateinit var creatorDao: CreatorDao
    @Inject
    lateinit var galleryDao: GalleryDao
    @Inject
    lateinit var creatorAdapter: CreateAdapter
    @Inject
    lateinit var favoriteAdapter: FavoriteAdapter
    @Inject lateinit var configApp: ConfigApp
    companion object {
        const val MY_CREATOR = 1
        const val FAVORITE = 2
    }

    private var screenSelected = 0
        set(value) {
            if (field == value) return
            binding.creatorView.apply {
                if (value == MY_CREATOR){
                    setBackgroundResource(R.drawable.stroke_gradient_yellow)
                    binding.emptyLayout.isVisible = creatorAdapter.data.isEmpty()
                }
                else setBackgroundColor(context.getColor(R.color.background_dark_gray))
            }
            binding.favoriteView.apply {
                if (value == FAVORITE){
                    setBackgroundResource(R.drawable.stroke_gradient_yellow)
                    binding.emptyLayout.isVisible = favoriteAdapter.data.isEmpty()
                }
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
        galleryDao.getAllFavorite().observe(viewLifecycleOwner){ galleries ->
            binding.emptyLayout.isVisible = (galleries.isEmpty() && screenSelected == FAVORITE)
            this.favoriteAdapter.data = galleries
        }
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
        favoriteAdapter
            .toggleFavouriteClicks
            .autoDispose(scope())
            .subscribe { gallery ->
                galleryDao.update(gallery)

            }
        favoriteAdapter
            .clicks
            .autoDispose(scope())
            .subscribe {gallery ->
                (activity as? MainActivity)
                    ?.gotoGalleryFragment(gallery.prompt, gallery.ratio)
            }
        (activity as? MainActivity)
            ?.pageChanges
            ?.filter { index -> index == 2 }
            ?.take(1)
            ?.autoDispose(scope())
            ?.subscribe {
                creatorDao.getAll().observe(viewLifecycleOwner){creators ->
                    binding.emptyLayout.isVisible = (creators.isEmpty() && screenSelected == MY_CREATOR)
                    this.creatorAdapter.data = creators
                }
            }

        creatorAdapter
            .clicks
            .autoDispose(scope())
            .subscribe { creator ->
                configApp.imageGenerationRequest.apply {
                    prompt = creator.prompt
                    negativePrompt = creator.negative
                    artStyle = creator.artStyle
                }
                configApp.imageBase64 = creator.image
                activity?.startFinalize()
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