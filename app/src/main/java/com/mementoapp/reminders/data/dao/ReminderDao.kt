package com.mementoapp.reminders.data.dao

import androidx.room.*
import com.mementoapp.reminders.data.model.Reminder
import com.mementoapp.reminders.data.model.ReminderCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {
    
    @Query("SELECT * FROM reminders WHERE isActive = 1")
    fun getActiveReminders(): Flow<List<Reminder>>
    
    @Query("SELECT * FROM reminders WHERE category = :category AND isActive = 1")
    suspend fun getRemindersByCategory(category: ReminderCategory): List<Reminder>
    
    @Query("SELECT * FROM reminders ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomReminder(): Reminder?
    
    @Insert
    suspend fun insertReminder(reminder: Reminder): Long
    
    @Insert
    suspend fun insertReminders(reminders: List<Reminder>)
    
    @Update
    suspend fun updateReminder(reminder: Reminder)
    
    @Delete
    suspend fun deleteReminder(reminder: Reminder)
    
    @Query("SELECT COUNT(*) FROM reminders WHERE isActive = 1")
    suspend fun getActiveReminderCount(): Int
}