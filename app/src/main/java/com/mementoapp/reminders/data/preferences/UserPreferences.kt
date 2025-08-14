package com.mementoapp.reminders.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.mementoapp.reminders.notifications.ReminderSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferences(private val context: Context) {
    
    private object PreferencesKeys {
        val REMINDERS_ENABLED = booleanPreferencesKey("reminders_enabled")
        val START_HOUR = intPreferencesKey("start_hour")
        val END_HOUR = intPreferencesKey("end_hour")
        val SKIP_DAY_PROBABILITY = floatPreferencesKey("skip_day_probability")
        val FIRST_LAUNCH = booleanPreferencesKey("first_launch")
    }
    
    fun getReminderSettings(): Flow<ReminderSettings> {
        return context.dataStore.data.map { preferences ->
            ReminderSettings(
                isEnabled = preferences[PreferencesKeys.REMINDERS_ENABLED] ?: true,
                startHour = preferences[PreferencesKeys.START_HOUR] ?: 9,
                endHour = preferences[PreferencesKeys.END_HOUR] ?: 21,
                skipDayProbability = preferences[PreferencesKeys.SKIP_DAY_PROBABILITY] ?: 0.2f
            )
        }
    }
    
    suspend fun updateReminderSettings(settings: ReminderSettings) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.REMINDERS_ENABLED] = settings.isEnabled
            preferences[PreferencesKeys.START_HOUR] = settings.startHour
            preferences[PreferencesKeys.END_HOUR] = settings.endHour
            preferences[PreferencesKeys.SKIP_DAY_PROBABILITY] = settings.skipDayProbability
        }
    }
    
    fun isFirstLaunch(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[PreferencesKeys.FIRST_LAUNCH] ?: true
        }
    }
    
    suspend fun setFirstLaunchCompleted() {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.FIRST_LAUNCH] = false
        }
    }
}