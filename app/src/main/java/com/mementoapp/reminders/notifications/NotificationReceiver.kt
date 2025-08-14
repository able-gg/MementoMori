package com.mementoapp.reminders.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationReceiver : BroadcastReceiver() {
    
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            ACTION_SEND_REMINDER -> {
                val notificationManager = NotificationManager(context)
                notificationManager.sendRandomReminder()
            }
            ACTION_SEND_JOURNAL_PROMPT -> {
                val notificationManager = NotificationManager(context)
                notificationManager.sendJournalPrompt()
            }
            ACTION_REMINDER_VIEWED -> {
                // Handle reminder viewed
            }
        }
    }
    
    companion object {
        const val ACTION_SEND_REMINDER = "com.mementoapp.reminders.SEND_REMINDER"
        const val ACTION_SEND_JOURNAL_PROMPT = "com.mementoapp.reminders.SEND_JOURNAL_PROMPT"
        const val ACTION_REMINDER_VIEWED = "com.mementoapp.reminders.REMINDER_VIEWED"
        const val EXTRA_LOG_ID = "log_id"
    }
}