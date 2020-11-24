package com.malec.cheesetime.ui.main.cheese.cheeseManage.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.viewpager.widget.ViewPager
import com.malec.cheesetime.databinding.FragmentFullscreenPhotoBinding
import com.malec.cheesetime.di.Injectable
import com.malec.cheesetime.model.Photo
import com.malec.cheesetime.ui.main.cheese.cheeseManage.CheeseManageViewModel

class FullscreenPhotoFragment : PhotoManager(), Injectable {
    override val viewModel: CheeseManageViewModel by activityViewModels {
        viewModelFactory
    }

    private lateinit var binding: FragmentFullscreenPhotoBinding

    private lateinit var adapter: FullscreenPhotoAdapter

    private val photos = mutableListOf<Photo>()

    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFullscreenPhotoBinding.inflate(inflater, parent, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        initViewModelListeners()
        initViewPager()
        initClickListeners()
    }

    private fun initViewModelListeners() {
        viewModel.isToolbarVisible.observe(viewLifecycleOwner, { isVisible ->
            if (isVisible != null) {
                binding.imageManageLayout.isVisible = isVisible
            }
        })

        viewModel.photos.observe(viewLifecycleOwner, {
            if (it != null) {
                photos.clear()
                photos.addAll(it)
                adapter.notifyDataSetChanged()
                viewModel.fullscreenPhotoPosition.value?.let { pos ->
                    if (pos > -1 && pos < photos.size)
                        binding.photoViewPager.setCurrentItem(pos, true)
                }
            }
            if (it == null || it.isEmpty())
                viewModel.exitFullscreenPhotoView()
        })
    }

    private fun initViewPager() {
        adapter = FullscreenPhotoAdapter(photos) {
            viewModel.isToolbarVisible.value = viewModel.isToolbarVisible.value?.not() ?: true
        }
        binding.photoViewPager.adapter = adapter
        binding.photoViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                viewModel.fullscreenPhotoPosition.value = position
            }

            override fun onPageSelected(position: Int) {
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
    }

    private fun initClickListeners() {
        binding.downloadButton.setOnClickListener {
            downloadPhoto(photos[binding.photoViewPager.currentItem])
        }

        binding.shareButton.setOnClickListener {
            viewModel.onPhotoShareClick(photos[binding.photoViewPager.currentItem])
        }

        binding.deleteButton.setOnClickListener {
            deletePhotoDialog(requireContext(), photos[binding.photoViewPager.currentItem])
        }
    }
}