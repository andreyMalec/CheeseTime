package com.malec.cheesetime.ui.main.task.taskManage

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.malec.cheesetime.R
import com.malec.cheesetime.databinding.ActivityTaskManageBinding
import com.malec.cheesetime.model.Task
import com.malec.cheesetime.ui.allertDialogBuilder.TaskDeleteDialog
import com.malec.cheesetime.ui.base.BaseActivity
import com.malec.cheesetime.ui.parseExtraIntent
import com.malec.cheesetime.util.DateTimePicker

class TaskManageActivity : BaseActivity() {
    private val viewModel: TaskManageViewModel by viewModels {
        viewModelFactory
    }

    private val cheeseList = mutableListOf<String>()
    private lateinit var adapter: ArrayAdapter<String>

    private var saveButton: MenuItem? = null
    private var deleteButton: MenuItem? = null
    private var isDeleteActive = false
    private var isSaveActive = false

    private lateinit var binding: ActivityTaskManageBinding

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_manage, menu)

        saveButton = menu?.findItem(R.id.saveButton)
        saveButton?.isVisible = isSaveActive

        deleteButton = menu?.findItem(R.id.deleteButton)
        deleteButton?.isVisible = isDeleteActive

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.saveButton -> viewModel.checkAndManageTask()
            R.id.deleteButton ->
                TaskDeleteDialog(this).setOnOkButtonClickListener {
                    viewModel.deleteTask()
                }.show()
            android.R.id.home -> onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }

    override val navigator: AppNavigator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskManageBinding.inflate(layoutInflater)
        setContentView(binding.getRoot())

        val task = parseExtraIntent<Task>(intent)
        viewModel.setTask(task)
        binding.task = viewModel.task.value

        initViewModelListeners()
        initInputListeners()
        initToolbar()

        adapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, cheeseList)
        binding.cheeseSpinner.adapter = adapter
    }

    private fun initViewModelListeners() {
        viewModel.cheeseList.observe(this, {
            adapter.clear()
            adapter.addAll(it)
        })

        viewModel.cheesePosition.observe(this, {
            binding.cheeseSpinner.setSelection(it)
        })

        viewModel.date.observe(this, {
            binding.dateText.text = it
        })

        viewModel.time.observe(this, {
            binding.timeText.text = it
        })

        viewModel.isSaveActive.observe(this, { active ->
            saveButton?.isVisible = active
            isSaveActive = active
        })

        viewModel.isDeleteActive.observe(this, { active ->
            deleteButton?.isVisible = active
            isDeleteActive = active
        })

        viewModel.manageError.observe(this, { message ->
            if (!message.isNullOrBlank()) {
                showError(message)
                viewModel.manageError.value = null
            }
        })

        viewModel.manageResult.observe(this, { message ->
            if (!message.isNullOrBlank()) {
                showMessage(message)
                viewModel.manageResult.value = null
                onBackPressed()
            }
        })
    }

    private fun initInputListeners() {
        binding.cheeseSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(a: AdapterView<*>?, v: View?, p: Int, p2: Long) {
                viewModel.selectCheeseAt(p)
            }
        }
        binding.todoEditText.doAfterTextChanged {
            viewModel.checkCanSave()
        }
        binding.dateButton.setOnClickListener {
            DateTimePicker(this).pickDate {
                viewModel.date.value = it
                viewModel.checkCanSave()
            }
        }
        binding.timeButton.setOnClickListener {
            DateTimePicker(this).pickTime {
                viewModel.time.value = it
                viewModel.checkCanSave()
            }
        }
    }

    private fun initToolbar() {
        binding.toolbar.setTitle(R.string.toolbar_manage_task)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}