package com.example.freelanceproject.ui

import ProjectViewModel
import android.R.attr.onClick
import android.graphics.Color
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.unit.dp
import com.example.freelanceproject.data.local.entities.Project
import kotlin.collections.emptyList

@Composable
fun ProjectListScreen(
    viewModel: ProjectViewModel
) {
    // Collecting the list of projects from the Flow we built in the Repository
    val projects by viewModel.projects.collectAsState(initial = emptyList())

    Scaffold(
        floatingActionButton = {
            // This is where the "+" button to add a new project will go
        }
    ) { paddingValues ->
        // LazyColumn is like a RecyclerView - it only renders what is on screen
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier.fillMaxSize()
        ) {
            items(projects) { project ->
                ProjectItem(project = project)
            }
        }
    }
}
@Composable
fun ProjectItem(project: Project, onClick: () -> Unit){

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        ) {
        var selectedProject by remember{ mutableStateOf<Project?>(null) }
        var showSheet by remember{mutableStateOf(false)}

        ProjectItem(
            project=project,
            onClick={
                selectedProject=project
                showSheet=true
            }
        )
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
                        text = "Client ID: ${project.clientId ?: "Unassigned"}",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Text(text = project.status.toString())
            }
            Spacer(modifier = Modifier.height(8.dp))

            val progress = if (project.targetAmount > 0) {
                project.paidAmount / project.targetAmount
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
    onConfirm: (Double) -> Unit
) {
    var amountText by remember { mutableStateOf("") }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Add Payment for ${project.title}", style = MaterialTheme.typography.headlineSmall)

            OutlinedTextField(
                value = amountText,
                onValueChange = { amountText = it },
                label = { Text("Amount (USD)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    val additionalAmount = amountText.toDoubleOrNull() ?: 0.0
                    onConfirm(additionalAmount)
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            ) {
                Text("Update Progress")
            }
        }
    }
}