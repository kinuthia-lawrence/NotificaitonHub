package com.larrykin.notificationhub.core.presentation.ui

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    val searchQuery by viewModel.searchQuery.collectAsState()

    Column {
        // search bar to filter by name
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.onSearchQueryChanged(it) },
            label = { Text(text = "Search app by name") },
            shape = RoundedCornerShape(30.dp),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon"
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear search",
                        modifier = Modifier.clickable { viewModel.onSearchQueryChanged("") }
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        // permission banner
        PermissionBanner(viewModel)
        // check if the app has notification access permission
        if (loading) {
            LoadingIndicator()
        } else {
            //filter apps based on search query
            val filteredApps = if (searchQuery.isEmpty()) {
                installedApps
            } else {
                installedApps.filter { app ->
                    app.name.contains(searchQuery, ignoreCase = true)
                }
            }
            LazyColumn {
                items(filteredApps) { app ->
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

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 13)
@Preview
@Composable
fun HomeScreenPreview() {
    val navController = NavController(context = TODO())
    val viewModel = MainViewModel(
        application = TODO()
    )
    HomeScreen(navController, viewModel)
}
