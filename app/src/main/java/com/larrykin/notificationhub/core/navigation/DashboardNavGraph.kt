package com.larrykin.notificationhub.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.larrykin.notificationhub.core.presentation.ui.AnalyticsScreen
import com.larrykin.notificationhub.core.presentation.ui.HistoryScreen
import com.larrykin.notificationhub.core.presentation.ui.HomeScreen
import com.larrykin.notificationhub.core.presentation.ui.ProfilesScreen
import com.larrykin.notificationhub.core.presentation.viewModels.MainViewModel

@Composable
fun DashboardNavGraph(
    nestedNavController: NavHostController,
    mainNavController: NavHostController,
    mainViewModel: MainViewModel
) {
    NavHost(
        navController = nestedNavController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(mainNavController, mainViewModel)
        }
        composable("profiles") {
            ProfilesScreen(mainNavController)
        }
        composable("history") {
            HistoryScreen(mainNavController)
        }
        composable("analytics") {
            AnalyticsScreen(mainNavController)
        }
    }
}