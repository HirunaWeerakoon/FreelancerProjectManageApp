package com.example.freelanceproject.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object Projects : BottomNavItem("project_list", "Projects", Icons.Default.Home)
    object Timer : BottomNavItem("timer_screen", "Timer", Icons.Default.Timer)
    object Clients : BottomNavItem("client_list", "Clients", Icons.Default.Person)
    object Stats : BottomNavItem("stats_screen", "Calendar", Icons.Default.DateRange)
}
