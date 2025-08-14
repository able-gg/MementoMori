package com.mementoapp.reminders.data.dao

import androidx.room.*
import com.mementoapp.reminders.data.model.ReminderLog
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface ReminderLogDao {
    
    @Query("SELECT * FROM reminder_logs ORDER BY sentAt DESC")
    fun getAllLogs(): Flow<List<ReminderLog>>
    
    @Query("SELECT * FROM reminder_logs WHERE sentAt >= :startDate ORDER BY sentAt DESC")
    fun getLogsAfter(startDate: LocalDateTime): Flow<List<ReminderLog>>
    
    @Query("SELECT * FROM reminder_logs ORDER BY sentAt DESC LIMIT 1")
    suspend fun getLastSentReminder(): ReminderLog?
    
    @Insert
    suspend fun insertLog(log: ReminderLog): Long
    
    @Update
    suspend fun updateLog(log: ReminderLog)
    
    @Query("DELETE FROM reminder_logs WHERE sentAt < :cutoffDate")
    suspend fun deleteOldLogs(cutoffDate: LocalDateTime)
}