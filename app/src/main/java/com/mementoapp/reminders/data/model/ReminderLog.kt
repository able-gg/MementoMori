package com.mementoapp.reminders.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "reminder_logs")
data class ReminderLog(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val reminderId: Long,
    val sentAt: LocalDateTime,
    val wasViewed: Boolean = false,
    val viewedAt: LocalDateTime? = null,
    val userResponse: String? = null
)