package com.example.freelanceproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.freelanceproject.data.local.FreelanceDatabase
import com.example.freelanceproject.data.repository.ProjectRepository
import com.example.freelanceproject.data.repository.ClientRepository
import com.example.freelanceproject.ui.theme.FreelanceProjectTheme
import com.example.freelanceproject.ui.AppNavigation
import androidx.lifecycle.ViewModelProvider
import com.example.freelanceproject.ViewModel.ProjectViewModel
import com.example.freelanceproject.ViewModel.ProjectViewModelFactory


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = FreelanceDatabase.getDatabase(this)

        val repository = ProjectRepository(database.projectDao)
        val clientRepository = ClientRepository(database.clientDao)

        val factory = ProjectViewModelFactory(repository, clientRepository)
        val viewModel = ViewModelProvider(this, factory)[ProjectViewModel::class.java]
        enableEdgeToEdge()
        setContent {
            FreelanceProjectTheme {
                AppNavigation(viewModel = viewModel)
            }
        }
    }
}

