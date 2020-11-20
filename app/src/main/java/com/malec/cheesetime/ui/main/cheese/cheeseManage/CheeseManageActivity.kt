package com.malec.cheesetime.ui.main.cheese.cheeseManage

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.cardview.widget.CardView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.util.Pair
import androidx.core.view.children
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.malec.cheesetime.R
import com.malec.cheesetime.databinding.ActivityCheeseManageBinding
import com.malec.cheesetime.model.Photo
import com.malec.cheesetime.ui.Screens
import com.malec.cheesetime.ui.Screens.GALLERY
import com.malec.cheesetime.ui.allertDialogBuilder.CheeseDeleteDialog
import com.malec.cheesetime.ui.base.BasePhotoActivity
import com.malec.cheesetime.ui.main.ResultNavigator
import com.malec.cheesetime.util.DateFormatter
import com.malec.cheesetime.util.DateTimePicker
import kotlinx.android.synthetic.main.activity_cheese_manage.*

class CheeseManageActivity : BasePhotoActivity(), PhotoAction {
    override val viewModel: CheeseManageViewModel by viewModels {
        viewModelFactory
    }

    private var oldToolbarColor = 0

    private val recipes = mutableListOf<String>()

    override val navigator = ResultNavigator(
        this,
        supportFragmentManager,
        R.id.navHostFragment
    )

    private lateinit var adapter: PhotoAdapter
    private lateinit var recipeAdapter: ArrayAdapter<String>

    private var saveButton: MenuItem? = null
    private var deleteButton: MenuItem? = null
    private var isDeleteActive = false
    private var isSaveActive = false

