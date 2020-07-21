package com.malec.cheesetime.ui.main.cheeseList

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.malec.cheesetime.R
import com.malec.cheesetime.di.Injectable
import com.malec.cheesetime.ui.main.dialog.FilterDialog
import kotlinx.android.synthetic.main.fragment_cheese_list.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class CheeseListFragment : Fragment(), Injectable, FilterDialog.DialogListener {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: CheeseListViewModel by viewModels {
        viewModelFactory
    }

    private lateinit var adapter: CheeseAdapter

    private var currentFilter: MenuItem? = null
    private var currentSort: MenuItem? = null
    private var dateFilterStart: MenuItem? = null
    private var dateFilterEnd: MenuItem? = null
    private var cheeseTypeFilter: MenuItem? = null

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
                if (cheeseListDrawer.isDrawerOpen(GravityCompat.START))
                    cheeseListDrawer.closeDrawer(GravityCompat.START)
                else
                    cheeseListDrawer.openDrawer(GravityCompat.START)
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
                viewModel.deleteSelected()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showDialog(title: String, helper: String) {
        val dialog = FilterDialog(this, title, helper)
        dialog.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme_Dialog)
        dialog.show(requireActivity().supportFragmentManager, "FilterDialog")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        initViewModelListeners()

        initNavigationListeners()
        initRecycler()
    }

    private fun initViewModelListeners() {
        viewModel.selectedCount.observe(viewLifecycleOwner, Observer { count ->
            val toolbar = (requireActivity() as AppCompatActivity).supportActionBar
            toolbar?.title = if (count == 0) {
                showMainMenu()
                getString(R.string.app_name)
            } else {
                showSelectMenu()
                getString(R.string.toolbar_n_selected, count)
            }
        })

        viewModel.cheeseList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
            swipeRefresh.isRefreshing = false
        })
    }

    private fun showMainMenu() {
        searchButton?.isVisible = true
        filterButton?.isVisible = true
        scanButton?.isVisible = true
        archiveButton?.isVisible = false
        printButton?.isVisible = false
        deleteButton?.isVisible = false
    }

    private fun showSelectMenu() {
        searchButton?.isVisible = false
        filterButton?.isVisible = false
        scanButton?.isVisible = false
        archiveButton?.isVisible = true
        printButton?.isVisible = true
        deleteButton?.isVisible = true
    }

    private fun initNavigationListeners() {
        cheeseNavView.setNavigationItemSelectedListener { item ->
            if (item.itemId == R.id.filterClear) {
                clearFilter()
                cheeseListDrawer.closeDrawer(GravityCompat.START)
                return@setNavigationItemSelectedListener true
            }

            prepareNavigationDialogContent(item)?.let {
                showDialog(item.title.toString(), it)
            }

            if (item.groupId == R.id.menuFilterGroup)
                currentFilter = item
            else if (item.groupId == R.id.menuSortGroup) {
                currentSort = item
                viewModel.sortBy.value = currentSort?.title?.toString()
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
    }

    private fun clearFilter() {
        viewModel.dateFilterStart.value = null
        viewModel.dateFilterEnd.value = null
        viewModel.cheeseTypeFilter.value = null
        viewModel.sortBy.value = null
        currentFilter = null
        currentSort?.isChecked = false
        currentSort = null
        dateFilterStart?.title = getString(R.string.filter_date_start)
        dateFilterEnd?.title = getString(R.string.filter_date_end)
        cheeseTypeFilter?.title = getString(R.string.filter_cheese_type)
    }

    private fun prepareNavigationDialogContent(item: MenuItem): String? {
        return when (item.itemId) {
            R.id.filterDateStart -> {
                dateFilterStart = item
                getString(R.string.cheese_date_format)
            }
            R.id.filterDateEnd -> {
                dateFilterEnd = item
                getString(R.string.cheese_date_format)
            }
            R.id.filterCheeseType -> {
                cheeseTypeFilter = item
                ""
            }
            else -> null
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

    override fun onDialogFinish(result: String?) {
        when (currentFilter?.itemId) {
            R.id.filterDateStart -> {
                currentFilter?.title = getString(R.string.filter_date_start) + ": " + (result ?: "")
                viewModel.dateFilterStart.value = result
            }
            R.id.filterDateEnd -> {
                currentFilter?.title = getString(R.string.filter_date_end) + ": " + (result ?: "")
                viewModel.dateFilterEnd.value = result
            }
            R.id.filterCheeseType -> {
                currentFilter?.title =
                    getString(R.string.filter_cheese_type) + ": " + (result ?: "")
                viewModel.cheeseTypeFilter.value = result
            }
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