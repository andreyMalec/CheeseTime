package com.malec.cheesetime.ui.main.cheeseList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.filterButton -> {
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
            val helper = when (item.itemId) {
                R.id.filterDateStart -> getString(R.string.cheese_date_format)
                R.id.filterDateEnd -> getString(R.string.cheese_date_format)
                R.id.filterCheeseType -> ""
                else -> null
            }
            helper?.let {
                showDialog(item.title.toString(), it)
            }

            if (item.groupId == R.id.menuFilterGroup)
                currentFilter = item
            else
                currentSort = item

            true
        }

        adapter = CheeseAdapter(viewModel)
        cheeseRecycler.adapter = adapter
        cheeseRecycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        viewModel.cheeseList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
            swipeRefresh.isRefreshing = false
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
                currentFilter?.title = getString(R.string.filter_cheese_type) + ": " + (result ?: "")
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