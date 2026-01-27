package com.example.freelanceproject.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.freelanceproject.ViewModel.ProjectViewModel

@Composable
fun AppNavigation(
    viewModel: ProjectViewModel
) {
    val navController = rememberNavController()

    // The "startDestination" tells the app which screen to show first
    NavHost(navController = navController, startDestination = "project_list") {

        // SCREEN 1: The Project Dashboard
        composable("project_list") {
            ProjectListScreen(
                viewModel = viewModel,
                onNavigateToTimer = {
                    // When the user clicks "Go to Timer", we fly them to the timer screen
                    navController.navigate("timer_screen")
                }
            )
        }

        // SCREEN 2: The Global Timer
        composable("timer_screen") {
            TimerScreen(
                viewModel = viewModel
            )
        }
    }
}