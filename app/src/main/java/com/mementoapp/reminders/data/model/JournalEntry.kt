package com.mementoapp.reminders.data.model

import java.time.LocalDate
import java.time.LocalDateTime

data class JournalEntry(
    val id: String = "",
    val date: LocalDate,
    val highlight: String,
    val thoughts: String,
    val createdAt: LocalDateTime = LocalDateTime.now()
)