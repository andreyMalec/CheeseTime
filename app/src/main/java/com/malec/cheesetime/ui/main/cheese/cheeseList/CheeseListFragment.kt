package com.malec.cheesetime.ui.main.cheese.cheeseList

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RelativeLayout
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.switchmaterial.SwitchMaterial
import com.malec.cheesetime.R
import com.malec.cheesetime.databinding.FragmentCheeseListBinding
import com.malec.cheesetime.di.Injectable
import com.malec.cheesetime.model.CheeseSort
import com.malec.cheesetime.ui.allertDialogBuilder.CheeseDeleteDialog
import com.malec.cheesetime.ui.main.DeleteSwipeCallback
import com.malec.cheesetime.util.DateTimePicker
import javax.inject.Inject

class CheeseListFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: CheeseListViewModel by viewModels {
        viewModelFactory
    }

    private val cheeseTypes = mutableListOf<String>()

    private lateinit var adapter: CheeseAdapter
    private lateinit var cheeseTypeAdapter: ArrayAdapter<String>
    private lateinit var cheeseTypeSpinner: Spinner
    private lateinit var archivedSwitch: SwitchMaterial

    private var dateFilterStart: MenuItem? = null
    private var dateFilterEnd: MenuItem? = null
    private var cheeseTypeFilter: MenuItem? = null
    private var archivedFilter: MenuItem? = null
    private var dateSortStart: MenuItem? = null
    private var dateSortEnd: MenuItem? = null
    private var cheeseTypeSort: MenuItem? = null

    private var isMainMenu = true

    private var _binding: FragmentCheeseListBinding? = null
    private val binding get() = _binding!!

    private var searchView: SearchView? = null

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (isMainMenu)
            inflater.inflate(R.menu.menu_main, menu)
        else
            inflater.inflate(R.menu.menu_main_selected, menu)

        if (searchView == null) {
            searchView = menu.findItem(R.id.appBarSearch)?.actionView as SearchView?
            initSearchView()
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun initSearchView() {
        searchView?.findViewById<View>(androidx.appcompat.R.id.search_plate)
            ?.setBackgroundResource(android.R.color.transparent)
        searchView?.queryHint = getString(R.string.search_hint)

        initSearchViewListener()
    }

    private fun initSearchViewListener() {
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.searchQuery.value = newText

                return false
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.filterButton -> {
                if (binding.cheeseListDrawer.isDrawerOpen(GravityCompat.END))
                    binding.cheeseListDrawer.closeDrawer(GravityCompat.END)
                else
                    binding.cheeseListDrawer.openDrawer(GravityCompat.END)
                true
            }
            R.id.archiveButton -> {
                viewModel.archiveSelected()
                true
            }
            R.id.printButton -> {
                viewModel.printSelected()
                true
            }
            R.id.deleteButton -> {
                CheeseDeleteDialog(requireContext()).setOnOkButtonClickListener {
                    viewModel.deleteSelected()
                }.show(viewModel.selectedCount.value)
                true
            }
            android.R.id.home -> {
                viewModel.unselect()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        initFilterMenu()

        initViewModelListeners()

        initNavigationListeners()
        initRecycler()
    }

    private fun initFilterMenu() {
        dateFilterStart = binding.cheeseNavView.menu.findItem(R.id.filterDateStart)
        dateFilterEnd = binding.cheeseNavView.menu.findItem(R.id.filterDateEnd)
        cheeseTypeFilter = binding.cheeseNavView.menu.findItem(R.id.filterCheeseType)
        archivedFilter = binding.cheeseNavView.menu.findItem(R.id.filterArchived)
        dateSortStart = binding.cheeseNavView.menu.findItem(R.id.sortDateStart)
        dateSortEnd = binding.cheeseNavView.menu.findItem(R.id.sortDateEnd)
        cheeseTypeSort = binding.cheeseNavView.menu.findItem(R.id.sortCheeseType)
    }

    private fun initViewModelListeners() {
        viewModel.selectedCount.observe(viewLifecycleOwner, { count ->
            val toolbar = (requireActivity() as AppCompatActivity).supportActionBar
            isMainMenu = count == 0
            updateMenu()
            toolbar?.setDisplayHomeAsUpEnabled(!isMainMenu)
            toolbar?.title = if (isMainMenu)
                getString(R.string.app_name)
            else
                getString(R.string.toolbar_n_selected, count)
        })

        viewModel.cheeseTypes.observe(viewLifecycleOwner, {
            cheeseTypeAdapter.clear()
            it?.let { cheeseTypes ->
                cheeseTypeAdapter.addAll(cheeseTypes)
            }
            cheeseTypeAdapter.notifyDataSetChanged()
        })

        viewModel.cheeseList.observe(viewLifecycleOwner, {
            adapter.submitList(it)
            binding.swipeRefresh.isRefreshing = false
        })
    }

    private fun updateMenu() {
        requireActivity().invalidateOptionsMenu()
    }

    private fun initNavigationListeners() {
        binding.cheeseNavView.setNavigationItemSelectedListener { item ->
            if (item.itemId == R.id.filterClear) {
                clearFilter()
                return@setNavigationItemSelectedListener true
            }

            prepareFilterDialog(item.itemId)

            if (item.groupId == R.id.menuSortGroup)
                viewModel.sortBy.value = when (item.itemId) {
                    R.id.sortDateStart -> CheeseSort.DATE_START
                    R.id.sortDateEnd -> CheeseSort.DATE_END
                    R.id.sortCheeseType -> CheeseSort.TYPE
                    else -> CheeseSort.ID
                }

            true
        }

        binding.cheeseListDrawer.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(newState: Int) {}
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
            override fun onDrawerOpened(drawerView: View) {}

            override fun onDrawerClosed(drawerView: View) {
                viewModel.searchQuery.value = viewModel.searchQuery.value
            }
        })

        archivedSwitch =
            (archivedFilter?.actionView as RelativeLayout).getChildAt(0) as SwitchMaterial
        archivedSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.archivedFilter.value = isChecked
        }

        cheeseTypeSpinner =
            (cheeseTypeFilter?.actionView as RelativeLayout).getChildAt(0) as Spinner
        cheeseTypeAdapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                cheeseTypes
            )
        cheeseTypeSpinner.adapter = cheeseTypeAdapter
        cheeseTypeSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {}

                override fun onItemSelected(
                    adapter: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long
                ) {
                    val type = adapter?.getItemAtPosition(position)?.toString()
                    viewModel.cheeseTypeFilter.value =
                        if (position == 0) null
                        else type
                }
            }
    }

    private fun clearFilter() {
        viewModel.dateFilterStart.value = null
        viewModel.dateFilterEnd.value = null
        viewModel.cheeseTypeFilter.value = null
        viewModel.sortBy.value = null

        dateFilterStart?.title = getString(R.string.filter_date_start)
        dateFilterEnd?.title = getString(R.string.filter_date_end)
        cheeseTypeFilter?.title = getString(R.string.filter_cheese_type)
        cheeseTypeSpinner.setSelection(0)
        archivedSwitch.isChecked = false
        dateSortStart?.isChecked = false
        dateSortEnd?.isChecked = false
        cheeseTypeSort?.isChecked = false
    }

    private fun prepareFilterDialog(itemId: Int) {
        when (itemId) {
            R.id.filterDateStart -> {
                DateTimePicker(requireActivity()).pickDate {
                    dateFilterStart?.title = getString(R.string.filter_date_start) + ": " + it
                    viewModel.dateFilterStart.value = it
                }
            }
            R.id.filterDateEnd -> {
                DateTimePicker(requireActivity()).pickDate {
                    dateFilterEnd?.title = getString(R.string.filter_date_end) + ": " + it
                    viewModel.dateFilterEnd.value = it
                }
            }
            R.id.filterCheeseType -> {
                cheeseTypeSpinner.performClick()
            }
            R.id.filterArchived -> {
                archivedSwitch.performClick()
            }
        }
    }

    private fun initRecycler() {
        adapter = CheeseAdapter(viewModel)
        binding.cheeseRecycler.adapter = adapter
        binding.cheeseRecycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                if (fromPosition == 0 || toPosition == 0) binding.cheeseRecycler.scrollToPosition(0)
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (positionStart == 0) binding.cheeseRecycler.scrollToPosition(0)
            }
        })

        val helper = ItemTouchHelper(
            DeleteSwipeCallback(
                ::onTaskSwipe,
                requireContext()
            )
        )
        helper.attachToRecyclerView(binding.cheeseRecycler)

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.update()
        }
    }

    private fun onTaskSwipe(position: Int) {
        CheeseDeleteDialog(requireContext()).setOnOkButtonClickListener {
            viewModel.onSwipe(adapter.currentList[position])
        }.setOnCancelButtonClickListener {
            Handler(Looper.getMainLooper()).postDelayed({
                adapter.notifyItemChanged(position)
            }, 400)
        }.show()
    }

    override fun onResume() {
        super.onResume()
        viewModel.update()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheeseListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}