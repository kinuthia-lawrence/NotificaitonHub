package com.larrykin.notificationhub.core.presentation.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun BottomNavigation(nestedNavController: NavController) {
    val currentRoute = nestedNavController.currentBackStackEntryAsState().value?.destination?.route
    NavigationBar {
        NavigationBarItem(
            selected = currentRoute == "home",
            onClick = {
                nestedNavController.navigate("home") {
                    popUpTo("dashboard") { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = { Icon(Icons.Filled.Home, null) },
            label = { Text("Home") }
        )

        NavigationBarItem(
            selected = currentRoute == "profiles",
            onClick = {
                nestedNavController.navigate("profiles") {
                    popUpTo("dashboard") { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = { Icon(Icons.AutoMirrored.Filled.List, null) },
            label = { Text("Profiles") }
        )

        NavigationBarItem(
            selected = currentRoute == "history",
            onClick = {
                nestedNavController.navigate("history") {
                    popUpTo("dashboard") { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = { Icon(Icons.Filled.Info, null) },
            label = { Text("History") }
        )

        NavigationBarItem(
            selected = currentRoute == "analytics",
            onClick = {
                nestedNavController.navigate("analytics") {
                    popUpTo("dashboard") { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = { Icon(Icons.Filled.Info, null) },
            label = { Text("Analytics") }
        )
    }

}

@Preview
@Composable
fun PreviewBottomNavigation() {
    val navController = rememberNavController()
    BottomNavigation(navController)
}

