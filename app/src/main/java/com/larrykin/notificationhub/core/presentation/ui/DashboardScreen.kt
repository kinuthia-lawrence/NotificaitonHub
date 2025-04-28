package com.larrykin.notificationhub.core.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.larrykin.notificationhub.core.presentation.viewModels.MainViewModel

@Composable
fun DashboardScreen(viewModel: MainViewModel) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigation(navController)
        }
    )
    { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues))
        NavHost(
            navController = navController,
            startDestination = "home"
        ) {
            composable("home") {
                HomeScreen(navController, viewModel)
            }
            composable("profiles") {
                ProfilesScreen(navController)
            }
            composable("history") {
                HistoryScreen(navController)
            }
            composable("settings") {
                SettingsScreen(navController)
            }
            composable("appDetails/{packageName}") { backStackEntry ->
                val packageName = backStackEntry.arguments?.getString("packageName")
                packageName?.let {
                    AppDetailScreen(packageName, navController)
                }
            }

        }

    }
}

