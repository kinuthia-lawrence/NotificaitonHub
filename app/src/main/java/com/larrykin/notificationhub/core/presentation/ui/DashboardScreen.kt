package com.larrykin.notificationhub.core.presentation.ui

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.larrykin.notificationhub.core.navigation.DashboardNavGraph
import com.larrykin.notificationhub.core.presentation.viewModels.MainViewModel

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 13)
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

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 13)
@Preview
@Composable
fun PreviewDashboardScreen() {
    val mainNavController = rememberNavController()
    val mainViewModel = MainViewModel(TODO())

    DashboardScreen(
        mainNavController = mainNavController,
        mainViewModel = mainViewModel
    )
}
