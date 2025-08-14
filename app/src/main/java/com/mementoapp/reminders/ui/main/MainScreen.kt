package com.mementoapp.reminders.ui.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import com.mementoapp.reminders.ui.components.FirstLaunchDialog
import com.mementoapp.reminders.ui.components.SettingsDialog
import com.mementoapp.reminders.ui.components.JournalDialog
import com.mementoapp.reminders.ui.components.CalendarView
import com.mementoapp.reminders.notifications.ReminderSettings
import com.mementoapp.reminders.data.repository.JournalRepository
import com.mementoapp.reminders.notifications.NotificationManager
import com.mementoapp.reminders.notifications.ReminderScheduler
import com.mementoapp.reminders.data.model.JournalEntry
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    reminderLogId: Long? = null
) {
    val context = LocalContext.current
    val journalRepository = remember { JournalRepository(context) }
    val notificationManager = remember { NotificationManager(context) }
    val reminderScheduler = remember { ReminderScheduler(context) }
    val viewModel = remember { MainViewModel(journalRepository, notificationManager, reminderScheduler) }
    
    val journalEntries by viewModel.journalEntries.collectAsState()
    
    var showSettings by remember { mutableStateOf(false) }
    var isFirstLaunch by remember { mutableStateOf(true) }
    var reminderSettings by remember { mutableStateOf(ReminderSettings()) }
    var selectedTabIndex by remember { mutableStateOf(0) }
    var showJournalDialog by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedJournalEntry by remember { mutableStateOf<JournalEntry?>(null) }
    
    // Show first launch dialog
    if (isFirstLaunch) {
        FirstLaunchDialog(
            onDismiss = { 
                isFirstLaunch = false
                viewModel.scheduleReminders()
            }
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
    
    // Journal dialog
    if (showJournalDialog) {
        JournalDialog(
            date = selectedDate,
            existingEntry = selectedJournalEntry,
            onSave = { entry ->
                viewModel.saveJournalEntry(entry)
                showJournalDialog = false
            },
            onDismiss = { showJournalDialog = false }
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
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = selectedTabIndex == 0,
                    onClick = { selectedTabIndex = 0 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.DateRange, contentDescription = "Journal") },
                    label = { Text("Journal") },
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 1 }
                )
            }
        }
    ) { paddingValues ->
        when (selectedTabIndex) {
            0 -> HomeTabContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                onTestNotification = { viewModel.sendTestNotification() },
                onOpenJournal = { 
                    selectedDate = LocalDate.now()
                    selectedJournalEntry = null
                    showJournalDialog = true
                }
            )
            1 -> JournalTabContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                journalEntries = journalEntries,
                onDateSelected = { date ->
                    selectedDate = date
                    viewModel.viewModelScope.launch {
                        selectedJournalEntry = viewModel.getJournalEntry(date).first()
                        showJournalDialog = true
                    }
                }
            )
        }
    }
}

@Composable
private fun HomeTabContent(
    modifier: Modifier = Modifier,
    onTestNotification: () -> Unit,
    onOpenJournal: () -> Unit
) {
    LazyColumn(
        modifier = modifier,
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
                        text = "Quick Actions",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Button(
                        onClick = onTestNotification,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Test Notification")
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    OutlinedButton(
                        onClick = onOpenJournal,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Write in Journal")
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
                        text = "How It Works",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    val features = listOf(
                        "📱 Random reminders throughout the day",
                        "🌙 Daily journaling prompts at 9 PM",
                        "📅 Calendar view of your journal entries",
                        "💭 Thought-provoking questions about time and mortality",
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
    }
}

@Composable
private fun JournalTabContent(
    modifier: Modifier = Modifier,
    journalEntries: List<JournalEntry>,
    onDateSelected: (LocalDate) -> Unit
) {
    Column(modifier = modifier) {
        CalendarView(
            journalEntries = journalEntries,
            onDateSelected = onDateSelected,
            modifier = Modifier.fillMaxWidth()
        )
        
        if (journalEntries.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Recent Entries",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            
            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(journalEntries.take(5)) { entry ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onDateSelected(entry.date) }
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = entry.date.toString(),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                            if (entry.highlight.isNotBlank()) {
                                Text(
                                    text = entry.highlight,
                                    style = MaterialTheme.typography.bodyMedium,
                                    maxLines = 2
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}