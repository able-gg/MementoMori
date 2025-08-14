package com.mementoapp.reminders.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mementoapp.reminders.data.model.JournalEntry
import com.mementoapp.reminders.data.repository.JournalRepository
import com.mementoapp.reminders.notifications.NotificationManager
import com.mementoapp.reminders.notifications.ReminderScheduler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate

class MainViewModel(
    private val journalRepository: JournalRepository,
    private val notificationManager: NotificationManager,
    private val reminderScheduler: ReminderScheduler
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()
    
    val journalEntries = journalRepository.getAllJournalEntries()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    fun saveJournalEntry(entry: JournalEntry) {
        viewModelScope.launch {
            journalRepository.saveJournalEntry(entry)
        }
    }
    
    fun getJournalEntry(date: LocalDate): Flow<JournalEntry?> {
        return journalRepository.getJournalEntry(date)
    }
    
    fun sendTestNotification() {
        notificationManager.sendTestNotification()
    }
    
    fun scheduleReminders() {
        reminderScheduler.scheduleNextReminder()
    }
}

data class MainUiState(
    val isLoading: Boolean = false,
    val error: String? = null
)