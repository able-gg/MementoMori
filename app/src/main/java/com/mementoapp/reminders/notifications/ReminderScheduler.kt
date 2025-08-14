package com.mementoapp.reminders.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.mementoapp.reminders.data.preferences.UserPreferences
import kotlinx.coroutines.flow.first
import java.util.*
import kotlin.random.Random

class ReminderScheduler(
    private val context: Context,
    private val userPreferences: UserPreferences
) {
    
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    
    suspend fun scheduleNextReminder() {
        val settings = userPreferences.getReminderSettings().first()
        if (!settings.isEnabled) return
        
        val nextReminderTime = calculateNextReminderTime(settings)
        
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            action = NotificationReceiver.ACTION_SEND_REMINDER
        }
        
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REMINDER_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            nextReminderTime,
            pendingIntent
        )
    }
    
    private fun calculateNextReminderTime(settings: ReminderSettings): Long {
        val calendar = Calendar.getInstance()
        
        // Random chance to skip today (10-30% chance based on user preference)
        val skipChance = Random.nextFloat()
        if (skipChance < settings.skipDayProbability) {
            // Skip to tomorrow
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        
        // Random time within active hours
        val startHour = settings.startHour
        val endHour = settings.endHour
        val randomHour = Random.nextInt(startHour, endHour + 1)
        val randomMinute = Random.nextInt(0, 60)
        
        calendar.set(Calendar.HOUR_OF_DAY, randomHour)
        calendar.set(Calendar.MINUTE, randomMinute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        
        // If the time has already passed today, move to tomorrow
        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        
        return calendar.timeInMillis
    }
    
    fun cancelAllReminders() {
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REMINDER_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        alarmManager.cancel(pendingIntent)
    }
    
    companion object {
        private const val REMINDER_REQUEST_CODE = 1001
    }
}

data class ReminderSettings(
    val isEnabled: Boolean = true,
    val startHour: Int = 9,
    val endHour: Int = 21,
    val skipDayProbability: Float = 0.2f // 20% chance to skip a day
)