package com.malec.cheesetime.ui.main.task.taskManage

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import com.malec.cheesetime.R
import com.malec.cheesetime.model.Task
import com.malec.cheesetime.ui.BaseActivity
import com.malec.cheesetime.ui.Screens
import com.malec.cheesetime.ui.main.DeleteDialog
import com.malec.cheesetime.util.DateFormatter
import com.malec.cheesetime.util.DateTimePicker
import kotlinx.android.synthetic.main.activity_task_manage.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class TaskManageActivity : BaseActivity() {
    private val viewModel: TaskManageViewModel by viewModels {
        viewModelFactory
    }

    private val cheeseList = mutableListOf<String>()
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_manage, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.saveButton -> viewModel.checkAndManageTask()
            R.id.deleteButton ->
                DeleteDialog(this).setOnOkButtonClickListener {
                    viewModel.deleteTask()
                }.showTaskDialog()
            android.R.id.home -> onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_manage)

        val task = Screens.TaskManageScreen.parseExtraIntent(intent)
        viewModel.setTask(task)

        initViewModelListeners()
        initInputListeners()
        initToolbar()

        adapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, cheeseList)
        cheeseSpinner.adapter = adapter
    }

    private fun initViewModelListeners() {
        viewModel.cheeseList.observe(this, Observer { list ->
            adapter.clear()
            val cheeseNames = list.map {
                it.name + " id: " + it.id
            }
            adapter.addAll(cheeseNames)
        })

        viewModel.task.observe(this, Observer {
            if (it != null)
                showTaskData(it)
        })

        viewModel.isFieldsEmptyError.observe(this, Observer { isError ->
            if (isError) {
                showError(getString(R.string.empty_fields_error))
                viewModel.isFieldsEmptyError.value = false

                if (todoEditText.text.isNullOrBlank())
                    todoLayout.error = getString(R.string.required_field_error)
                if (dateText.text.isNullOrBlank())
                    dateButton.error = getString(R.string.required_field_error)
                if (timeText.text.isNullOrBlank())
                    timeButton.error = getString(R.string.required_field_error)
            }
        })

        viewModel.manageError.observe(this, Observer { message ->
            if (!message.isNullOrBlank()) {
                showError(message)
                viewModel.manageError.value = null
            }
        })

        viewModel.manageResult.observe(this, Observer { message ->
            if (!message.isNullOrBlank()) {
                showMessage(message)
                viewModel.manageResult.value = null
                onBackPressed()
            }
        })
    }

    private fun showTaskData(task: Task) {
        val name = task.cheeseName + " id: " + task.cheeseId
        val index = cheeseList.indexOf(name)
        if (index == -1)
            adapter.add(name)
        adapter.notifyDataSetChanged()
        cheeseSpinner.setSelection(
            if (index == -1) cheeseList.size - 1
            else index
        )

        todoEditText.setText(task.todo)
        dateText.text = DateFormatter.simpleFormat(task.date)
        timeText.text = DateFormatter.simpleFormatTime(task.date)
        commentEditText.setText(task.comment)
    }

    private fun initInputListeners() {
        cheeseSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(adapter: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                viewModel.cheese.value = adapter?.getItemAtPosition(p2)?.toString()
            }
        }
        todoEditText.doAfterTextChanged {
            viewModel.todo.value = it?.toString()?.trim()
        }
        dateText.doAfterTextChanged {
            viewModel.date.value = it?.toString()?.trim()
        }
        dateButton.setOnClickListener {
            dateButton.error = null
            DateTimePicker(this).pickDate {
                dateText.text = it
            }
        }
        timeText.doAfterTextChanged {
            viewModel.time.value = it?.toString()?.trim()
        }
        timeButton.setOnClickListener {
            timeButton.error = null
            DateTimePicker(this).pickTime {
                timeText.text = it
            }
        }
        commentEditText.doAfterTextChanged {
            viewModel.comment.value = it?.toString()?.trim()
        }
    }

    private fun initToolbar() {
        toolbar.setTitle(R.string.toolbar_manage_task)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}