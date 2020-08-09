package com.malec.cheesetime.service.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.malec.cheesetime.R
import com.malec.cheesetime.ui.login.LoginActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class NotificationReceiver : BroadcastReceiver() {
    private lateinit var channelId: String

    companion object {
        const val NOTIFICATION_ID = "notification_id"
        const val NOTIFICATION_TITLE = "notification_title"
        const val NOTIFICATION_MESSAGE = "notification_message"
    }

    override fun onReceive(context: Context, intent: Intent) {
        channelId = context.getString(R.string.app_name)

        val notificationIntent = Intent(context, LoginActivity::class.java)
        val contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0)

        val notificationTitle = intent.getStringExtra(NOTIFICATION_TITLE) ?: ""
        val notificationMessage = intent.getStringExtra(NOTIFICATION_MESSAGE) ?: ""
        val notificationId = intent.getIntExtra(NOTIFICATION_ID, 1)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createChannel(notificationManager)

        val builder = NotificationCompat.Builder(context, channelId).apply {
            setContentIntent(contentIntent)
            setSmallIcon(R.drawable.icon)
            setContentTitle(notificationTitle)
            setContentText(notificationMessage)
            setVibrate(longArrayOf(500))
        }.build()

        notificationManager.notify(notificationId, builder)
    }

    private fun createChannel(notificationManager: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    channelId,
                    channelId,
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = channelId
                    enableVibration(true)
                    setShowBadge(true)
                }
            )
        }
    }
}