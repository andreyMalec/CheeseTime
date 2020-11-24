package com.malec.cheesetime.ui.main.cheese.cheeseManage

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.ViewAnimationUtils
import android.view.WindowInsets
import androidx.activity.viewModels
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.malec.cheesetime.Permissions
import com.malec.cheesetime.R
import com.malec.cheesetime.databinding.ActivityCheeseManageBinding
import com.malec.cheesetime.model.Cheese
import com.malec.cheesetime.ui.Screens.CODE_GALLERY
import com.malec.cheesetime.ui.base.BaseActivity
import com.malec.cheesetime.ui.main.ResultNavigator
import com.malec.cheesetime.ui.parseExtraIntent

class CheeseManageActivity : BaseActivity() {
    private val viewModel: CheeseManageViewModel by viewModels {
        viewModelFactory
    }

    override val navigator = ResultNavigator(
        this,
        R.id.navHostFragment
    )

    private lateinit var binding: ActivityCheeseManageBinding

    private var oldToolbarColor = 0
    private var oldToolbarTitle: String = ""
    private var colorWhite = 0
    private var backgroundShadow: Drawable? = null
    private var backgroundTransparent: Drawable? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null && data.data != null && requestCode == CODE_GALLERY)
            viewModel.getImageFromUri(data.data)
        else if (resultCode == Activity.RESULT_OK && requestCode == Permissions.CAMERA.code)
            viewModel.getImageFromCamera()

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheeseManageBinding.inflate(layoutInflater)
        setContentView(binding.getRoot())

        val cheese = parseExtraIntent<Cheese>(intent)
        viewModel.setCheese(cheese)

        colorWhite = getColor(R.color.colorWhite)
        backgroundShadow = ContextCompat.getDrawable(this, R.color.colorShadow)
        backgroundTransparent = ContextCompat.getDrawable(this, android.R.color.transparent)
        oldToolbarTitle = getString(R.string.toolbar_manage_cheese)

        iniViewModelListeners()
        initToolbar()
    }

    private fun iniViewModelListeners() {
        viewModel.badgeColor.observe(this, { color ->
            Handler(Looper.getMainLooper()).postDelayed({
                animateToolbarColorChange(color)
            }, 200)
        })

        viewModel.isProgressVisible.observe(this, {
            binding.progressLayout.isVisible = it ?: false
        })

        viewModel.isToolbarVisible.observe(this, { isVisible ->
            if (isVisible != null) {
                binding.toolbar.isVisible = isVisible
            }
        })

        viewModel.isFullscreen.observe(this, { isFullscreen ->
            binding.reveal.isVisible = !isFullscreen
            binding.revealBackground.isVisible = !isFullscreen
            binding.toolbar.title = if (!isFullscreen) oldToolbarTitle
            else ""
            if (!isFullscreen) binding.toolbar.navigationIcon?.clearColorFilter()
            else binding.toolbar.navigationIcon?.colorFilter =
                PorterDuffColorFilter(colorWhite, PorterDuff.Mode.SRC_ATOP)
            binding.toolbar.background = if (!isFullscreen) backgroundTransparent
            else backgroundShadow
        })

        viewModel.photoManageResult.observe(this, {
            it?.let {
                showMessage(it)
                viewModel.photoManageResult.value = null
            }
        })

        viewModel.manageError.observe(this, { message ->
            if (!message.isNullOrBlank()) {
                showError(message)
                viewModel.manageError.value = null
            }
        })

        viewModel.manageResult.observe(this, { message ->
            if (!message.isNullOrBlank()) {
                showMessage(message)
                viewModel.manageResult.value = null
                onBackPressed()
            }
        })
    }

    private fun initToolbar() {
        binding.toolbar.setTitle(R.string.toolbar_manage_cheese)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.toolbar.setOnApplyWindowInsetsListener { _, insets ->
            val statusBarHeight =
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R)
                    insets.getInsets(WindowInsets.Type.systemBars()).top
                else
                    insets.systemWindowInsetTop

            val h =
                (resources.getDimension(R.dimen.toolbar_height) + statusBarHeight).toInt()
            val lp = binding.toolbar.layoutParams as CoordinatorLayout.LayoutParams
            binding.toolbar.layoutParams = lp.also { it.height = h }
            binding.toolbar.setPadding(0, statusBarHeight, 0, 0)
            binding.revealBackground.layoutParams = lp
            binding.revealBackground.setPadding(0, statusBarHeight, 0, 0)
            binding.reveal.layoutParams = lp
            binding.reveal.setPadding(0, statusBarHeight, 0, 0)
            insets
        }
    }

    private fun animateToolbarColorChange(newColor: Int) {
        if (oldToolbarColor == newColor) return

        val animator = ViewAnimationUtils.createCircularReveal(
            binding.reveal,
            binding.toolbar.width,
            0,
            0F,
            binding.toolbar.width.toFloat()
        )

        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                binding.reveal.setBackgroundColor(newColor)
                binding.revealBackground.setBackgroundColor(oldToolbarColor)
            }

            override fun onAnimationEnd(animation: Animator?) {
                oldToolbarColor = newColor
                window.statusBarColor = newColor
            }
        })

        animator.duration = 300
        animator.start()
    }

    override fun onBackPressed() {
        if (viewModel.isFullscreen.value == true)
            viewModel.exitFullscreenPhotoView()
        else
            super.onBackPressed()
    }
}