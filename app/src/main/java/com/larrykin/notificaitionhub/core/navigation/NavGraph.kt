package com.larrykin.notificaitionhub.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.larrykin.notificaitionhub.core.presentation.ui.AppDetailScreen
import com.larrykin.notificaitionhub.core.presentation.ui.HistoryScreen
import com.larrykin.notificaitionhub.core.presentation.ui.HomeScreen
import com.larrykin.notificaitionhub.core.presentation.ui.ProfilesScreen
import com.larrykin.notificaitionhub.core.presentation.ui.SettingsScreen
import com.larrykin.notificaitionhub.core.presentation.ui.SplashScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable(route = "splash") {
            SplashScreen(navController)
        }
        composable(route = "home") {
            HomeScreen(navController)
        }
        composable(route = "profiles") {
            ProfilesScreen(navController)
        }
        composable(route = "history") {
            HistoryScreen(navController)
        }
        composable(route = "settings") {
            SettingsScreen(navController)
        }
        composable(route = "appDetails/{packageName}") { backStackEntry ->
            val packageName = backStackEntry.arguments?.getString("packageName")
            packageName?.let {
                AppDetailScreen(packageName, navController)
            }
        }
    }
}