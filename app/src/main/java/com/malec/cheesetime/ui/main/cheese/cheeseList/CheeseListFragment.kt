package com.malec.cheesetime.ui.main.cheese.cheeseList

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.malec.cheesetime.R
import com.malec.cheesetime.di.Injectable
import com.malec.cheesetime.model.CheeseSort
import com.malec.cheesetime.ui.main.AlertDialogBuilder
import com.malec.cheesetime.util.DateTimePicker
import kotlinx.android.synthetic.main.fragment_cheese_list.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
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
    private lateinit var archivedSwitch: Switch

    private var dateFilterStart: MenuItem? = null
    private var dateFilterEnd: MenuItem? = null
    private var cheeseTypeFilter: MenuItem? = null
    private var archivedFilter: MenuItem? = null
    private var dateSortStart: MenuItem? = null
    private var dateSortEnd: MenuItem? = null
    private var cheeseTypeSort: MenuItem? = null

    private var searchButton: MenuItem? = null
    private var filterButton: MenuItem? = null
    private var scanButton: MenuItem? = null
    private var archiveButton: MenuItem? = null
    private var printButton: MenuItem? = null
    private var deleteButton: MenuItem? = null

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        searchButton = menu.findItem(R.id.appBarSearch)
        filterButton = menu.findItem(R.id.filterButton)
        scanButton = menu.findItem(R.id.scanButton)
        archiveButton = menu.findItem(R.id.archiveButton)
        printButton = menu.findItem(R.id.printButton)
        deleteButton = menu.findItem(R.id.deleteButton)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.filterButton -> {
                if (cheeseListDrawer.isDrawerOpen(GravityCompat.END))
                    cheeseListDrawer.closeDrawer(GravityCompat.END)
                else
                    cheeseListDrawer.openDrawer(GravityCompat.END)
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
                AlertDialogBuilder(requireContext()).setOnOkButtonClickListener {
                    viewModel.deleteSelected()
                }.showCheeseDialog(viewModel.selectedCount.value)

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
        dateFilterStart = cheeseNavView.menu.findItem(R.id.filterDateStart)
        dateFilterEnd = cheeseNavView.menu.findItem(R.id.filterDateEnd)
        cheeseTypeFilter = cheeseNavView.menu.findItem(R.id.filterCheeseType)
        archivedFilter = cheeseNavView.menu.findItem(R.id.filterArchived)
        dateSortStart = cheeseNavView.menu.findItem(R.id.sortDateStart)
        dateSortEnd = cheeseNavView.menu.findItem(R.id.sortDateEnd)
        cheeseTypeSort = cheeseNavView.menu.findItem(R.id.sortCheeseType)
    }

    private fun initViewModelListeners() {
        viewModel.selectedCount.observe(viewLifecycleOwner, Observer { count ->
            val toolbar = (requireActivity() as AppCompatActivity).supportActionBar
            toolbar?.title = if (count == 0) {
                showMainMenu(true)
                toolbar?.setDisplayHomeAsUpEnabled(false)
                getString(R.string.app_name)
            } else {
                showMainMenu(false)
                toolbar?.setDisplayHomeAsUpEnabled(true)
                getString(R.string.toolbar_n_selected, count)
            }
        })

        viewModel.cheeseTypes.observe(viewLifecycleOwner, Observer {
            cheeseTypeAdapter.clear()
            it?.let { cheeseTypes ->
                cheeseTypeAdapter.addAll(cheeseTypes)
            }
            cheeseTypeAdapter.notifyDataSetChanged()
        })

        viewModel.cheeseList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
            swipeRefresh.isRefreshing = false
        })
    }

    private fun showMainMenu(show: Boolean) {
        searchButton?.isVisible = show
        filterButton?.isVisible = show
        scanButton?.isVisible = show
        archiveButton?.isVisible = !show
        printButton?.isVisible = !show
        deleteButton?.isVisible = !show
    }

    private fun initNavigationListeners() {
        cheeseNavView.setNavigationItemSelectedListener { item ->
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

        cheeseListDrawer.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(newState: Int) {}
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
            override fun onDrawerOpened(drawerView: View) {}

            override fun onDrawerClosed(drawerView: View) {
                viewModel.applyFilters()
            }
        })

        archivedSwitch =
            (archivedFilter?.actionView as RelativeLayout).getChildAt(0) as Switch
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
                    p2: Int,
                    p3: Long
                ) {
                    val type = adapter?.getItemAtPosition(p2)?.toString()
                    viewModel.cheeseTypeFilter.value = if (p2 == 0) null
                    else type
                }
            }
    }

    private fun clearFilter() {
        viewModel.dateFilterStart.value = null
        viewModel.dateFilterEnd.value = null
        viewModel.cheeseTypeFilter.value = null
        viewModel.sortBy.value = null
        dateSortStart?.isChecked = false
        dateSortEnd?.isChecked = false
        cheeseTypeSort?.isChecked = false
        dateFilterStart?.title = getString(R.string.filter_date_start)
        dateFilterEnd?.title = getString(R.string.filter_date_end)
        cheeseTypeFilter?.title = getString(R.string.filter_cheese_type)
        cheeseTypeSpinner.setSelection(0)
        archivedSwitch.isChecked = false
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
        cheeseRecycler.adapter = adapter
        cheeseRecycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                if (fromPosition == 0 || toPosition == 0) cheeseRecycler.scrollToPosition(0)
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (positionStart == 0) cheeseRecycler.scrollToPosition(0)
            }
        })

        swipeRefresh.setOnRefreshListener {
            viewModel.update()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.update()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cheese_list, container, false)
    }
}