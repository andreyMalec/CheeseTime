package com.malec.cheesetime.service.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.malec.cheesetime.R
import com.malec.cheesetime.model.Task
import com.malec.cheesetime.util.DateFormatter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*

@ExperimentalCoroutinesApi
class TaskScheduler(private val context: Context) {
    fun schedule(task: Task) {
        if (task.date - Date().time <= 0)
            return

        val title = "${task.todo} (${task.cheeseName} id: ${task.cheeseId})"
        val title30Min =
            "(${context.getString(R.string.date_in_30_min)}) ${task.todo} (${task.cheeseName} id: ${task.cheeseId})"
        val notificationId = task.id.toInt() * 10
        val notificationId30Min = notificationId + 1

        val notificationIntent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra(NotificationReceiver.NOTIFICATION_ID, notificationId)
            putExtra(NotificationReceiver.NOTIFICATION_TITLE, title)
            putExtra(NotificationReceiver.NOTIFICATION_MESSAGE, task.comment)
        }
        val notificationIntent30Min = Intent(context, NotificationReceiver::class.java).apply {
            putExtra(NotificationReceiver.NOTIFICATION_ID, notificationId30Min)
            putExtra(NotificationReceiver.NOTIFICATION_TITLE, title30Min)
            putExtra(NotificationReceiver.NOTIFICATION_MESSAGE, task.comment)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId,
            notificationIntent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )
        val pendingIntent30Min = PendingIntent.getBroadcast(
            context,
            notificationId30Min,
            notificationIntent30Min,
            PendingIntent.FLAG_CANCEL_CURRENT
        )

        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val date30Min = task.date - DateFormatter.millisecondsIn30Min

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, task.date, pendingIntent)

        if (date30Min - Date().time > DateFormatter.millisecondsIn30Min)
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                date30Min,
                pendingIntent30Min
            )
    }
}