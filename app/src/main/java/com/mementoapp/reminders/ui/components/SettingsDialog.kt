package com.mementoapp.reminders.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.mementoapp.reminders.notifications.ReminderSettings
import kotlin.math.roundToInt

@Composable
fun SettingsDialog(
    settings: ReminderSettings,
    onSettingsChange: (ReminderSettings) -> Unit,
    onDismiss: () -> Unit
) {
    var tempSettings by remember { mutableStateOf(settings) }
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Enable/Disable Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Enable Reminders",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Switch(
                        checked = tempSettings.isEnabled,
                        onCheckedChange = { 
                            tempSettings = tempSettings.copy(isEnabled = it)
                        }
                    )
                }
                
                if (tempSettings.isEnabled) {
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    // Start Hour
                    Text(
                        text = "Start Hour: ${tempSettings.startHour}:00",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Slider(
                        value = tempSettings.startHour.toFloat(),
                        onValueChange = { 
                            tempSettings = tempSettings.copy(startHour = it.roundToInt())
                        },
                        valueRange = 0f..23f,
                        steps = 22
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // End Hour
                    Text(
                        text = "End Hour: ${tempSettings.endHour}:00",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Slider(
                        value = tempSettings.endHour.toFloat(),
                        onValueChange = { 
                            tempSettings = tempSettings.copy(endHour = it.roundToInt())
                        },
                        valueRange = 0f..23f,
                        steps = 22
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Skip Day Probability
                    Text(
                        text = "Skip Day Chance: ${(tempSettings.skipDayProbability * 100).roundToInt()}%",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Higher values mean more unpredictable reminders",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Slider(
                        value = tempSettings.skipDayProbability,
                        onValueChange = { 
                            tempSettings = tempSettings.copy(skipDayProbability = it)
                        },
                        valueRange = 0f..0.5f
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel")
                    }
                    
                    Button(
                        onClick = {
                            onSettingsChange(tempSettings)
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}