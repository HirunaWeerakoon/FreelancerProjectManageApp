package com.example.freelanceproject.ui

import android.graphics.Color
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun ProjectItem(project: Project){
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),


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
                        text = "Client ID: ${project.clientId ?: "Unassigned"}",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Text(text=project.status.toString())
            }
            Spacer(modifier = Modifier.height(8.dp))

        }
    }

}