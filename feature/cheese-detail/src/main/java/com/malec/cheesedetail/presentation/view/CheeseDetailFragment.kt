package com.malec.cheesedetail.presentation.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.children
import androidx.core.view.updatePadding
import androidx.core.widget.doAfterTextChanged
import com.malec.cheesedetail.R
import com.malec.cheesedetail.databinding.FragmentCheeseDetailBinding
import com.malec.cheesedetail.di.CheeseDetailComponent
import com.malec.cheesedetail.di.CheeseDetailComponentProvider
import com.malec.cheesedetail.presentation.store.CheeseDetailState
import com.malec.cheesedetail.presentation.viewcontroller.CheeseDetailViewController
import com.malec.injection.ComponentOwner
import com.malec.presentation.base.BaseFragment
import com.malec.presentation.dialog.CheeseDeleteDialog
import com.malec.presentation.dialog.DateTimePicker
import com.malec.presentation.initToolbar
import com.malec.presentation.navigation.BackButtonListener
import com.malec.presentation.unidirectional.BaseView
import com.malec.presentation.update
import javax.inject.Inject

class CheeseDetailFragment : BaseFragment<FragmentCheeseDetailBinding>(),
    ComponentOwner<CheeseDetailComponent>, BaseView<CheeseDetailState>, BackButtonListener {

    @Inject
    internal lateinit var viewController: CheeseDetailViewController

    private var saveButton: MenuItem? = null
    private var deleteButton: MenuItem? = null

    private var oldToolbarColor = 0
    private var oldToolbarTitle: String = ""
    private var colorWhite = 0
    private var backgroundShadow: Drawable? = null
    private var backgroundTransparent: Drawable? = null

    private lateinit var recipeAdapter: ArrayAdapter<String>

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()

        inflater.inflate(R.menu.menu_manage, menu)

        saveButton = menu.findItem(R.id.saveButton)
        deleteButton = menu.findItem(R.id.deleteButton)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.saveButton -> {
                viewController.saveCheese()
            }
            R.id.deleteButton ->
                CheeseDeleteDialog(requireContext()).setOnOkButtonClickListener {
                    viewController.deleteCheese()
                }.show()
            android.R.id.home -> onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun renderState(state: CheeseDetailState) {
        binding.cheese = state.cheese
        recipeAdapter.update(state.recipes.map {
            it.name
        })
        saveButton?.isVisible = state.isSaveActive
        deleteButton?.isVisible = state.isDeleteActive
        animateToolbarColorChange(state.cheese.badgeColor)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getLong(CHEESE_ID)?.let {
            viewController.loadCheese(it)
        }

        recipeAdapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                mutableListOf()
            )
        binding.recipeSpinner.adapter = recipeAdapter

        initToolbar()
        initInputListeners()
    }

    private fun initInputListeners() {
        binding.nameEditText.doAfterTextChanged {
            viewController.setName(it.toString())
        }
        binding.dateButton.setOnClickListener {
            DateTimePicker(requireActivity(), requireActivity().supportFragmentManager).pickDate {
                viewController.setDate(it)
            }
        }
        binding.recipeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(adapter: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                viewController.selectRecipeAtPosition(p2)
            }
        }
        binding.commentEditText.doAfterTextChanged {
            viewController.setComment(it.toString())
        }
        binding.milkTypeEditText.doAfterTextChanged {
            viewController.setMilkType(it.toString())
        }
        binding.milkVolumeEditText.doAfterTextChanged {
            viewController.setMilkVolume(it.toString())
        }
        binding.milkAgeEditText.doAfterTextChanged {
            viewController.setMilkAge(it.toString())
        }
        binding.compositionEditText.doAfterTextChanged {
            viewController.setComposition(it.toString())
        }
        binding.volumeEditText.doAfterTextChanged {
            viewController.setVolume(it.toString())
        }
        binding.volumeMaxEditText.doAfterTextChanged {
            viewController.setVolumeMax(it.toString())
        }

        binding.barcodeImage.setOnClickListener {
//            viewModel.shareCheese()
        }

        initColorClickListener()
    }

    private fun initColorClickListener() {
        for (child in binding.colorLayout1.children + binding.colorLayout2.children) {
            child.setOnClickListener {
                if (it is CardView) {
                    val newColor = it.cardBackgroundColor.defaultColor
                    viewController.setBadgeColor(newColor)
                }
            }
        }
    }

    private fun initToolbar() {
        setHasOptionsMenu(true)
        val activity = (requireActivity() as AppCompatActivity)
        activity.initToolbar(binding.root, binding.toolbar, R.string.toolbar_edit_cheese)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.reveal.layoutParams = binding.toolbar.layoutParams
        binding.reveal.updatePadding(top = binding.toolbar.paddingTop)
        binding.revealBackground.layoutParams = binding.toolbar.layoutParams
        binding.revealBackground.updatePadding(top = binding.toolbar.paddingTop)
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
                oldToolbarColor = newColor
            }

            override fun onAnimationEnd(animation: Animator?) {
                requireActivity().window.statusBarColor = newColor
            }
        })

        animator.duration = 300
        animator.start()
    }

    override fun provideComponent() = CheeseDetailComponentProvider.getInstance().component

    override fun inject(component: CheeseDetailComponent) {
        component.inject(this)
    }

    override fun createViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        attachToParent: Boolean
    ) = FragmentCheeseDetailBinding.inflate(inflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(viewController)
    }

    override fun onBackPressed() {
        viewController.onBackClick()
    }

    companion object {
        private const val CHEESE_ID = "CheeseId"

        fun newInstance(cheeseId: Long?): CheeseDetailFragment {
            return CheeseDetailFragment().apply {
                arguments = Bundle().apply {
                    if (cheeseId != null)
                        putLong(CHEESE_ID, cheeseId)
                }
            }
        }
    }
}