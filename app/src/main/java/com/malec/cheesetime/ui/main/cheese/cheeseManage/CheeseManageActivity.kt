package com.malec.cheesetime.ui.main.cheese.cheeseManage

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.cardview.widget.CardView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.malec.cheesetime.R
import com.malec.cheesetime.model.Cheese
import com.malec.cheesetime.ui.BaseActivity
import com.malec.cheesetime.ui.Screens
import com.malec.cheesetime.util.DateFormatter
import com.malec.cheesetime.util.DateTimePicker
import kotlinx.android.synthetic.main.activity_cheese_manage.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class CheeseManageActivity : BaseActivity() {
    private val viewModel: CheeseManageViewModel by viewModels {
        viewModelFactory
    }
    private var oldToolbarColor = 0

    private val stages = mutableSetOf<String>()

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
        for (c in stagesParamsLayout.children)
            if (c.tag == "input")
                stages.add(
                    c.findViewById<EditText>(R.id.stageEditText).text?.toString()?.trim() ?: ""
                )
        viewModel.stages.value = stages.toList()
        viewModel.checkAndManageCheese()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheese_manage)

        val cheese = Screens.CheeseManageScreen.parseExtraIntent(intent)
        viewModel.setCheese(cheese)

        initViewModelListeners()
        initColorClickListener()
        initParamsClickListeners()
        initToolbar()
        initInputListeners()

        stageAddButton.setOnClickListener {
            addStage()
        }
    }

    private fun initViewModelListeners() {
        viewModel.cheese.observe(this, Observer { cheese ->
            if (cheese != null)
                showCheeseData(cheese)
        })

        viewModel.badgeColor.observe(this, Observer { color ->
            Handler().postDelayed({
                animateToolbarColorChange(color)
            }, 200)
        })

        viewModel.isFieldsEmptyError.observe(this, Observer { isError ->
            if (isError) {
                showError(getString(R.string.empty_fields_error))
                viewModel.isFieldsEmptyError.value = false

                if (nameEditText.text.isNullOrBlank())
                    nameLayout.error = getString(R.string.required_field_error)
                if (dateText.text.isNullOrBlank())
                    dateButton.error = getString(R.string.required_field_error)
                if (milkTypeEditText.text.isNullOrBlank())
                    milkTypeEditText.error = getString(R.string.required_field_error)
                if (milkVolumeEditText.text.isNullOrBlank())
                    milkVolumeEditText.error = getString(R.string.required_field_error)
                if (milkAgeEditText.text.isNullOrBlank())
                    milkAgeEditText.error = getString(R.string.required_field_error)
            }
        })

        viewModel.manageError.observe(this, Observer { message ->
            if (!message.isNullOrBlank()) {
                showError(message)
                viewModel.manageError.value = null
            }
        })

        viewModel.manageResult.observe(this, Observer { message ->
            if (!message.isNullOrBlank()) {
                showMessage(message)
                viewModel.manageResult.value = null
                onBackPressed()
            }
        })
    }

    private fun addStage(text: String? = null) {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val newStageLayout = inflater.inflate(R.layout.item_stage, null) as LinearLayout
        stagesParamsLayout.addView(newStageLayout, stagesParamsLayout.childCount - 1)
        val editText = newStageLayout.findViewById<EditText>(R.id.stageEditText)
        newStageLayout.findViewById<View>(R.id.stageButton).setOnClickListener {
            stagesParamsLayout.removeView(it.parent as View)
        }
        text?.let {
            editText.setText(text)
        }
    }

    private fun showCheeseData(cheese: Cheese) {
        nameEditText.setText(cheese.name)

        dateText.text = DateFormatter.simpleFormat(cheese.date)

        val recipes = resources.getStringArray(R.array.recipes)
        recipeSpinner.setSelection(recipes.indexOf(cheese.recipe))

        commentEditText.setText(cheese.comment)

        val milk = cheese.milk.split("♂")
        milkTypeEditText.setText(milk[0])
        milkVolumeEditText.setText(milk[1])
        milkAgeEditText.setText(milk[2])

        compositionEditText.setText(cheese.composition)

        val stages = cheese.stages.split("♂")
        for (stage in stages)
            addStage(stage)

        val bitmap =
            BarcodeEncoder().encodeBitmap(cheese.id.toString(), BarcodeFormat.CODE_128, 550, 100)
        barcodeImage.setImageBitmap(bitmap)
        barcodeImage.isVisible = true
    }

    private fun initInputListeners() {
        nameEditText.doAfterTextChanged {
            if (nameLayout.error != null)
                nameLayout.error = null
            viewModel.name.value = it?.toString()?.trim()
        }
        dateText.doAfterTextChanged {
            if (dateButton.error != null)
                dateButton.error = null
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
                viewModel.recipe.value = adapter?.getItemAtPosition(p2)?.toString()?.trim()
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
            if (milkAgeEditText.error != null)
                milkAgeEditText.error = null
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