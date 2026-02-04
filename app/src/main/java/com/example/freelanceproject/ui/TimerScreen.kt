package com.example.freelanceproject.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.freelanceproject.ViewModel.ProjectViewModel
import com.example.freelanceproject.data.local.entities.Project
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerScreen(
    viewModel: ProjectViewModel
) {
    // 1. Get all projects
    val projects by viewModel.projects.collectAsState(initial = emptyList())

    // 2. State for the locked project ID (not the object itself, to avoid stale state)
    var selectedProjectId by remember { mutableStateOf<Int?>(null) }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    // 3. Derive the ACTUAL selected project from the live list
    val selectedProject = projects.find { it.id == selectedProjectId } ?: projects.firstOrNull { it.timerStartTime != null }
    
    // Auto-select helper: if nothing selected, select the first active one or just keep null
    LaunchedEffect(projects) {
       if (selectedProjectId == null) {
           val active = projects.find { it.timerStartTime != null }
           if (active != null) selectedProjectId = active.id
       }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ExposedDropdownMenuBox(
            expanded = isDropdownExpanded,
            onExpandedChange = { isDropdownExpanded = !isDropdownExpanded }
        ) {
            OutlinedTextField(
                value = selectedProject?.title ?: "Select a Project",
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropdownExpanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = isDropdownExpanded,
                onDismissRequest = { isDropdownExpanded = false }
            ) {
                projects.forEach { project ->
                    DropdownMenuItem(
                        text = { Text(project.title) },
                        onClick = {
                            selectedProjectId = project.id
                            isDropdownExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        // timer
        if (selectedProject != null) {
            ActiveTimerDisplay(
                project = selectedProject,
                onToggleTimer = { viewModel.toggleTimer(it) }
            )
        } else {
            Text("Please create a project first")
        }
    }
}

@Composable
fun ActiveTimerDisplay(
    project: Project,
    onToggleTimer: (Project) -> Unit
) {
    // We keep a local state for "Elapsed Seconds" so the UI updates every second
    var elapsedSeconds by remember { mutableLongStateOf(0L) }

    // THE TICKER: Updates every 1 second if timerStartTime is not null
    LaunchedEffect(project.timerStartTime) {
        if (project.timerStartTime != null) {
            while (true) {
                val timeDiff = System.currentTimeMillis() - (project.timerStartTime ?: System.currentTimeMillis())
                elapsedSeconds = timeDiff / 1000
                delay(1000) // Wait 1 second
            }
        } else {
            elapsedSeconds = 0L // Reset if stopped
        }
    }

    // Format Seconds to HH:MM:SS
    val hours = elapsedSeconds / 3600
    val minutes = (elapsedSeconds % 3600) / 60
    val seconds = elapsedSeconds % 60
    val formattedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds)

    Text(text = formattedTime, fontSize = 64.sp, style = MaterialTheme.typography.displayLarge)

    Spacer(modifier = Modifier.height(24.dp))

    // Total accumulated history
    Text(text = "Previous Total: ${"%.1f".format(project.totalHours ?: 0.0)} hrs", color = MaterialTheme.colorScheme.secondary)

    Spacer(modifier = Modifier.height(32.dp))

    Button(
        onClick = { onToggleTimer(project) },
        colors = ButtonDefaults.buttonColors(
            containerColor = if (project.timerStartTime != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
        ),
        modifier = Modifier.fillMaxWidth().height(56.dp)
    ) {
        Text(if (project.timerStartTime != null) "STOP TRACKING" else "START TRACKING")
    }
}