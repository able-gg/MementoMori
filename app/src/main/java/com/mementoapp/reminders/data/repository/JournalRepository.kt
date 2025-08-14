package com.mementoapp.reminders.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.mementoapp.reminders.data.model.JournalEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
// Simplified without kotlinx.serialization
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

private val Context.journalDataStore: DataStore<Preferences> by preferencesDataStore(name = "journal_entries")

class JournalRepository(private val context: Context) {
    
    suspend fun saveJournalEntry(entry: JournalEntry) {
        val keyHighlight = stringPreferencesKey("journal_${entry.date}_highlight")
        val keyThoughts = stringPreferencesKey("journal_${entry.date}_thoughts")
        val keyCreated = stringPreferencesKey("journal_${entry.date}_created")
        
        context.journalDataStore.edit { preferences ->
            preferences[keyHighlight] = entry.highlight
            preferences[keyThoughts] = entry.thoughts
            preferences[keyCreated] = entry.createdAt.toString()
        }
    }
    
    fun getJournalEntry(date: LocalDate): Flow<JournalEntry?> {
        val keyHighlight = stringPreferencesKey("journal_${date}_highlight")
        val keyThoughts = stringPreferencesKey("journal_${date}_thoughts")
        val keyCreated = stringPreferencesKey("journal_${date}_created")
        
        return context.journalDataStore.data.map { preferences ->
            val highlight = preferences[keyHighlight]
            val thoughts = preferences[keyThoughts]
            val created = preferences[keyCreated]
            
            if (highlight != null || thoughts != null) {
                JournalEntry(
                    id = UUID.randomUUID().toString(),
                    date = date,
                    highlight = highlight ?: "",
                    thoughts = thoughts ?: "",
                    createdAt = created?.let { LocalDateTime.parse(it) } ?: LocalDateTime.now()
                )
            } else null
        }
    }
    
    fun getAllJournalEntries(): Flow<List<JournalEntry>> {
        return context.journalDataStore.data.map { preferences ->
            val entries = mutableListOf<JournalEntry>()
            val processedDates = mutableSetOf<String>()
            
            preferences.asMap().forEach { (key, value) ->
                if (key.name.startsWith("journal_") && key.name.endsWith("_highlight") && value is String) {
                    val dateStr = key.name.removePrefix("journal_").removeSuffix("_highlight")
                    if (!processedDates.contains(dateStr)) {
                        processedDates.add(dateStr)
                        try {
                            val date = LocalDate.parse(dateStr)
                            val highlight = value
                            val thoughts = preferences[stringPreferencesKey("journal_${dateStr}_thoughts")] ?: ""
                            val created = preferences[stringPreferencesKey("journal_${dateStr}_created")]
                            
                            entries.add(
                                JournalEntry(
                                    id = UUID.randomUUID().toString(),
                                    date = date,
                                    highlight = highlight,
                                    thoughts = thoughts,
                                    createdAt = created?.let { LocalDateTime.parse(it) } ?: LocalDateTime.now()
                                )
                            )
                        } catch (e: Exception) {
                            // Skip invalid entries
                        }
                    }
                }
            }
            entries.sortedByDescending { it.date }
        }
    }
}

// Removed serialization data class