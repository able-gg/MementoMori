package com.mementoapp.reminders.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
// Removed lifecycle import
import com.mementoapp.reminders.ui.components.FirstLaunchDialog
import com.mementoapp.reminders.ui.components.SettingsDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    reminderLogId: Long? = null
) {
    // Simplified version without ViewModel for now
    var showSettings by remember { mutableStateOf(false) }
    var isFirstLaunch by remember { mutableStateOf(true) }
    var reminderSettings by remember { mutableStateOf(com.mementoapp.reminders.notifications.ReminderSettings()) }
    
    // Show first launch dialog
    if (isFirstLaunch) {
        FirstLaunchDialog(
            onDismiss = { isFirstLaunch = false }
        )
    }
    
    // Settings dialog
    if (showSettings) {
        SettingsDialog(
            settings = reminderSettings,
            onSettingsChange = { reminderSettings = it },
            onDismiss = { showSettings = false }
        )
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Memento Mori") },
                actions = {
                    IconButton(onClick = { showSettings = true }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "⏳",
                            style = MaterialTheme.typography.displayLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Remember Death",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Time is your most precious resource. Use it wisely.",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                }
            }
            
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "How It Works",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        val features = listOf(
                            "📱 Random reminders throughout the day",
                            "🎲 Unpredictable timing for maximum impact",
                            "💭 Thought-provoking questions about time and mortality",
                            "⏰ Customizable active hours",
                            "🔕 Smart skipping - sometimes no reminders for days"
                        )
                        
                        features.forEach { feature ->
                            Text(
                                text = feature,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    }
                }
            }
            
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "Current Status",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Reminders",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = if (reminderSettings.isEnabled) "Active" else "Paused",
                                style = MaterialTheme.typography.bodyLarge,
                                color = if (reminderSettings.isEnabled) 
                                    MaterialTheme.colorScheme.primary 
                                else 
                                    MaterialTheme.colorScheme.error,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        
                        if (reminderSettings.isEnabled) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Active hours: ${reminderSettings.startHour}:00 - ${reminderSettings.endHour}:00",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }
        }
    }
}