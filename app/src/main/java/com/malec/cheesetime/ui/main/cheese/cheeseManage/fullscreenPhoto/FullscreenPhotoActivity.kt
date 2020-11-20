package com.malec.cheesetime.ui.main.cheese.cheeseManage.fullscreenPhoto

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.malec.cheesetime.R
import com.malec.cheesetime.databinding.ActivityFullscreenPhotoBinding
import com.malec.cheesetime.ui.Screens
import com.malec.cheesetime.ui.base.BasePhotoActivity
import kotlinx.android.synthetic.main.activity_fullscreen_photo.*
import ru.terrakok.cicerone.android.support.SupportAppNavigator

class FullscreenPhotoActivity : BasePhotoActivity() {
    override val viewModel: FullscreenPhotoViewModel by viewModels {
        viewModelFactory
    }

    override val navigator = SupportAppNavigator(
        this,
        supportFragmentManager,
        R.id.navHostFragment
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityFullscreenPhotoBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_fullscreen_photo)

        val photo = Screens.FullscreenPhotoScreen.parseExtraIntent(intent)
        viewModel.setPhoto(photo)
        binding.photo = viewModel.fullscreenPhoto.value

        viewModel.isDeleted.observe(this, {
            if (it == true) setResult(RESULT_OK, Intent().putExtra("data", "deleted"))
        })

        downloadButton.setOnClickListener {
            downloadPhoto(viewModel.fullscreenPhoto.value)
        }

        shareButton.setOnClickListener {
            viewModel.onPhotoShareClick(viewModel.fullscreenPhoto.value)
        }

        deleteButton.setOnClickListener {
            deletePhotoDialog(viewModel.fullscreenPhoto.value)
        }
    }
}