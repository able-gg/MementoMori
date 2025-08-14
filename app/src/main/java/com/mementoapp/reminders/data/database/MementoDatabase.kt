package com.mementoapp.reminders.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.mementoapp.reminders.data.dao.ReminderDao
import com.mementoapp.reminders.data.dao.ReminderLogDao
import com.mementoapp.reminders.data.model.Reminder
import com.mementoapp.reminders.data.model.ReminderLog

@Database(
    entities = [Reminder::class, ReminderLog::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MementoDatabase : RoomDatabase() {
    
    abstract fun reminderDao(): ReminderDao
    abstract fun reminderLogDao(): ReminderLogDao
    
    companion object {
        const val DATABASE_NAME = "memento_database"
    }
}