package com.mementoapp.reminders.notifications

import android.app.NotificationChannel
import android.app.NotificationManager as AndroidNotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.mementoapp.reminders.MainActivity
import com.mementoapp.reminders.R
class NotificationManager(
    private val context: Context
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
    
    fun sendRandomReminder() {
        // Sample reminder questions
        val questions = listOf(
            "Are you proud of how you spent the last hour?",
            "If today were your last day, would you be satisfied with how you've used it?",
            "What's the most important thing you could be doing right now?",
            "Remember: you have a limited number of days left. How will you use today?",
            "What are you grateful for in this moment?"
        )
        
        val randomQuestion = questions.random()
        sendNotification("Memento Mori", randomQuestion, "reminder")
    }
    
    fun sendJournalPrompt() {
        val journalPrompts = listOf(
            "Time to reflect on your day. What was your highlight?",
            "How did you grow today? Take a moment to journal.",
            "Before the day ends, capture your thoughts and feelings.",
            "What are you grateful for today? Write it down.",
            "Reflect on your day - what would you do differently?"
        )
        
        val randomPrompt = journalPrompts.random()
        sendNotification("Daily Reflection", randomPrompt, "journal")
    }
    
    fun sendTestNotification() {
        sendNotification("Test Notification", "This is a test to check if notifications are working!", "test")
    }
    
    private fun sendNotification(title: String, message: String, type: String) {
        val notificationId = System.currentTimeMillis().toInt()
        
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("notification_type", type)
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context, 
            notificationId, 
            intent, 
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_hourglass)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()
        
        notificationManager.notify(notificationId, notification)
    }
    
    companion object {
        private const val CHANNEL_ID = "memento_reminders"
    }
}