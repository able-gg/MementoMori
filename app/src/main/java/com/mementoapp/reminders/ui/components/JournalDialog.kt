package com.mementoapp.reminders.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.mementoapp.reminders.data.model.JournalEntry
import java.time.LocalDate

@Composable
fun JournalDialog(
    date: LocalDate,
    existingEntry: JournalEntry? = null,
    onSave: (JournalEntry) -> Unit,
    onDismiss: () -> Unit
) {
    var highlight by remember { mutableStateOf(existingEntry?.highlight ?: "") }
    var thoughts by remember { mutableStateOf(existingEntry?.thoughts ?: "") }
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Daily Reflection",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                
                Text(
                    text = date.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    text = "What was the highlight of your day?",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = highlight,
                    onValueChange = { highlight = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Share your best moment...") },
                    minLines = 2,
                    maxLines = 4
                )
                
                Spacer(modifier = Modifier.height(20.dp))
                
                Text(
                    text = "Any other thoughts or reflections?",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = thoughts,
                    onValueChange = { thoughts = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("What's on your mind?") },
                    minLines = 3,
                    maxLines = 6
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
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
                            if (highlight.isNotBlank() || thoughts.isNotBlank()) {
                                onSave(
                                    JournalEntry(
                                        id = existingEntry?.id ?: "",
                                        date = date,
                                        highlight = highlight,
                                        thoughts = thoughts
                                    )
                                )
                            }
                        },
                        modifier = Modifier.weight(1f),
                        enabled = highlight.isNotBlank() || thoughts.isNotBlank()
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}