package com.malec.cheesetime.ui.main.cheeseList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.filterButton -> {
                if (cheeseListDrawer.isDrawerOpen(GravityCompat.START))
                    cheeseListDrawer.closeDrawer(GravityCompat.START)
                else
                    cheeseListDrawer.openDrawer(GravityCompat.START)
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
        cheeseNavView.setNavigationItemSelectedListener { item ->
            if (item.itemId == R.id.filterClear) {
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
                cheeseListDrawer.closeDrawer(GravityCompat.START)

                return@setNavigationItemSelectedListener true
            }

            val helper = when (item.itemId) {
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
            helper?.let {
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

        viewModel.cheeseList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
            swipeRefresh.isRefreshing = false
        })

        swipeRefresh.setOnRefreshListener {
            viewModel.update()
        }

        cheeseListDrawer.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(newState: Int) {}

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}

            override fun onDrawerClosed(drawerView: View) {
                viewModel.applyFilters()
            }

            override fun onDrawerOpened(drawerView: View) {}
        })
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