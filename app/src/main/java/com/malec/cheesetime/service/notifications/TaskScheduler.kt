package com.malec.cheesetime.service.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.malec.cheesetime.model.Task
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*

@ExperimentalCoroutinesApi
class TaskScheduler(private val context: Context) {
    fun schedule(task: Task) {
        if (task.date - Date().time <= 0)
            return

        val title = "${task.todo} (${task.cheeseName} id: ${task.cheeseId})"
        val notificationId = task.id.toInt() * 10

        val notificationIntent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra(NotificationReceiver.NOTIFICATION_ID, notificationId)
            putExtra(NotificationReceiver.NOTIFICATION_TITLE, title)
            putExtra(NotificationReceiver.NOTIFICATION_MESSAGE, task.comment)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId,
            notificationIntent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )

        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, task.date, pendingIntent)
    }
}