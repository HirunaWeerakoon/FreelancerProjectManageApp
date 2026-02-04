package com.example.freelanceproject.ui

import com.example.freelanceproject.ViewModel.ProjectViewModel
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.freelanceproject.data.local.entities.Project
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import com.example.freelanceproject.data.local.entities.ProjectStatus
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon

@Composable
fun ProjectListScreen(
    viewModel: ProjectViewModel,
    onNavigateToTimer: () -> Unit
) {
    var selectedProject by remember{ mutableStateOf<Project?>(null) }
    var showSheet by remember{mutableStateOf(false)}
    var showAddProjectDialog by remember { mutableStateOf(false) }
    var showAddClientDialog by remember { mutableStateOf(false) }

    // Collecting the list of projects from the Flow we built in the Repository
    val projects by viewModel.projects.collectAsState(initial = emptyList())
    // Collect clients for the dropdown
    val clients by viewModel.clients.collectAsState(initial = emptyList())

    // Stats Calculation
    val totalMoneyEarned = projects.sumOf { it.paidAmount }
    val totalHoursWorked = projects.sumOf { it.totalHours ?: 0.0 }
    val pendingIncome = projects.sumOf {
         if (it.targetAmount > it.paidAmount) it.targetAmount - it.paidAmount else 0.0
    }
    val effectiveHourlyRate = if (totalHoursWorked > 0) totalMoneyEarned / totalHoursWorked else 0.0

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddProjectDialog = true }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Project")
            }
        }
    ) { paddingValues ->
        if (showAddClientDialog) {
            AddClientDialog(
                onDismiss = { showAddClientDialog = false },
                onSave = { newClient ->
                    viewModel.addClient(newClient)
                    // Optionally keep AddProjectDialog open or it remains open by state
                }
            )
        }

        if (showAddProjectDialog) {
            AddProjectDialog(
                clients = clients,
                onDismiss = { showAddProjectDialog = false },
                onSave = { newProject ->
                    viewModel.addProject(newProject)
                },
                onAddClient = {
                    showAddClientDialog = true
                }
            )
        }

        if (showSheet && selectedProject != null) {
            AddMoneySheet(
                project = selectedProject!!,
                onDismiss = { showSheet = false },

                // 1. Add Money Logic
                onAddMoney = { amount ->
                    val updated = selectedProject!!.copy(
                        paidAmount = selectedProject!!.paidAmount + amount
                    )
                    viewModel.updateProject(updated)
                },

                // 2. Add Manual Hours Logic
                onAddManualHours = { hours ->
                    val updated = selectedProject!!.copy(
                        totalHours = (selectedProject!!.totalHours ?: 0.0) + hours
                    )
                    viewModel.updateProject(updated)
                },

                // 3. Navigation Logic
                onNavigateToTimer = {
                    onNavigateToTimer() // Pass the baton up to the parent
                }
            )
        }
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier.fillMaxSize()
        ) {
            // Stats Header
            item {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Dashboard",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Row(modifier = Modifier.fillMaxWidth()) {
                        StatCard(
                            title = "Total Earned",
                            value = "$${String.format("%.2f", totalMoneyEarned)}",
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        StatCard(
                            title = "Total Hours",
                            value = "${String.format("%.1f", totalHoursWorked)} hrs",
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        StatCard(
                            title = "Pending Income",
                            value = "$${String.format("%.2f", pendingIncome)}",
                            color = MaterialTheme.colorScheme.tertiaryContainer,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        StatCard(
                            title = "Hourly Rate",
                            value = "$${String.format("%.2f", effectiveHourlyRate)}/hr",
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            items(projects) { project ->
                ProjectItem(
                    project = project,
                    onClick = {
                        selectedProject = project
                        showSheet = true
                    },
                    onStatusChange = { newStatus ->
                        viewModel.updateProject(project.copy(status = newStatus))
                    }
                )
            }
        }
    }
}
@Composable
fun StatCard(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = color),
        modifier = modifier.height(110.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
        ) {
            Text(text = title, style = MaterialTheme.typography.labelMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = value, style = MaterialTheme.typography.headlineSmall, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
        }
    }
}

@Composable
fun ProjectItem(
    project: Project,
    onClick: () -> Unit,
    onStatusChange: (ProjectStatus) -> Unit // New callback for status change
) {
    // Determine status color for the dropdown button
    val statusColor = when (project.status) {
        ProjectStatus.PLANNING -> Color(0xFFFFF9BD)
        ProjectStatus.ACTIVE -> Color(0xFF98A1BC)
        ProjectStatus.ADVANCED -> Color(0xFF98A1BC)
        ProjectStatus.PAID -> Color(0xFFB0DB9C)
        ProjectStatus.COMPLETED -> Color(0xFFCAE8BD)
        ProjectStatus.CANCELLED -> Color(0xFFDA6C6C)
    }

    var statusExpanded by remember { mutableStateOf(false) }

    Card(
        // Static background color as requested: AEDEFC
        colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = Color(0xFFAEDEFC)),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick() },
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = project.title, style = MaterialTheme.typography.titleMedium)
                    // Show start/end date if available
                    if (project.startDate != null || project.endDate != null) {
                         val dateFormat = java.text.SimpleDateFormat("MMM dd", java.util.Locale.getDefault())
                         val start = project.startDate?.let { dateFormat.format(it) } ?: "?"
                         val end = project.endDate?.let { dateFormat.format(it) } ?: "?"
                         Text(
                            text = "$start - $end",
                             style = MaterialTheme.typography.bodySmall,
                             color = Color.Gray
                         )
                    }
                    Text(
                        text = "Client ID: ${project.clientID ?: "Unassigned"}",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                // Status Dropdown Button
                Box {
                    androidx.compose.material3.Button(
                        onClick = { statusExpanded = true },
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = statusColor),
                        modifier = Modifier.height(30.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                    ) {
                         Text(text = project.status.name, style = MaterialTheme.typography.labelSmall, color = Color.Black)
                         Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.Black)
                    }
                    DropdownMenu(
                        expanded = statusExpanded,
                        onDismissRequest = { statusExpanded = false }
                    ) {
                        ProjectStatus.values().forEach { status ->
                            DropdownMenuItem(
                                text = { Text(status.name) },
                                onClick = {
                                    onStatusChange(status)
                                    statusExpanded = false
                                }
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            val progress = if (project.targetAmount > 0) {
                (project.paidAmount / project.targetAmount).toFloat()
            } else {
                0f
            }
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Time Progress
            if (project.targetHours != null && project.targetHours > 0) {
                val timeProgress = (project.totalHours ?: 0.0).toFloat() / project.targetHours.toFloat()
                Text(
                    text = "Time: ${String.format("%.1f", project.totalHours ?: 0.0)} / ${project.targetHours} hrs",
                    style = MaterialTheme.typography.bodySmall
                )
                LinearProgressIndicator(
                    progress = { timeProgress },
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFFFFA500) // Orange for Time
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMoneySheet(
    project: Project,
    onDismiss: () -> Unit,
    onAddMoney: (Double) -> Unit,
    onAddManualHours: (Double) -> Unit, // New: Logic to add manual hours
    onNavigateToTimer: () -> Unit       // New: Event to go to Timer Screen
) {
    var moneyText by remember { mutableStateOf("") }
    var hoursText by remember { mutableStateOf("") }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text("Manage ${project.title}", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))

            // --- SECTION 1: MONEY ---
            Text("Add Earnings", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = moneyText,
                    onValueChange = { moneyText = it },
                    label = { Text("$ Amount") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    val amount = moneyText.toDoubleOrNull() ?: 0.0
                    onAddMoney(amount)
                    moneyText = "" // Clear input
                }) {
                    Text("Add")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider() // Visual separator
            Spacer(modifier = Modifier.height(24.dp))

            // --- SECTION 2: TIME ---
            Text("Track Time", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)

            // Manual Entry
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = hoursText,
                    onValueChange = { hoursText = it },
                    label = { Text("Manual Hours") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    val hours = hoursText.toDoubleOrNull() ?: 0.0
                    onAddManualHours(hours)
                    hoursText = "" // Clear input
                }) {
                    Text("Add")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Navigation Button
            OutlinedButton(
                onClick = {
                    onNavigateToTimer()
                    onDismiss() // Close the sheet when leaving
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                // You can add an Icon here if you want (Icons.Default.Timer)
                Text("Go to Timer Dashboard")
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
