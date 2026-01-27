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
    // 1. Get all projects for the Dropdown
    val projects by viewModel.projects.collectAsState(initial = emptyList())

    // 2. State for the Dropdown
    var isDropdownExpanded by remember { mutableStateOf(false) }
    var selectedProject by remember { mutableStateOf<Project?>(null) }

    // 3. Auto-select the first running project (if any) when screen opens
    LaunchedEffect(projects) {
        if (selectedProject == null) {
            selectedProject = projects.find { it.timerStartTime != null } ?: projects.firstOrNull()
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
                            selectedProject = project
                            isDropdownExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        // timer
        // If a project is selected, show its timer
        if (selectedProject != null) {
            val project = selectedProject!! // Force unwrap since we checked null

            // This composable handles the "Ticking" logic separate from the database
            ActiveTimerDisplay(
                project = project,
                onToggleTimer = { viewModel.toggleTimer(it) } // You need to add this to VM
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
                val timeDiff = System.currentTimeMillis() - project.timerStartTime
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
    Text(text = "Previous Total: ${"%.1f".format(project.totalHours)} hrs", color = MaterialTheme.colorScheme.secondary)

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