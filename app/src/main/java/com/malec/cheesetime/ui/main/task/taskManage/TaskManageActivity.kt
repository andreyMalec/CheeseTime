package com.malec.cheesetime.ui.main.task.taskManage

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.malec.cheesetime.R
import com.malec.cheesetime.util.DateTimePicker
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.android.synthetic.main.activity_task_manage.*
import javax.inject.Inject

class TaskManageActivity : AppCompatActivity(), HasAndroidInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: TaskManageViewModel by viewModels {
        viewModelFactory
    }

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_manage)

        dateButton.setOnClickListener {
            DateTimePicker(this).pickDate {
                dateText.text = it
            }
        }

        timeButton.setOnClickListener {
            DateTimePicker(this).pickTime {
                timeText.text = it
            }
        }
    }
}