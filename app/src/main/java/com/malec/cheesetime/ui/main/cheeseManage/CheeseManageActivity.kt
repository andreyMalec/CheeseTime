package com.malec.cheesetime.ui.main.cheeseManage

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.malec.cheesetime.R
import com.malec.cheesetime.model.Cheese
import com.malec.cheesetime.ui.Screens
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.android.synthetic.main.activity_cheese_manage.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
class CheeseManageActivity : AppCompatActivity(), HasAndroidInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: CheeseManageViewModel by viewModels {
        viewModelFactory
    }
    private var oldToolbarColor = 0

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

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_cheese_manage, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.saveButton -> viewModel.checkAndManageCheese()
            R.id.deleteButton -> viewModel.deleteCheese()
            android.R.id.home -> onBackPressed()
        }

        return super.onOptionsItemSelected(item)
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
                if (dateEditText.text.isNullOrBlank())
                    dateLayout.error = getString(R.string.required_field_error)
                if (milkTypeEditText.text.isNullOrBlank())
                    milkTypeEditText.error = getString(R.string.required_field_error)
                if (milkVolumeEditText.text.isNullOrBlank())
                    milkVolumeEditText.error = getString(R.string.required_field_error)
                if (milkAgeEditText.text.isNullOrBlank())
                    milkAgeEditText.error = getString(R.string.required_field_error)
            }
        })

        viewModel.isInvalidDateError.observe(this, Observer { isError ->
            if (isError) {
                showError(getString(R.string.invalid_date_error))
                viewModel.isInvalidDateError.value = false
                dateLayout.error = getString(R.string.invalid_date_error) +
                        " (" + getString(R.string.cheese_date_format) + ")"
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

    private fun showError(message: String) {
        Snackbar.make(scrollView, message, Snackbar.LENGTH_LONG)
            .setBackgroundTint(ContextCompat.getColor(this, R.color.colorError))
            .setTextColor(ContextCompat.getColor(this, R.color.colorWhite))
            .show()
    }

    private fun showMessage(message: String) {
        val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast.view.background.colorFilter = PorterDuffColorFilter(
            ContextCompat.getColor(this, R.color.backgroundLight),
            PorterDuff.Mode.SRC_IN
        )
        toast.view.findViewById<TextView>(android.R.id.message).setTextColor(
            ContextCompat.getColor(this, R.color.colorAccent)
        )
        toast.show()
    }

    private fun showCheeseData(cheese: Cheese) {
        nameEditText.setText(cheese.name)

        val format = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        dateEditText.setText(format.format(cheese.date))

        val recipes = resources.getStringArray(R.array.recipes)
        recipeSpinner.setSelection(recipes.indexOf(cheese.recipe))

        commentEditText.setText(cheese.comment)

        val milk = cheese.milk.split("♂")
        milkTypeEditText.setText(milk[0])
        milkVolumeEditText.setText(milk[1])
        milkAgeEditText.setText(milk[2])

        compositionEditText.setText(cheese.composition)

        val stages = cheese.stages.split("♂")
        if (stages.size >= 3) {
            stage1EditText.setText(stages[0])
            stage2EditText.setText(stages[1])
            stage3EditText.setText(stages[2])
        }

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
        dateEditText.doAfterTextChanged {
            if (dateLayout.error != null)
                dateLayout.error = null
            viewModel.date.value = it?.toString()?.trim()
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
        val stages = mutableSetOf<String>()
        for (child in stagesParamsLayout.children)
            (child as EditText).doAfterTextChanged {
                it?.toString()?.trim()?.let { stage ->
                    stages.add(stage)
                }
                viewModel.stages.value = stages.toList()
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