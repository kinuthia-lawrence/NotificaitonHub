package com.larrykin.notificationhub.core.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.larrykin.notificationhub.core.presentation.components.AppListItem
import com.larrykin.notificationhub.core.presentation.components.PermissionBanner
import com.larrykin.notificationhub.core.presentation.viewModels.MainViewModel
import com.larrykin.notificationhub.ui.theme.purpleColor

@Composable
fun HomeScreen(navController: NavController, viewModel: MainViewModel) {
    val installedApps by viewModel.installedApps.collectAsState(initial = emptyList())
    val loading by viewModel.loadingApps.collectAsState(initial = true)

    Column {
        PermissionBanner(viewModel)
        if (loading) {
            LoadingIdicator()
        } else {
            LazyColumn {
                items(installedApps) { app ->
                    AppListItem(
                        app = app,
                        onClick = { navController.navigate("appDetails/${app.packageName}") }
                    )

                }
            }
        }
    }
}

@Composable
fun LoadingIdicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = purpleColor)
    }
}

