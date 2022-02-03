package com.malec.taskdetail.presentation.view

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.bekawestberg.loopinglayout.library.LoopingLayoutManager
import com.bekawestberg.loopinglayout.library.LoopingSnapHelper
import com.malec.injection.ComponentOwner
import com.malec.presentation.base.BaseFragment
import com.malec.presentation.dialog.DateTimePicker
import com.malec.presentation.dialog.TaskDeleteDialog
import com.malec.presentation.initToolbar
import com.malec.presentation.navigation.BackButtonListener
import com.malec.presentation.unidirectional.BaseView
import com.malec.presentation.update
import com.malec.taskdetail.R
import com.malec.taskdetail.databinding.FragmentTaskDetailBinding
import com.malec.taskdetail.di.TaskDetailComponent
import com.malec.taskdetail.di.TaskDetailComponentProvider
import com.malec.taskdetail.presentation.store.TaskDetailState
import com.malec.taskdetail.presentation.viewcontroller.TaskDetailViewController
import javax.inject.Inject

class TaskDetailFragment : BaseFragment<FragmentTaskDetailBinding>(),
    ComponentOwner<TaskDetailComponent>, BaseView<TaskDetailState>, BackButtonListener {
    @Inject
    internal lateinit var viewController: TaskDetailViewController

    private lateinit var adapter: ArrayAdapter<String>

    private var saveButton: MenuItem? = null
    private var deleteButton: MenuItem? = null

    private var blockFirstLaunchScroll = true

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_manage, menu)

        saveButton = menu.findItem(R.id.saveButton)
        deleteButton = menu.findItem(R.id.deleteButton)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.saveButton -> {
                viewController.saveTask()
            }
            R.id.deleteButton ->
                TaskDeleteDialog(requireContext()).setOnOkButtonClickListener {
                    viewController.deleteTask()
                }.show()
            android.R.id.home -> onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getLong(TASK_ID)?.let {
            viewController.loadTask(it)
        }

        initTimeOffsetScroller()
        initInputListeners()
        initToolbar()

        adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            android.R.id.text1,
            mutableListOf<String>()
        )
        binding.cheeseSpinner.adapter = adapter
    }

    override fun renderState(state: TaskDetailState) {
        binding.task = state.task
        adapter.update(state.cheeses)
        binding.cheeseSpinner.setSelection(state.selectedCheesePosition)
        saveButton?.isVisible = state.isSaveActive
        deleteButton?.isVisible = state.isDeleteActive
        binding.timeText.text = state.time
        binding.dateText.text = state.date
    }

    private fun initTimeOffsetScroller() {
        val snapHelper = LoopingSnapHelper()
        val lm = LoopingLayoutManager(requireContext(), LoopingLayoutManager.VERTICAL, false)
        val list = resources.getStringArray(R.array.task_date_preset).toList()
        binding.dateTimeRecycler.apply {
            setHasFixedSize(true)
            adapter = DateTimeAdapter(list)
            layoutManager = lm
            snapHelper.attachToRecyclerView(this)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (blockFirstLaunchScroll) {
                        blockFirstLaunchScroll = false
                        return
                    }
                    val v = snapHelper.findSnapView(lm) as TextView
                    viewController.selectTimeAtPosition(list.indexOf(v.text.toString()))
                }
            })
        }
    }

    private fun initInputListeners() {
        binding.cheeseSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(a: AdapterView<*>?, v: View?, p: Int, p2: Long) {
                viewController.selectCheeseAtPosition(p)
            }
        }

        binding.todoEditText.doAfterTextChanged {
            viewController.setTodo(it.toString())
        }
        binding.commentEditText.doAfterTextChanged {
            viewController.setComment(it.toString())
        }
        binding.dateButton.setOnClickListener {
            DateTimePicker(requireContext(), requireActivity().supportFragmentManager).pickDate {
                viewController.setDate(it)
            }
        }
        binding.timeButton.setOnClickListener {
            DateTimePicker(requireContext(), requireActivity().supportFragmentManager).pickTime {
                viewController.setTime(it)
            }
        }
    }

    private fun initToolbar() {
        setHasOptionsMenu(true)
        val activity = (requireActivity() as AppCompatActivity)
        activity.initToolbar(binding.root, binding.toolbar, R.string.toolbar_edit_task)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun createViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        attachToParent: Boolean
    ) = FragmentTaskDetailBinding.inflate(inflater)

    override fun provideComponent() = TaskDetailComponentProvider.getInstance().component

    override fun inject(component: TaskDetailComponent) {
        component.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(viewController)
    }

    override fun onBackPressed() {
        viewController.onBack()
    }

    companion object {
        private const val TASK_ID = "TaskId"

        fun newInstance(taskId: Long?): TaskDetailFragment {
            return TaskDetailFragment().apply {
                arguments = Bundle().apply {
                    if (taskId != null)
                        putLong(TASK_ID, taskId)
                }
            }
        }
    }
}