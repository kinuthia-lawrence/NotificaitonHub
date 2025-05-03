package com.larrykin.notificationhub.core.presentation.ui

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.larrykin.notificationhub.core.presentation.components.AppListItem
import com.larrykin.notificationhub.core.presentation.components.PermissionBanner
import com.larrykin.notificationhub.core.presentation.viewModels.MainViewModel
import com.larrykin.notificationhub.ui.theme.purpleColor

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 13)
@Composable
fun HomeScreen(navController: NavController, viewModel: MainViewModel) {
    val installedApps by viewModel.installedApps.collectAsState()
    val loading by viewModel.loadingApps.collectAsState()

    Column {
        PermissionBanner(viewModel)
        if (loading) {
            LoadingIndicator()
        } else {
            LazyColumn {
                items(installedApps) { app ->
                    AppListItem(
                        appInfoDetails = app,
                        onClick = {
                            navController.navigate("appDetails/${app.name}")
                        }
                    )

                }
            }
        }
    }
}

@Composable
fun LoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = purpleColor)
    }
}

