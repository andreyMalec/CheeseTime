//package com.malec.cheesetime.ui.main.cheese.cheeseManage
//
//import android.animation.Animator
//import android.animation.AnimatorListenerAdapter
//import android.graphics.drawable.Drawable
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.ViewAnimationUtils
//import android.view.WindowInsets
//import androidx.coordinatorlayout.widget.CoordinatorLayout
//import androidx.core.content.ContextCompat
//import com.malec.cheesetime.R
//import com.malec.cheesetime.databinding.ActivityCheeseManageBinding
//import com.malec.cheesetime.ui.parseExtraIntent
//import com.malec.domain.model.Cheese
//import com.malec.presentation.base.BaseActivity
//import com.malec.presentation.navigation.ResultNavigator
//
//class CheeseManageActivity : BaseActivity<ActivityCheeseManageBinding>() {
////    private val cameraScreen = Screens.cameraPick()
////    private val cameraLauncher = registerForActivityResult(ResultContract(cameraScreen), this)
////    private val galleryScreen = Screens.galleryPick()
////    private val galleryLauncher = registerForActivityResult(ResultContract(galleryScreen), this)
//
////    private val viewModel: CheeseManageViewModel by viewModels {
////        viewModelFactory
////    }
//
//    override val navigator = ResultNavigator(
//        this,
//        R.id.navHostFragment,
//        mapOf(
////            Pair(cameraScreen.screenKey, cameraLauncher),
////            Pair(galleryScreen.screenKey, galleryLauncher)
//        )
//    )
//
//    private var oldToolbarColor = 0
//    private var oldToolbarTitle: String = ""
//    private var colorWhite = 0
//    private var backgroundShadow: Drawable? = null
//    private var backgroundTransparent: Drawable? = null
//
////    override fun onActivityResult(result: Intent?) {
////        if (result != null && result.data != null)
////            viewModel.getImageFromUri(result.data)
////        else
////            viewModel.getImageFromCamera()
////    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
////        binding = ActivityCheeseManageBinding.inflate(layoutInflater)
//        setContentView(binding.getRoot())
//
//        val cheese = parseExtraIntent<Cheese>(intent)
////        viewModel.setCheese(cheese)
//
//        colorWhite = getColor(R.color.colorWhite)
//        backgroundShadow = ContextCompat.getDrawable(this, R.color.colorShadow)
//        backgroundTransparent = ContextCompat.getDrawable(this, android.R.color.transparent)
//        oldToolbarTitle = getString(R.string.toolbar_manage_cheese)
//
//        iniViewModelListeners()
//        initToolbar()
//    }
//
//    private fun iniViewModelListeners() {
////        viewModel.badgeColor.observe(this, { color ->
////            Handler(Looper.getMainLooper()).postDelayed({
////                animateToolbarColorChange(color)
////            }, 200)
////        })
////
////        viewModel.isProgressVisible.observe(this, {
////            binding.progressLayout.isVisible = it ?: false
////        })
////
////        viewModel.isToolbarVisible.observe(this, { isVisible ->
////            if (isVisible != null) {
////                binding.toolbar.isVisible = isVisible
////            }
////        })
////
////        viewModel.isFullscreen.observe(this, { isFullscreen ->
////            binding.reveal.isVisible = !isFullscreen
////            binding.revealBackground.isVisible = !isFullscreen
////            binding.toolbar.title = if (!isFullscreen) oldToolbarTitle
////            else ""
////            if (!isFullscreen) binding.toolbar.navigationIcon?.clearColorFilter()
////            else binding.toolbar.navigationIcon?.colorFilter =
////                PorterDuffColorFilter(colorWhite, PorterDuff.Mode.SRC_ATOP)
////            binding.toolbar.background = if (!isFullscreen) backgroundTransparent
////            else backgroundShadow
////        })
////
////        viewModel.photoManageResult.observe(this, {
////            it?.let {
////                showMessage(it)
////                viewModel.photoManageResult.value = null
////            }
////        })
////
////        viewModel.manageError.observe(this, { message ->
////            if (!message.isNullOrBlank()) {
////                showError(message)
////                viewModel.manageError.value = null
////            }
////        })
////
////        viewModel.manageResult.observe(this, { message ->
////            if (!message.isNullOrBlank()) {
////                showMessage(message)
////                viewModel.manageResult.value = null
////                onBackPressed()
////            }
////        })
//    }
//
//    private fun initToolbar() {
//        binding.toolbar.setTitle(R.string.toolbar_manage_cheese)
//        setSupportActionBar(binding.toolbar)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//
//        binding.toolbar.setOnApplyWindowInsetsListener { _, insets ->
//            val statusBarHeight =
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R)
//                    insets.getInsets(WindowInsets.Type.systemBars()).top
//                else
//                    insets.systemWindowInsetTop
//
//            val h =
//                (resources.getDimension(R.dimen.toolbar_height) + statusBarHeight).toInt()
//            val lp = binding.toolbar.layoutParams as CoordinatorLayout.LayoutParams
//            binding.toolbar.layoutParams = lp.also { it.height = h }
//            binding.toolbar.setPadding(0, statusBarHeight, 0, 0)
//            binding.revealBackground.layoutParams = lp
//            binding.revealBackground.setPadding(0, statusBarHeight, 0, 0)
//            binding.reveal.layoutParams = lp
//            binding.reveal.setPadding(0, statusBarHeight, 0, 0)
//            insets
//        }
//    }
//
//    private fun animateToolbarColorChange(newColor: Int) {
//        if (oldToolbarColor == newColor) return
//
//        val animator = ViewAnimationUtils.createCircularReveal(
//            binding.reveal,
//            binding.toolbar.width,
//            0,
//            0F,
//            binding.toolbar.width.toFloat()
//        )
//
//        animator.addListener(object : AnimatorListenerAdapter() {
//            override fun onAnimationStart(animation: Animator?) {
//                binding.reveal.setBackgroundColor(newColor)
//                binding.revealBackground.setBackgroundColor(oldToolbarColor)
//            }
//
//            override fun onAnimationEnd(animation: Animator?) {
//                oldToolbarColor = newColor
//                window.statusBarColor = newColor
//            }
//        })
//
//        animator.duration = 300
//        animator.start()
//    }
//
//    override fun onBackPressed() {
////        if (viewModel.isFullscreen.value == true)
////            viewModel.exitFullscreenPhotoView()
////        else
////            super.onBackPressed()
//    }
//
//    override fun createViewBinding(inflater: LayoutInflater) =
//        ActivityCheeseManageBinding.inflate(inflater)
//}