    private lateinit var stagesAdapter: StringAdapter

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_manage, menu)

        saveButton = menu?.findItem(R.id.saveButton)
        saveButton?.isVisible = isSaveActive

        deleteButton = menu?.findItem(R.id.deleteButton)
        deleteButton?.isVisible = isDeleteActive

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.saveButton -> saveCheese()
            R.id.deleteButton ->
                CheeseDeleteDialog(this).setOnOkButtonClickListener {
                    viewModel.deleteCheese()
                }.show()
            android.R.id.home -> onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null && data.data != null && requestCode == GALLERY)
            viewModel.getImageFromUri(data.data)
        else if (resultCode == Activity.RESULT_OK && requestCode == CAMERA_PERMISSION.code)
            viewModel.getImageFromCamera()
        else if (resultCode == Activity.RESULT_OK && data?.hasExtra("data") == true)
            viewModel.fullscreenPhoto.value?.let { viewModel.onPhotoDeleteClick(it) }

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CAMERA_PERMISSION.code && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            viewModel.onAttachFromCamera()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityCheeseManageBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_cheese_manage)

        val cheese = Screens.CheeseManageScreen.parseExtraIntent(intent)
        viewModel.setCheese(cheese)
        binding.cheese = viewModel.cheese.value

        initViewModelListeners()
        initColorClickListener()
        initClickListeners()
        initToolbar()
        initInputListeners()
        initPhotoRecycler()

        recipeAdapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, recipes)
        recipeSpinner.adapter = recipeAdapter

        viewModel.stages.value?.let {
            stagesAdapter = StringAdapter(it, viewModel)
            stagesRecycler.adapter = stagesAdapter
            stagesRecycler.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun saveCheese() {
        progressLayout.visibility = View.VISIBLE

        viewModel.checkAndManageCheese()
    }

    private fun showImageDialog() {
        AttachSourceDialog { result ->
            when (result) {
                AttachSourceDialog.DialogResult.GALLERY -> {
                    viewModel.onAttachFromGallery()
                }
                AttachSourceDialog.DialogResult.CAMERA -> checkOrRequestPermission(CAMERA_PERMISSION) {
                    viewModel.onAttachFromCamera()
                }
            }
        }.show(supportFragmentManager, "SelectAttachSource")
    }

    private fun initViewModelListeners() {
        viewModel.stages.observe(this, { stages ->
            stagesAdapter.submitList(stages)
        })

        viewModel.isSaveActive.observe(this, { active ->
            saveButton?.isVisible = active
            isSaveActive = active
        })

        viewModel.isDeleteActive.observe(this, { active ->
            deleteButton?.isVisible = active
            isDeleteActive = active
        })

        viewModel.recipes.observe(this, {
            recipeAdapter.clear()
            it?.let { recipes ->
                recipeAdapter.addAll(recipes)
                recipeSpinner.setSelection(recipes.indexOf(viewModel.cheese.value?.recipe ?: ""))
            }
            recipeAdapter.notifyDataSetChanged()
        })

        viewModel.badgeColor.observe(this, { color ->
            Handler(Looper.getMainLooper()).postDelayed({
                animateToolbarColorChange(color)
            }, 200)
        })

        viewModel.photos.observe(this, {
            adapter.submitList(it)
        })

        viewModel.manageError.observe(this, { message ->
            if (!message.isNullOrBlank()) {
                showError(message)
                viewModel.manageError.value = null

                progressLayout.visibility = View.GONE
            }
        })

        viewModel.manageResult.observe(this, { message ->
            if (!message.isNullOrBlank()) {
                showMessage(message)
                viewModel.manageResult.value = null
                onBackPressed()

                progressLayout.visibility = View.GONE
            }
        })
    }

    private fun initInputListeners() {
        nameEditText.doAfterTextChanged {
            viewModel.checkCanSave()
        }
        dateButton.setOnClickListener {
            DateTimePicker(this).pickDate {
                viewModel.cheese.value?.date = DateFormatter.dateFromString(it)
                dateText.text = it
            }
        }
        recipeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(adapter: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val recipe = adapter?.getItemAtPosition(p2)?.toString()
                viewModel.selectRecipe(recipe)
            }
        }
        milkTypeEditText.doAfterTextChanged {
            viewModel.checkCanSave()
        }
        milkVolumeEditText.doAfterTextChanged {
            viewModel.checkCanSave()
        }

        barcodeImage.setOnClickListener {
            viewModel.shareCheese()
        }
    }

    override fun onPhotoClick(photo: Photo, vararg transitionOptions: Pair<View, String>) {
        viewModel.onPhotoClick(photo, transitionOptions[0])
    }

    override fun onPhotoLongClick(photo: Photo) {
        PhotoMenuBuilder()
            .setOnDownloadClickListener {
                downloadPhoto(photo)
            }
            .setOnShareClickListener {
                viewModel.onPhotoShareClick(photo)
            }
            .setOnDeleteClickListener {
                deletePhotoDialog(photo)
            }
            .show(supportFragmentManager)
    }

    private fun initToolbar() {
        toolbar.setTitle(R.string.toolbar_manage_cheese)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setOnApplyWindowInsetsListener { _, insets ->
            val statusBarHeight =
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R)
                    insets.getInsets(WindowInsets.Type.systemBars()).top
                else
                    insets.systemWindowInsetTop

            val h = (resources.getDimension(R.dimen.toolbar_height) + statusBarHeight).toInt()
            var lp = toolbar.layoutParams as CoordinatorLayout.LayoutParams
            toolbar.layoutParams = lp.also { it.height = h }
            toolbar.setPadding(0, statusBarHeight, 0, 0)
            revealBackground.layoutParams = lp
            revealBackground.setPadding(0, statusBarHeight, 0, 0)
            reveal.layoutParams = lp
            reveal.setPadding(0, statusBarHeight, 0, 0)
            lp = scrollView.layoutParams as CoordinatorLayout.LayoutParams
            scrollView.layoutParams = lp.also { it.topMargin = h }
            insets
        }
    }

    private fun initPhotoRecycler() {
        adapter = PhotoAdapter(this)
        photoRecycler.adapter = adapter
        photoRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun animateToolbarColorChange(newColor: Int) {
        if (newColor == oldToolbarColor) return

        val animator = ViewAnimationUtils.createCircularReveal(
            reveal,
            toolbar.width,
            0,
            0F,
            toolbar.width.toFloat()
        )

        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                reveal.setBackgroundColor(newColor)
                revealBackground.setBackgroundColor(oldToolbarColor)
            }

            override fun onAnimationEnd(animation: Animator?) {
                oldToolbarColor = newColor
                window.statusBarColor = newColor
            }
        })

        animator.duration = 300
        animator.start()
    }

    private fun initColorClickListener() {
        for (child in colorLayout1.children + colorLayout2.children) {
            child.setOnClickListener {
                if (it is CardView) {
                    val newColor = it.cardBackgroundColor.defaultColor
                    viewModel.badgeColor.value = newColor
                }
            }
        }
    }

    private fun initClickListeners() {
        stageAddButton.setOnClickListener {
            viewModel.addNewStage()
        }

        photoAddButton.setOnClickListener {
            showImageDialog()
        }
    }
}