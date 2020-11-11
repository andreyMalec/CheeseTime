package com.malec.cheesetime.ui.main.cheese.cheeseManage

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.cardview.widget.CardView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.malec.cheesetime.R
import com.malec.cheesetime.databinding.ActivityCheeseManageBinding
import com.malec.cheesetime.model.Cheese
import com.malec.cheesetime.model.Photo
import com.malec.cheesetime.model.StringValue
import com.malec.cheesetime.ui.BaseActivity
import com.malec.cheesetime.ui.Screens
import com.malec.cheesetime.ui.allertDialogBuilder.PhotoDeleteDialog
import com.malec.cheesetime.ui.main.ResultNavigator
import com.malec.cheesetime.ui.main.cheese.cheeseManage.CheeseManageViewModel.Companion.CAMERA
import com.malec.cheesetime.ui.main.cheese.cheeseManage.CheeseManageViewModel.Companion.STORAGE
import com.malec.cheesetime.util.DateFormatter
import com.malec.cheesetime.util.DateTimePicker
import kotlinx.android.synthetic.main.activity_cheese_manage.*

class CheeseManageActivity : BaseActivity(), PhotoAdapter.PhotoAction {
    private val viewModel: CheeseManageViewModel by viewModels {
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
    private var isAlreadyCreated = false

    private lateinit var stagesAdapter: StringAdapter

    private val dropDownButtonClick = object : View.OnClickListener {
        override fun onClick(v: View?) {
            if (v == null) return

            val paramsLayout =
                ((v.parent as LinearLayout).getChildAt(1) as LinearLayout).getChildAt(1)

            v.startAnimation(AnimationUtils.loadAnimation(v.context, R.anim.rotate180))
            if (paramsLayout.visibility == View.VISIBLE) {
                v.setBackgroundResource(R.drawable.icon_arrow_up)
                paramsLayout.visibility = View.GONE
            } else {
                v.setBackgroundResource(R.drawable.icon_arrow_down)
                paramsLayout.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_manage, menu)

        saveButton = menu?.findItem(R.id.saveButton)
        menu?.findItem(R.id.deleteButton)?.isVisible = isAlreadyCreated

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.saveButton -> saveCheese()
            R.id.deleteButton -> viewModel.deleteCheese()
            android.R.id.home -> onBackPressed()
        }

        return super.onOptionsItemSelected(item)
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
                AttachSourceDialog.DialogResult.CAMERA -> {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_GRANTED
                    )
                        viewModel.onAttachFromCamera()
                    else
                        requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA)
                }
            }
        }.show(supportFragmentManager, "SelectAttachSource")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CAMERA && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            viewModel.onAttachFromCamera()

        if (requestCode == STORAGE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            viewModel.tmpPhoto.value?.let {
                viewModel.onPhotoDownloadClick(it)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        viewModel.getImageFromResult(requestCode, resultCode, data)

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityCheeseManageBinding = DataBindingUtil.setContentView(this, R.layout.activity_cheese_manage)

        val cheese = Screens.CheeseManageScreen.parseExtraIntent(intent)
        viewModel.setCheese(cheese)

        initViewModelListeners()
        initColorClickListener()
        initParamsClickListeners()
        initToolbar()
        initInputListeners()
        initRecycler()

        stageAddButton.setOnClickListener {
//            Log.e("test", "testMessage: "+stages)
            viewModel.addNewStage()
        }

        photoAddButton.setOnClickListener {
            showImageDialog()
        }

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

    private fun initViewModelListeners() {
        viewModel.cheese.observe(this, { cheese ->
            isAlreadyCreated = cheese != null
            if (isAlreadyCreated)
                showCheeseData(cheese)
        })

        viewModel.stages.observe(this, { stages ->
            stagesAdapter.submitList(stages)
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
            Handler().postDelayed({
                animateToolbarColorChange(color)
            }, 200)
        })

        viewModel.photos.observe(this, {
            adapter.submitList(it)
        })

        viewModel.isFieldsEmptyError.observe(this, { isError ->
            if (isError) {
                showError(getString(R.string.empty_fields_error))
                viewModel.isFieldsEmptyError.value = false

                if (milkTypeEditText.text.isNullOrBlank())
                    milkTypeEditText.error = getString(R.string.required_field_error)
                if (milkVolumeEditText.text.isNullOrBlank())
                    milkVolumeEditText.error = getString(R.string.required_field_error)

                progressLayout.visibility = View.GONE
            }
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

        viewModel.photoManageResult.observe(this, { message ->
            if (!message.isNullOrBlank()) {
                showMessage(message)
                viewModel.photoManageResult.value = null
            }
        })
    }

    override fun onLongClick(photo: Photo) {
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

    private fun downloadPhoto(photo: Photo) {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        )
            viewModel.onPhotoDownloadClick(photo)
        else {
            viewModel.setTmpPhoto(photo)
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE)
        }
    }

    private fun deletePhotoDialog(photo: Photo) {
        PhotoDeleteDialog(this).setOnOkButtonClickListener {
            viewModel.onPhotoDeleteClick(photo)
        }.show()
    }


    private fun showCheeseData(cheese: Cheese) {
        nameEditText.setText(cheese.name)

        dateText.text = DateFormatter.simpleFormat(cheese.date)

        val recipes = viewModel.recipes.value
        recipeSpinner.setSelection(recipes?.indexOf(cheese.recipe) ?: 0)

        commentEditText.setText(cheese.comment)

        val milk = cheese.milk.split("♂")
        milkTypeEditText.setText(milk[0])
        milkVolumeEditText.setText(milk[1])
        milkAgeEditText.setText(milk[2])

        compositionEditText.setText(cheese.composition)

//        val stages = cheese.stages.split("♂")
//        for (stage in stages)
//            addStage(stage)

        val barcodeBitmap =
            BarcodeEncoder().encodeBitmap(cheese.id.toString(), BarcodeFormat.CODE_128, 550, 100)
        barcodeImage.setImageBitmap(barcodeBitmap)
        barcodeImage.isVisible = true
    }

    private fun initInputListeners() {
        nameEditText.doAfterTextChanged {
            viewModel.name.value = it?.toString()?.trim()
        }
        dateText.doAfterTextChanged {
            viewModel.date.value = it?.toString()
        }
        dateButton.setOnClickListener {
            DateTimePicker(this).pickDate {
                dateText.text = it
            }
        }
        recipeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(adapter: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val recipe = adapter?.getItemAtPosition(p2)?.toString()
//                saveStages()
                viewModel.selectRecipe(recipe)
            }
        }
        commentEditText.doAfterTextChanged {
            viewModel.comment.value = it?.toString()?.trim()
        }
        milkTypeEditText.doAfterTextChanged {
            if (milkTypeEditText.error != null)
                milkTypeEditText.error = null
            viewModel.milkType.value = it?.toString()?.trim()
        }
        milkVolumeEditText.doAfterTextChanged {
            if (milkVolumeEditText.error != null)
                milkVolumeEditText.error = null
            viewModel.milkVolume.value = it?.toString()?.trim()
        }
        milkAgeEditText.doAfterTextChanged {
            viewModel.milkAge.value = it?.toString()?.trim()
        }
        compositionEditText.doAfterTextChanged {
            viewModel.composition.value = it?.toString()?.trim()
        }

        barcodeImage.setOnClickListener {
            viewModel.shareCheese()
        }
    }

    private fun initToolbar() {
        toolbar.setTitle(R.string.toolbar_manage_cheese)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setOnApplyWindowInsetsListener { _, insets ->
            val statusBarHeight = insets.systemWindowInsetTop

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

    private fun initRecycler() {
        adapter = PhotoAdapter(this)
        photoRecycler.adapter = adapter
        photoRecycler.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL)
    }

    private fun animateToolbarColorChange(newColor: Int) {
        if (newColor == oldToolbarColor) return

        val animator = ViewAnimationUtils.createCircularReveal(
            reveal,
            toolbar.width / 2,
            toolbar.height / 2,
            0F,
            toolbar.width / 2F
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

        animator.duration = 200
        animator.start()
    }

    private fun initParamsClickListeners() {
        milkButton.setOnClickListener(dropDownButtonClick)
        milkText.setOnClickListener {
            milkButton.performClick()
        }

        compositionButton.setOnClickListener(dropDownButtonClick)
        compositionText.setOnClickListener {
            compositionButton.performClick()
        }

        stagesButton.setOnClickListener(dropDownButtonClick)
        stagesText.setOnClickListener {
            stagesButton.performClick()
        }
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
}