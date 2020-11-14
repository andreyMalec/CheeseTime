package com.malec.cheesetime.ui.main.task.taskList

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.malec.cheesetime.R
import com.malec.cheesetime.di.Injectable
import com.malec.cheesetime.ui.allertDialogBuilder.TaskDeleteDialog
import kotlinx.android.synthetic.main.fragment_task_list.*
import javax.inject.Inject

class TaskListFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: TaskListViewModel by viewModels {
        viewModelFactory
    }

    private lateinit var adapter: TaskAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycler()
        viewModel.autoRepeat(::updateTime)

        viewModel.taskList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
            swipeRefresh.isRefreshing = false
        })

        swipeRefresh.setOnRefreshListener {
            viewModel.update()
        }
    }

    private fun updateTime() {
        adapter.notifyDataSetChanged()
    }

    private fun initRecycler() {
        adapter = TaskAdapter(viewModel)
        taskRecycler.adapter = adapter
        taskRecycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val helper = ItemTouchHelper(
            TaskSwipeCallback(
                ::onTaskSwipe,
                requireContext()
            )
        )
        helper.attachToRecyclerView(taskRecycler)
    }

    private fun onTaskSwipe(position: Int) {
        TaskDeleteDialog(requireContext()).setOnOkButtonClickListener {
            viewModel.onSwipe(adapter.currentList[position])
        }.setOnCancelButtonClickListener {
            Handler(Looper.getMainLooper()).postDelayed({
                adapter.notifyItemChanged(position)
            }, 400)
        }.show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_task_list, container, false)
    }

    override fun onResume() {
        super.onResume()
        viewModel.update()
    }
}