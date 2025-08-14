package com.mementoapp.reminders.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminders")
data class Reminder(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val question: String,
    val category: ReminderCategory,
    val isActive: Boolean = true
)

enum class ReminderCategory {
    TIME_REFLECTION,
    PRODUCTIVITY,
    MORTALITY_AWARENESS,
    LIFE_PURPOSE,
    GRATITUDE
}