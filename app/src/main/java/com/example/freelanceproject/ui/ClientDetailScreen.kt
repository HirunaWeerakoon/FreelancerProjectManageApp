package com.example.freelanceproject.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.freelanceproject.ViewModel.ProjectViewModel
import com.example.freelanceproject.data.local.entities.Client
import com.example.freelanceproject.data.local.entities.Project

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientDetailScreen(
    clientId: Int,
    viewModel: ProjectViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (Int) -> Unit,
    onNavigateToProjectDetail: (Int) -> Unit
) {
    val clients by viewModel.clients.collectAsState()
    val projects by viewModel.projects.collectAsState()

    val client = clients.find { it.id == clientId }
    // Project.clientID is String?, Client.id is Int. Matching via toString()
    val clientProjects = projects.filter { it.clientID == clientId.toString() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(client?.name ?: "Client Details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (client != null) {
                        IconButton(onClick = { onNavigateToEdit(client.id) }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit Client")
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        if (client == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("Client not found")
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                ClientInfoSection(client)
                Spacer(modifier = Modifier.height(24.dp))
                Text("Projects", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(clientProjects) { project ->
                        ProjectItemSimple(project = project, onClick = { onNavigateToProjectDetail(project.id) })
                    }
                    if (clientProjects.isEmpty()) {
                        item {
                            Text("No projects for this client.", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ClientInfoSection(client: Client) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            DetailRow(label = "Name", value = client.name)
            client.companyName?.let { DetailRow(label = "Company", value = it) }
            client.email?.let { DetailRow(label = "Email", value = it) }
            client.phone?.let { DetailRow(label = "Phone", value = it) }
            client.address?.let { DetailRow(label = "Address", value = it) }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(text = "$label: ", fontWeight = FontWeight.Bold)
        Text(text = value)
    }
}

@Composable
fun ProjectItemSimple(project: Project, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = project.title, style = MaterialTheme.typography.titleMedium)
            Text(text = project.status.name, style = MaterialTheme.typography.bodySmall)
        }
    }
}
