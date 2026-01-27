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
import com.example.freelanceproject.data.local.entities.ProjectStatus
import com.example.freelanceproject.data.model.ProjectType
import com.example.freelanceproject.data.repository.ProjectRepository
import com.example.freelanceproject.ui.theme.FreelanceProjectTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.util.Date
import kotlin.collections.emptyList

@Composable
fun ProjectListScreen(
    viewModel: ProjectViewModel,
    onNavigateToTimer: () -> Unit
) {
    var selectedProject by remember{ mutableStateOf<Project?>(null) }
    var showSheet by remember{mutableStateOf(false)}


    // Collecting the list of projects from the Flow we built in the Repository
    val projects by viewModel.projects.collectAsState(initial = emptyList())

    Scaffold(
        floatingActionButton = {
            // This is where the "+" button to add a new project will go
        }
    ) { paddingValues ->
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
            items(projects) { project ->
                ProjectItem(project = project, onClick = {
                    selectedProject = project
                    showSheet = true
                })
            }
        }
    }
}
@Composable
fun ProjectItem(project: Project,
                onClick: () -> Unit,

)

{

    Card(
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
                    Text(
                        text = "Client ID: ${project.clientID ?: "Unassigned"}",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Text(text = project.status.toString())
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
