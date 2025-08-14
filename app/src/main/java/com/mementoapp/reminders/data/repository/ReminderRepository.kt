package com.mementoapp.reminders.data.repository

import com.mementoapp.reminders.data.dao.ReminderDao
import com.mementoapp.reminders.data.dao.ReminderLogDao
import com.mementoapp.reminders.data.model.Reminder
import com.mementoapp.reminders.data.model.ReminderCategory
import com.mementoapp.reminders.data.model.ReminderLog
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

class ReminderRepository(
    private val reminderDao: ReminderDao,
    private val reminderLogDao: ReminderLogDao
) {
    
    fun getActiveReminders(): Flow<List<Reminder>> = reminderDao.getActiveReminders()
    
    suspend fun getRandomReminder(): Reminder? = reminderDao.getRandomReminder()
    
    suspend fun getRemindersByCategory(category: ReminderCategory): List<Reminder> =
        reminderDao.getRemindersByCategory(category)
    
    suspend fun insertReminder(reminder: Reminder): Long = reminderDao.insertReminder(reminder)
    
    suspend fun insertReminders(reminders: List<Reminder>) = reminderDao.insertReminders(reminders)
    
    suspend fun updateReminder(reminder: Reminder) = reminderDao.updateReminder(reminder)
    
    suspend fun deleteReminder(reminder: Reminder) = reminderDao.deleteReminder(reminder)
    
    suspend fun getActiveReminderCount(): Int = reminderDao.getActiveReminderCount()
    
    // Reminder Log methods
    fun getAllLogs(): Flow<List<ReminderLog>> = reminderLogDao.getAllLogs()
    
    fun getLogsAfter(startDate: LocalDateTime): Flow<List<ReminderLog>> =
        reminderLogDao.getLogsAfter(startDate)
    
    suspend fun getLastSentReminder(): ReminderLog? = reminderLogDao.getLastSentReminder()
    
    suspend fun logReminderSent(reminderId: Long): Long {
        val log = ReminderLog(
            reminderId = reminderId,
            sentAt = LocalDateTime.now()
        )
        return reminderLogDao.insertLog(log)
    }
    
    suspend fun markReminderViewed(logId: Long, response: String? = null) {
        // Get all logs and find the one with matching ID
        val logs = reminderLogDao.getAllLogs()
        logs.collect { logList ->
            val log = logList.find { it.id == logId }
            log?.let {
                val updatedLog = it.copy(
                    wasViewed = true,
                    viewedAt = LocalDateTime.now(),
                    userResponse = response
                )
                reminderLogDao.updateLog(updatedLog)
            }
        }
    }
    
    suspend fun cleanupOldLogs(daysToKeep: Int = 30) {
        val cutoffDate = LocalDateTime.now().minusDays(daysToKeep.toLong())
        reminderLogDao.deleteOldLogs(cutoffDate)
    }
}