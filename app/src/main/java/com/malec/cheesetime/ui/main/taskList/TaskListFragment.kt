package com.malec.cheesetime.ui.main.taskList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.malec.cheesetime.R
import com.malec.cheesetime.di.Injectable
import javax.inject.Inject

class TaskListFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: TaskListViewModel by viewModels {
        viewModelFactory
    }

//    private lateinit var adapter: TaskAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        adapter = TaskAdapter(viewModel)
//        taskRecycler.adapter = adapter
//        taskRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//
//        viewModel.taskList.observe(viewLifecycleOwner, Observer {
//            adapter.submitList(it)
//        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_task_list, container, false)
    }
}