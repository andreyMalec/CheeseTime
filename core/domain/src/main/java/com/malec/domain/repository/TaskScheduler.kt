package com.malec.domain.repository

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.malec.domain.R
import com.malec.domain.model.Task
import com.malec.domain.util.DateFormatter

class TaskScheduler(private val context: Context) {
    private val alarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun schedule(task: Task) {
        if (task.date - System.currentTimeMillis() <= 0)
            return

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            task.date,
            createPendingIntent(task)
        )

        val date30Min = task.date - DateFormatter.millisecondsIn30Min
        if (date30Min - System.currentTimeMillis() > DateFormatter.millisecondsInHour * 2)
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                date30Min,
                createPendingIntent30Min(task)
            )
    }

    fun cancel(taskId: Long) {
        val notificationId = taskId.toInt() * 10
        alarmManager.cancel(createPendingIntent(notificationId, "", ""))
        alarmManager.cancel(createPendingIntent(notificationId + 1, "", ""))
    }

    private fun createPendingIntent30Min(task: Task): PendingIntent {
        val title30Min =
            "(${context.getString(R.string.date_in_30_min)}) ${task.todo} (${task.cheeseName} id: ${task.cheeseId})"
        val notificationId30Min = task.id.toInt() * 10 + 1

        return createPendingIntent(notificationId30Min, title30Min, task.comment)
    }

    private fun createPendingIntent(task: Task): PendingIntent {
        val title = "${task.todo} (${task.cheeseName} id: ${task.cheeseId})"
        val notificationId = task.id.toInt() * 10

        return createPendingIntent(notificationId, title, task.comment)
    }

    private fun createPendingIntent(
        notificationId: Int,
        title: String,
        comment: String
    ): PendingIntent {
//        val notificationIntent = Intent(context, NotificationReceiver::class.java).apply {
//            putExtra(NotificationReceiver.NOTIFICATION_ID, notificationId)
//            putExtra(NotificationReceiver.NOTIFICATION_TITLE, title)
//            putExtra(NotificationReceiver.NOTIFICATION_MESSAGE, comment)
//        }

        return PendingIntent.getBroadcast(
            context,
            notificationId,
            Intent(),
            PendingIntent.FLAG_CANCEL_CURRENT
        )
    }
}