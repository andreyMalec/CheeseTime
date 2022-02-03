package com.malec.tasklist.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.malec.domain.model.Task
import com.malec.injection.ComponentOwner
import com.malec.presentation.DeleteSwipeCallback
import com.malec.presentation.base.BaseFragment
import com.malec.presentation.dialog.TaskDeleteDialog
import com.malec.presentation.navigation.BackButtonListener
import com.malec.presentation.unidirectional.BaseView
import com.malec.tasklist.databinding.FragmentTaskListBinding
import com.malec.tasklist.di.TaskListComponent
import com.malec.tasklist.di.TaskListComponentProvider
import com.malec.tasklist.presentation.store.TaskListState
import com.malec.tasklist.presentation.viewcontroller.TaskListViewController
import javax.inject.Inject

class TaskListFragment : BaseFragment<FragmentTaskListBinding>(), ComponentOwner<TaskListComponent>,
    BaseView<TaskListState>, BackButtonListener, TaskAdapter.TaskAction {
    @Inject
    internal lateinit var viewController: TaskListViewController

    private lateinit var adapter: TaskAdapter

    override fun onBackPressed() {
        viewController.onExitClick()
    }

    override fun renderState(state: TaskListState) {
        adapter.submitList(state.tasks)
        binding.swipeRefresh.isRefreshing = state.isLoading
        if (state.isNeedToUpdate)
            adapter.notifyItemRangeChanged(0, adapter.itemCount)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycler()

        binding.swipeRefresh.setOnRefreshListener {
            viewController.onUpdateTasks()
        }
        binding.addFAB.setOnClickListener {
            viewController.onAddClick()
        }
    }

    private fun initRecycler() {
        adapter = TaskAdapter(this)
        binding.taskRecycler.adapter = adapter
        binding.taskRecycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val helper = ItemTouchHelper(
            DeleteSwipeCallback(
                ::onTaskSwipe,
                requireContext()
            )
        )
        helper.attachToRecyclerView(binding.taskRecycler)
    }

    private fun onTaskSwipe(position: Int) {
        TaskDeleteDialog(requireContext()).setOnOkButtonClickListener {
            viewController.onSwipe(adapter.currentList[position])
        }.setOnCancelButtonClickListener {
            adapter.notifyItemChanged(position)
        }.show()
    }

    override fun createViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        attachToParent: Boolean
    ) = FragmentTaskListBinding.inflate(inflater)

    override fun provideComponent() = TaskListComponentProvider.getInstance().component

    override fun inject(component: TaskListComponent) {
        component.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(viewController)
    }

    override fun onClick(task: Task) {
        viewController.onTaskClick(task)
    }

    override fun onSwipe(task: Task) {
        viewController.onSwipe(task)
    }

    companion object {
        fun newInstance() = TaskListFragment()
    }
}