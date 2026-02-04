package com.example.freelanceproject.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.freelanceproject.ViewModel.ProjectViewModel

@Composable
fun AppNavigation(
    viewModel: ProjectViewModel
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                val items = listOf(
                    BottomNavItem.Projects,
                    BottomNavItem.Timer,
                    BottomNavItem.Clients,
                    BottomNavItem.Stats
                )
                items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Projects.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // SCREEN 1: The Project Dashboard
            composable(BottomNavItem.Projects.route) {
                ProjectListScreen(
                    viewModel = viewModel,
                    onNavigateToTimer = {
                        navController.navigate(BottomNavItem.Timer.route)
                    }
                )
            }

            // SCREEN 2: The Global Timer
            composable(BottomNavItem.Timer.route) {
                TimerScreen(
                    viewModel = viewModel
                )
            }
            // 3. Client List Stub
            composable(BottomNavItem.Clients.route) {
                ClientListScreen(
                    viewModel = viewModel,
                    onNavigateToClientDetail = { clientId ->
                        navController.navigate("client_detail/$clientId")
                    }
                )
            }

            // 4. Stats Stub
            composable(BottomNavItem.Stats.route) {
                StatsScreen(viewModel = viewModel)
            }

            // Client Detail Screen
            composable(
                route = "client_detail/{clientId}",
                arguments = listOf(androidx.navigation.navArgument("clientId") { type = androidx.navigation.NavType.IntType })
            ) { backStackEntry ->
                val clientId = backStackEntry.arguments?.getInt("clientId") ?: return@composable
                ClientDetailScreen(
                    clientId = clientId,
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToEdit = { id -> navController.navigate("client_edit/$id") },
                    onNavigateToProjectDetail = { projectId -> navController.navigate("project_detail/$projectId") }
                )
            }

            // Client Edit Screen
            composable(
                route = "client_edit/{clientId}",
                arguments = listOf(androidx.navigation.navArgument("clientId") { type = androidx.navigation.NavType.IntType })
            ) { backStackEntry ->
                val clientId = backStackEntry.arguments?.getInt("clientId") ?: return@composable
                ClientEditScreen(
                    clientId = clientId,
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // Project Detail Screen
            composable(
                route = "project_detail/{projectId}",
                arguments = listOf(androidx.navigation.navArgument("projectId") { type = androidx.navigation.NavType.IntType })
            ) { backStackEntry ->
                val projectId = backStackEntry.arguments?.getInt("projectId") ?: return@composable
                ProjectDetailScreen(
                    projectId = projectId,
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}