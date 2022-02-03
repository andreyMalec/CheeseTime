package com.malec.cheeselist.presentation.view

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.switchmaterial.SwitchMaterial
import com.malec.cheeselist.R
import com.malec.cheeselist.databinding.FragmentCheeseListBinding
import com.malec.cheeselist.di.CheeseListComponent
import com.malec.cheeselist.di.CheeseListComponentProvider
import com.malec.cheeselist.presentation.store.CheeseListState
import com.malec.cheeselist.presentation.viewcontroller.CheeseListViewController
import com.malec.domain.model.CheeseSort
import com.malec.injection.ComponentOwner
import com.malec.presentation.base.BaseFragment
import com.malec.presentation.dialog.CheeseDeleteDialog
import com.malec.presentation.dialog.DateTimePicker
import com.malec.presentation.navigation.BackButtonListener
import com.malec.presentation.unidirectional.BaseView
import javax.inject.Inject

class CheeseListFragment : BaseFragment<FragmentCheeseListBinding>(), BaseView<CheeseListState>,
    ComponentOwner<CheeseListComponent>, BackButtonListener {
    @Inject
    internal lateinit var viewController: CheeseListViewController

    private lateinit var adapter: CheeseAdapter
    private lateinit var cheeseTypeAdapter: ArrayAdapter<String>
    private lateinit var cheeseTypeSpinner: Spinner
    private lateinit var archivedSwitch: SwitchMaterial

    private var isMainMenu = true
    private var selectedCount = 0

    private val cheeseTypes = mutableListOf<String>()

    private var searchView: SearchView? = null

    private var toolbar: ActionBar? = null

    private var dateFilterStart: MenuItem? = null
    private var dateFilterEnd: MenuItem? = null
    private var cheeseTypeFilter: MenuItem? = null
    private var archivedFilter: MenuItem? = null
    private var dateSortStart: MenuItem? = null
    private var dateSortEnd: MenuItem? = null
    private var cheeseTypeSort: MenuItem? = null

    private val adapterDataObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            if (fromPosition == 0 || toPosition == 0) binding.cheeseRecycler.scrollToPosition(0)
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            if (positionStart == 0) binding.cheeseRecycler.scrollToPosition(0)
        }
    }

    private val searchListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?) = false

        override fun onQueryTextChange(newText: String?): Boolean {
            viewController.onSearch(newText ?: "")

            return false
        }
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        toolbar = (requireActivity() as AppCompatActivity).supportActionBar
        initRecycler()
        initFilterMenu()
        initNavigationListeners()

        binding.addFAB.setOnClickListener {
            viewController.onAddClick()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (isMainMenu)
            inflater.inflate(R.menu.menu_main, menu)
        else
            inflater.inflate(R.menu.menu_main_selected, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        searchView = menu.findItem(R.id.appBarSearch)?.actionView as SearchView?
        initSearchView()
    }

    private fun initSearchView() {
        searchView?.findViewById<View>(androidx.appcompat.R.id.search_plate)
            ?.setBackgroundResource(android.R.color.transparent)
        searchView?.queryHint = getString(R.string.search_hint)

        searchView?.setOnQueryTextListener(searchListener)
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
                viewController.archiveSelected()
                true
            }
            R.id.printButton -> {
                viewController.printSelected()
                true
            }
            R.id.deleteButton -> {
                CheeseDeleteDialog(requireContext()).setOnOkButtonClickListener {
                    viewController.deleteSelected()
                }.show(selectedCount)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun updateMenu() {
        requireActivity().invalidateOptionsMenu()
    }

    override fun renderState(state: CheeseListState) {
        adapter.submitList(state.cheeses)
        binding.swipeRefresh.isRefreshing = state.isLoading
        if (selectedCount != state.selectedCount) {
            selectedCount = state.selectedCount
            isMainMenu = selectedCount == 0
            updateMenu()
            toolbar?.setDisplayHomeAsUpEnabled(!isMainMenu)
            toolbar?.title = if (isMainMenu)
                getString(R.string.app_name)
            else
                getString(R.string.toolbar_n_selected, selectedCount)
        }
        cheeseTypeAdapter.clear()
        cheeseTypeAdapter.addAll(state.cheeseTypes)
        cheeseTypeAdapter.notifyDataSetChanged()
        dateFilterStart?.title = state.filter.dateStart?.let {
            "${getString(R.string.filter_date_start)}: $it"
        } ?: getString(R.string.filter_date_start)
        dateFilterEnd?.title = state.filter.dateEnd?.let {
            "${getString(R.string.filter_date_end)}: $it"
        } ?: getString(R.string.filter_date_end)
        archivedSwitch.isChecked = state.filter.archived ?: false
        when (state.filter.sortBy) {
            CheeseSort.DATE_START -> dateSortStart?.isChecked = true
            CheeseSort.DATE_END -> dateSortEnd?.isChecked = true
            CheeseSort.TYPE -> cheeseTypeSort?.isChecked = true
            else -> {
                dateSortStart?.isChecked = false
                dateSortEnd?.isChecked = false
                cheeseTypeSort?.isChecked = false
            }
        }
        cheeseTypeSpinner.setSelection(state.cheeseTypes.indexOfFirst {
            it == state.filter.type
        })
    }

    private fun initNavigationListeners() {
        binding.cheeseNavView.setNavigationItemSelectedListener { item ->
            if (item.itemId == R.id.filterClear) {
                viewController.clearFilter()
                return@setNavigationItemSelectedListener true
            }

            prepareFilterDialog(item.itemId)

            if (item.groupId == R.id.menuSortGroup)
                viewController.sortBy(
                    when (item.itemId) {
                        R.id.sortDateStart -> CheeseSort.DATE_START
                        R.id.sortDateEnd -> CheeseSort.DATE_END
                        R.id.sortCheeseType -> CheeseSort.TYPE
                        else -> CheeseSort.ID
                    }
                )
            true
        }

        archivedSwitch =
            (archivedFilter?.actionView as RelativeLayout).getChildAt(0) as SwitchMaterial
        archivedSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewController.filterArchived(isChecked)
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
                    viewController.filterCheeseType(
                        if (position == 0) null
                        else type
                    )
                }
            }
    }

    private fun prepareFilterDialog(itemId: Int) {
        when (itemId) {
            R.id.filterDateStart -> {
                DateTimePicker(
                    requireContext(),
                    requireActivity().supportFragmentManager
                ).pickDate {
                    dateFilterStart?.title = "${getString(R.string.filter_date_start)}: $it"
                    viewController.filterDateStart(it)
                }
            }
            R.id.filterDateEnd -> {
                DateTimePicker(
                    requireContext(),
                    requireActivity().supportFragmentManager
                ).pickDate {
                    dateFilterEnd?.title = "${getString(R.string.filter_date_end)}: $it"
                    viewController.filterDateEnd(it)
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
        adapter = CheeseAdapter(viewController)
        binding.cheeseRecycler.adapter = adapter
        binding.cheeseRecycler.layoutManager = LinearLayoutManager(context)

        adapter.registerAdapterDataObserver(adapterDataObserver)

        val helper = ItemTouchHelper(
            CheeseDeleteSwipeCallback(
                ::onCheeseSwipe,
                requireContext()
            )
        )
        helper.attachToRecyclerView(binding.cheeseRecycler)

        binding.swipeRefresh.setOnRefreshListener {
            viewController.onUpdateCheeses()
        }
    }

    private fun onCheeseSwipe(position: Int) {
        CheeseDeleteDialog(requireContext()).setOnOkButtonClickListener {
            viewController.onSwipe(adapter.currentList[position])
        }.setOnCancelButtonClickListener {
            adapter.notifyItemChanged(position)
        }.show()
    }

    override fun onBackPressed() {
        if (selectedCount == 0)
            viewController.onExitClick()
        else
            viewController.unselectAll()
    }

    override fun createViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        attachToParent: Boolean
    ) = FragmentCheeseListBinding.inflate(inflater, parent, attachToParent)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(viewController)
    }

    override fun provideComponent() = CheeseListComponentProvider.getInstance().component

    override fun inject(component: CheeseListComponent) {
        component.inject(this)
    }

    companion object {
        fun newInstance() = CheeseListFragment()
    }
}