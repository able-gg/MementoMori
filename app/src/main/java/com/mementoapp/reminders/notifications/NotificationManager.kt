package com.mementoapp.reminders.notifications

import android.app.NotificationChannel
import android.app.NotificationManager as AndroidNotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.mementoapp.reminders.MainActivity
import com.mementoapp.reminders.R
import com.mementoapp.reminders.data.repository.ReminderRepository

class NotificationManager(
    private val context: Context,
    private val reminderRepository: ReminderRepository
) {
    
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as AndroidNotificationManager
    
    init {
        createNotificationChannel()
    }
    
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Memento Reminders",
            AndroidNotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Reminders to reflect on time and mortality"
            enableVibration(true)
            setShowBadge(true)
        }
        notificationManager.createNotificationChannel(channel)
    }
    
    suspend fun sendRandomReminder() {
        val reminder = reminderRepository.getRandomReminder() ?: return
        val logId = reminderRepository.logReminderSent(reminder.id)
        
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("reminder_log_id", logId)
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context, 
            logId.toInt(), 
            intent, 
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val viewedIntent = Intent(context, NotificationReceiver::class.java).apply {
            action = NotificationReceiver.ACTION_REMINDER_VIEWED
            putExtra(NotificationReceiver.EXTRA_LOG_ID, logId)
        }
        
        val viewedPendingIntent = PendingIntent.getBroadcast(
            context,
            (logId + 10000).toInt(),
            viewedIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_hourglass)
            .setContentTitle("Memento Mori")
            .setContentText(reminder.question)
            .setStyle(NotificationCompat.BigTextStyle().bigText(reminder.question))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.ic_check, "Reflect", viewedPendingIntent)
            .build()
        
        notificationManager.notify(logId.toInt(), notification)
    }
    
    suspend fun markReminderViewed(logId: Long) {
        reminderRepository.markReminderViewed(logId)
        notificationManager.cancel(logId.toInt())
    }
    
    companion object {
        private const val CHANNEL_ID = "memento_reminders"
    }
}