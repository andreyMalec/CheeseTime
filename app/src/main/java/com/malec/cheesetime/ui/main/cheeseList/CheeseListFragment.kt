package com.malec.cheesetime.ui.main.cheeseList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.malec.cheesetime.R
import com.malec.cheesetime.di.Injectable
import kotlinx.android.synthetic.main.fragment_cheese_list.*
import javax.inject.Inject

class CheeseListFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: CheeseListViewModel by viewModels {
        viewModelFactory
    }

    private lateinit var adapter: CheeseAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = CheeseAdapter(viewModel)
        cheeseRecycler.adapter = adapter
        cheeseRecycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        viewModel.cheeseList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cheese_list, container, false)
    }
}