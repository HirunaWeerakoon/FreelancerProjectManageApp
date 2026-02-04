package com.example.freelanceproject.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.freelanceproject.data.repository.ProjectRepository
import com.example.freelanceproject.data.repository.ClientRepository

class ProjectViewModelFactory(
    private val repository: ProjectRepository,
    private val clientRepository: ClientRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProjectViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProjectViewModel(repository, clientRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}