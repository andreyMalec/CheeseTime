package com.malec.cheesetime.ui.main.cheese.cheeseManage.fragments

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import androidx.cardview.widget.CardView
import androidx.core.view.children
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.malec.cheesetime.Permissions
import com.malec.cheesetime.R
import com.malec.cheesetime.databinding.FragmentCheeseManageBinding
import com.malec.cheesetime.di.Injectable
import com.malec.cheesetime.model.Photo
import com.malec.cheesetime.ui.allertDialogBuilder.CheeseDeleteDialog
import com.malec.cheesetime.ui.main.cheese.cheeseManage.AttachSourceDialog
import com.malec.cheesetime.ui.main.cheese.cheeseManage.CheeseManageViewModel
import com.malec.cheesetime.ui.main.cheese.cheeseManage.PhotoMenuBuilder
import com.malec.cheesetime.ui.main.cheese.cheeseManage.StringAdapter
import com.malec.cheesetime.util.DateFormatter
import com.malec.cheesetime.util.DateTimePicker

class CheeseManageFragment : PhotoManager(), PhotoAdapter.PhotoAction, Injectable {
    override val viewModel: CheeseManageViewModel by activityViewModels {
        viewModelFactory
    }

    private var _binding: FragmentCheeseManageBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: PhotoAdapter
    private lateinit var recipeAdapter: ArrayAdapter<String>
    private lateinit var stagesAdapter: StringAdapter

    private var saveButton: MenuItem? = null
    private var deleteButton: MenuItem? = null
    private var isDeleteActive = false
    private var isSaveActive = false

    private val recipes = mutableListOf<String>()

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_manage, menu)

        saveButton = menu.findItem(R.id.saveButton)
        saveButton?.isVisible = isSaveActive

        deleteButton = menu.findItem(R.id.deleteButton)
        deleteButton?.isVisible = isDeleteActive
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.saveButton -> viewModel.checkAndManageCheese()
            R.id.deleteButton ->
                CheeseDeleteDialog(requireContext()).setOnOkButtonClickListener {
                    viewModel.deleteCheese()
                }.show()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModelListeners()
        initColorClickListener()
        initClickListeners()
        initInputListeners()
        initPhotoRecycler()

        setHasOptionsMenu(true)

        recipeAdapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                recipes
            )
        binding.recipeSpinner.adapter = recipeAdapter

        stagesAdapter = StringAdapter(viewModel)
        binding.stagesRecycler.adapter = stagesAdapter
        binding.stagesRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.root.setOnApplyWindowInsetsListener { _, insets ->
            val statusBarHeight =
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R)
                    insets.getInsets(WindowInsets.Type.systemBars()).top
                else
                    insets.systemWindowInsetTop

            val h =
                (resources.getDimension(R.dimen.toolbar_height) + statusBarHeight).toInt()
            val lp = binding.scrollView.layoutParams as FrameLayout.LayoutParams
            binding.scrollView.layoutParams = lp.also { it.topMargin = h }
            insets
        }
    }

    private fun showImageDialog() {
        AttachSourceDialog { result ->
            when (result) {
                AttachSourceDialog.DialogResult.GALLERY -> {
                    viewModel.onAttachFromGallery()
                }
                AttachSourceDialog.DialogResult.CAMERA ->
                    checkOrRequestPermission(Permissions.CAMERA) {
                        viewModel.onAttachFromCamera()
                    }
            }
        }.show(requireActivity().supportFragmentManager, "SelectAttachSource")
    }

    private fun initViewModelListeners() {
        viewModel.stages.observe(viewLifecycleOwner, { stages ->
            stagesAdapter.submitList(stages)
            (binding.stagesRecycler.parent as View).invalidate()
        })

        viewModel.isSaveActive.observe(viewLifecycleOwner, { active ->
            saveButton?.isVisible = active
            isSaveActive = active
        })

        viewModel.isDeleteActive.observe(viewLifecycleOwner, { active ->
            deleteButton?.isVisible = active
            isDeleteActive = active
        })

        viewModel.recipes.observe(viewLifecycleOwner, {
            recipeAdapter.clear()
            it?.let { recipes ->
                recipeAdapter.addAll(recipes)
                binding.recipeSpinner.setSelection(
                    recipes.indexOf(
                        viewModel.cheese.value?.recipe ?: ""
                    )
                )
            }
            recipeAdapter.notifyDataSetChanged()
        })

        viewModel.photos.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })
    }

    private fun initInputListeners() {
        binding.nameEditText.doAfterTextChanged {
            viewModel.checkCanSave()
        }
        binding.dateButton.setOnClickListener {
            DateTimePicker(requireActivity()).pickDate {
                viewModel.cheese.value?.date = DateFormatter.dateFromString(it)
                binding.dateText.text = it
            }
        }
        binding.recipeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(adapter: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val recipe = adapter?.getItemAtPosition(p2)?.toString()
                viewModel.selectRecipe(recipe)
            }
        }
        binding.milkTypeEditText.doAfterTextChanged {
            viewModel.checkCanSave()
        }
        binding.milkVolumeEditText.doAfterTextChanged {
            viewModel.checkCanSave()
        }

        binding.barcodeImage.setOnClickListener {
            viewModel.shareCheese()
        }
    }

    override fun onPhotoClick(photo: Photo) {
        viewModel.onPhotoClick(photo)
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
                deletePhotoDialog(requireContext(), photo)
            }
            .show(requireActivity().supportFragmentManager)
    }


    private fun initPhotoRecycler() {
        adapter = PhotoAdapter(this)
        binding.photoRecycler.adapter = adapter
        binding.photoRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun initColorClickListener() {
        for (child in binding.colorLayout1.children + binding.colorLayout2.children) {
            child.setOnClickListener {
                if (it is CardView) {
                    val newColor = it.cardBackgroundColor.defaultColor
                    viewModel.badgeColor.value = newColor
                }
            }
        }
    }

    private fun initClickListeners() {
        binding.stageAddButton.setOnClickListener {
            viewModel.addNewStage()
        }

        binding.photoAddButton.setOnClickListener {
            showImageDialog()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheeseManageBinding.inflate(inflater, parent, false)
        binding.cheese = viewModel.cheese.value
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}