package com.malec.cheesetime.ui.cheeseManage

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.children
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.malec.cheesetime.R
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.android.synthetic.main.activity_cheese_manage.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_cheese_manage, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.saveButton -> saveCheese()
            R.id.deleteButton -> deleteCheese()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun saveCheese() {
        val name = nameEditText.text?.toString()?.trim()
        val date = dateEditText.text?.toString()?.trim()
        val recipe = recipeSpinner.selectedItem.toString()
        val comment = commentEditText.text?.toString()?.trim()
        val milkType = milkTypeEditText.text?.toString()?.trim()
        val milkVolume = milkVolumeEditText.text?.toString()?.trim()
        val milkAge = milkAgeEditText.text?.toString()?.trim()
        val milk = "$milkType;$milkVolume;$milkAge"
        val composition = compositionEditText.text?.toString()?.trim()
        var s = ""
        for (et in stagesParamsLayout.children)
            s += (et as EditText).text?.toString() + ";"
        val stages = s.dropLast(1)

        viewModel.checkAndManageCheese(name, date, recipe, comment, milk, composition, stages)
    }

    private fun deleteCheese() {
        viewModel.deleteCheese()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheese_manage)

        initViewModelListeners()
        initColorClickListener()
        initParamsClickListeners()
        initToolbar()
    }

    private fun initViewModelListeners() {

        viewModel.badgeColor.observe(this, Observer { color ->
            Handler().postDelayed({
                animateToolbarColorChange(color)
            }, 200)
        })
    }

    private fun initToolbar() {
        toolbar.setTitle(R.string.toolbar_manage_cheese)
        setSupportActionBar(toolbar)

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