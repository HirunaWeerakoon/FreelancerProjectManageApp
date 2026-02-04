package com.example.freelanceproject.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.freelanceproject.ViewModel.ProjectViewModel
import com.example.freelanceproject.data.local.entities.Project
import com.example.freelanceproject.data.local.entities.ProjectStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectDetailScreen(
    projectId: Int,
    viewModel: ProjectViewModel,
    onNavigateBack: () -> Unit
) {
    val projects by viewModel.projects.collectAsState()
    val project = projects.find { it.id == projectId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(project?.title ?: "Project Details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (project == null) {
            Box(Modifier.padding(innerPadding)) { Text("Project not found") }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                InfoCard("Description", project.description ?: "No description")
                
                StatusCard(
                    currentStatus = project.status,
                    onStatusSelected = { newStatus ->
                        viewModel.updateProject(project.copy(status = newStatus))
                    }
                )

                InfoCard("Type", project.projectType.name)
                InfoCard("Target Amount", "$${project.targetAmount}")
                InfoCard("Paid Amount", "$${project.paidAmount}")
                project.targetHours?.let {
                    InfoCard("Target Hours", "$it")
                }
                project.totalHours?.let {
                    InfoCard("Total Hours Logged", "%.2f".format(it))
                }
            }
        }
    }
}

@Composable
fun InfoCard(title: String, content: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = content, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun StatusCard(currentStatus: ProjectStatus, onStatusSelected: (ProjectStatus) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = true },
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = "Status", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = currentStatus.name, style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Change Status")
                }
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                ProjectStatus.values().forEach { status ->
                    DropdownMenuItem(
                        text = { Text(status.name) },
                        onClick = {
                            onStatusSelected(status)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
