package com.larrykin.notificationhub.core.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.larrykin.notificationhub.core.navigation.DashboardNavGraph
import com.larrykin.notificationhub.core.presentation.viewModels.MainViewModel

@Composable
fun DashboardScreen(
    mainNavController: NavHostController,
    mainViewModel: MainViewModel

) {
    val nestedNavController = rememberNavController()

    Scaffold(
        topBar = {
            DashboardTopAppBar()
        },
        bottomBar = {
            BottomNavigation(nestedNavController)
        }
    )
    { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            DashboardNavGraph(
                nestedNavController = nestedNavController,
                mainNavController = mainNavController,
                mainViewModel = mainViewModel
            )
        }
    }
}

