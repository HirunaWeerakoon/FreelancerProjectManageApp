package com.example.freelanceproject.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.freelanceproject.data.local.entities.Project
import com.example.freelanceproject.data.local.entities.Client
import com.example.freelanceproject.data.repository.ProjectRepository
import com.example.freelanceproject.data.repository.ClientRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProjectViewModel(
    private val repository: ProjectRepository,
    private val clientRepository: ClientRepository // New dependency
) : ViewModel() {

    // Use StateFlow to hold the list of projects
    // We 'stateIn' the flow from the repository to make it a StateFlow
    val projects: StateFlow<List<Project>> = repository.allProjects
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val clients: StateFlow<List<Client>> = clientRepository.allClients
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addProject(project: Project){
        viewModelScope.launch {
            repository.insert(project)
        }
    }
    fun updateProject(project: Project){
        viewModelScope.launch {
            repository.update(project)
        }
    }
    fun deleteProject(project: Project){
        viewModelScope.launch {
            repository.delete(project)
        }
    }
    // In ProjectViewModel.kt
    fun toggleTimer(project: Project) {
        viewModelScope.launch {
            if (project.timerStartTime == null) {
                // START: Save current timestamp
                val updatedProject = project.copy(timerStartTime = System.currentTimeMillis())
                repository.update(updatedProject)
            } else {
                // STOP: Calculate difference and add to totalHours
                val currentTime = System.currentTimeMillis()
                val timeElapsedMs = currentTime - project.timerStartTime

                // Convert Ms to Hours (Decimal)
                val hoursToAdd = timeElapsedMs / (1000.0 * 60 * 60)

                val updatedProject = project.copy(
                    timerStartTime = null, // Reset active timer
                    totalHours = (project.totalHours ?: 0.0) + hoursToAdd
                )
                repository.update(updatedProject)
            }
        }
    }

    fun addClient(client: Client){
        viewModelScope.launch {
            clientRepository.insert(client)
        }
    }

    fun updateClient(client: Client){
        viewModelScope.launch {
            clientRepository.update(client)
        }
    }


}