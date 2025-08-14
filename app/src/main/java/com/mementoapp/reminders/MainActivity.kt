package com.mementoapp.reminders

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.mementoapp.reminders.ui.main.MainScreen
import com.mementoapp.reminders.ui.theme.MementoRemindersTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val reminderLogId = intent.getLongExtra("reminder_log_id", -1L)
        
        setContent {
            MementoRemindersTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(
                        reminderLogId = if (reminderLogId != -1L) reminderLogId else null
                    )
                }
            }
        }
    }
}