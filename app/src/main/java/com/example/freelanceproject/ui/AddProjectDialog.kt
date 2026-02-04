package com.example.freelanceproject.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.freelanceproject.data.local.entities.Client
import com.example.freelanceproject.data.local.entities.Project
import com.example.freelanceproject.data.local.entities.ProjectStatus
import com.example.freelanceproject.data.model.ProjectType
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.DateRange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProjectDialog(
    clients: List<Client>,
    onDismiss: () -> Unit,
    onSave: (Project) -> Unit,
    onAddClient: () -> Unit // Callback to add a client if list is empty
) {
    var title by remember { mutableStateOf("") }
    var selectedClient by remember { mutableStateOf<Client?>(null) }
    var targetAmount by remember { mutableStateOf("") }
    var targetHours by remember { mutableStateOf("") }
    var projectType by remember { mutableStateOf(ProjectType.FIXED_PRICE) }
    
    // Status Selection State
    var selectedStatus by remember { mutableStateOf(ProjectStatus.PLANNING) }
    var statusExpanded by remember { mutableStateOf(false) }

    var expanded by remember { mutableStateOf(false) } // Client Dropdown

    // Date Selection State
    var startDate by remember { mutableStateOf<Long?>(null) }
    var endDate by remember { mutableStateOf<Long?>(null) }
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    // Date Formatter
    val dateFormatter = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())

    if (showStartDatePicker) {
        val datePickerState = androidx.compose.material3.rememberDatePickerState(initialSelectedDateMillis = startDate)
        androidx.compose.material3.DatePickerDialog(
            onDismissRequest = { showStartDatePicker = false },
            confirmButton = {
                androidx.compose.material3.TextButton(onClick = {
                    startDate = datePickerState.selectedDateMillis
                    showStartDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                androidx.compose.material3.TextButton(onClick = { showStartDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            androidx.compose.material3.DatePicker(state = datePickerState)
        }
    }

    if (showEndDatePicker) {
        val datePickerState = androidx.compose.material3.rememberDatePickerState(initialSelectedDateMillis = endDate)
        androidx.compose.material3.DatePickerDialog(
            onDismissRequest = { showEndDatePicker = false },
            confirmButton = {
                androidx.compose.material3.TextButton(onClick = {
                    endDate = datePickerState.selectedDateMillis
                    showEndDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                androidx.compose.material3.TextButton(onClick = { showEndDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            androidx.compose.material3.DatePicker(state = datePickerState)
        }
    }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text("Add New Project", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))

            // Title Input
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Project Title") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Client Dropdown
            if (clients.isEmpty()) {
                Button(onClick = onAddClient, modifier = Modifier.fillMaxWidth()) {
                    Text("Add Client (Required)")
                }
            } else {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        readOnly = true,
                        value = selectedClient?.name ?: "Select Client",
                        onValueChange = { },
                        label = { Text("Client") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        clients.forEach { client ->
                            DropdownMenuItem(
                                text = { Text(client.name) },
                                onClick = {
                                    selectedClient = client
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Status Dropdown
            ExposedDropdownMenuBox(
                expanded = statusExpanded,
                onExpandedChange = { statusExpanded = !statusExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    readOnly = true,
                    value = selectedStatus.name,
                    onValueChange = { },
                    label = { Text("Status") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = statusExpanded) },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = statusExpanded,
                    onDismissRequest = { statusExpanded = false }
                ) {
                    ProjectStatus.values().forEach { status ->
                        DropdownMenuItem(
                            text = { Text(status.name) },
                            onClick = {
                                selectedStatus = status
                                statusExpanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))


            // Target Amount Input
            OutlinedTextField(
                value = targetAmount,
                onValueChange = { targetAmount = it },
                label = { Text("Target Amount ($)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Target Hours Input
            OutlinedTextField(
                value = targetHours,
                onValueChange = { targetHours = it },
                label = { Text("Target Hours") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Start Date Picker
            OutlinedTextField(
                value = startDate?.let { dateFormatter.format(java.util.Date(it)) } ?: "",
                onValueChange = {},
                label = { Text("Start Date (Optional)") },
                readOnly = true, // Keep readOnly true so keyboard doesn't open
                trailingIcon = {
                    androidx.compose.material3.IconButton(onClick = { showStartDatePicker = true }) {
                        androidx.compose.material3.Icon(androidx.compose.material.icons.Icons.Default.DateRange, contentDescription = "Select Start Date")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showStartDatePicker = true }, // Correct usage of clickable
                enabled = false, // Disables internal focus/input logic
                colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                    disabledTextColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = androidx.compose.material3.MaterialTheme.colorScheme.outline,
                    disabledLabelColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledTrailingIconColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledContainerColor = androidx.compose.material3.MaterialTheme.colorScheme.surface, // Ensure background isn't greyed out
                    disabledPlaceholderColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // End Date Picker
            OutlinedTextField(
                value = endDate?.let { dateFormatter.format(java.util.Date(it)) } ?: "",
                onValueChange = {},
                label = { Text("End Date (Optional)") },
                readOnly = true,
                trailingIcon = {
                    androidx.compose.material3.IconButton(onClick = { showEndDatePicker = true }) {
                        androidx.compose.material3.Icon(androidx.compose.material.icons.Icons.Default.DateRange, contentDescription = "Select End Date")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showEndDatePicker = true }, // Correct usage of clickable
                enabled = false,
                colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                    disabledTextColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = androidx.compose.material3.MaterialTheme.colorScheme.outline,
                    disabledLabelColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledTrailingIconColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledContainerColor = androidx.compose.material3.MaterialTheme.colorScheme.surface,
                    disabledPlaceholderColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            //test

            // Project Type Radio Buttons
            Text("Project Type", style = MaterialTheme.typography.labelLarge)
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = projectType == ProjectType.FIXED_PRICE,
                    onClick = { projectType = ProjectType.FIXED_PRICE }
                )
                Text("Fixed Price")
                Spacer(modifier = Modifier.width(16.dp))
                RadioButton(
                    selected = projectType == ProjectType.HOURLY,
                    onClick = { projectType = ProjectType.HOURLY }
                )
                Text("Hourly")
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Save Button
            Button(
                onClick = {
                    val amount = targetAmount.toDoubleOrNull() ?: 0.0
                    val hours = targetHours.toDoubleOrNull()
                    if (title.isNotBlank() && selectedClient != null) {
                        val newProject = Project(
                            title = title,
                            clientID = selectedClient?.id.toString(), // Store Client ID as String
                            projectType = projectType,
                            description = "",
                            targetAmount = amount,
                            targetHours = hours, // Save Target Hours
                            paidAmount = 0.0,
                            totalHours = 0.0,
                            status = selectedStatus, // Use selected status
                            isCompleted = false,
                            startDate = startDate?.let { java.util.Date(it) },
                            endDate = endDate?.let { java.util.Date(it) }
                        )
                        onSave(newProject)
                        onDismiss()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = title.isNotBlank() && selectedClient != null
            ) {
                Text("Save Project")
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
