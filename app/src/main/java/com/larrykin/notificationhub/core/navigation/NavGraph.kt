package com.larrykin.notificationhub.core.navigation

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.larrykin.notificationhub.core.presentation.ui.AppDetailScreen
import com.larrykin.notificationhub.core.presentation.ui.DashboardScreen
import com.larrykin.notificationhub.core.presentation.ui.HistoryScreen
import com.larrykin.notificationhub.core.presentation.ui.HomeScreen
import com.larrykin.notificationhub.core.presentation.ui.ProfilesScreen
import com.larrykin.notificationhub.core.presentation.ui.SettingsScreen
import com.larrykin.notificationhub.core.presentation.ui.SplashScreen
import com.larrykin.notificationhub.core.presentation.viewModels.MainViewModel

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 13)
@Composable
fun NavGraph(navController: NavHostController, viewModel: MainViewModel) {
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable(route = "splash") {
            SplashScreen(navController)
        }
        composable(route = "dashboard") {
            DashboardScreen(navController, viewModel)
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

