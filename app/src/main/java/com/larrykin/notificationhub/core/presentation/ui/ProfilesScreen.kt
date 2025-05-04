package com.larrykin.notificationhub.core.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.larrykin.notificationhub.core.presentation.components.ComingSoonScreen
import com.larrykin.notificationhub.core.presentation.viewModels.ProfileViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun ProfilesScreen(navController: NavController) {
    val viewModel: ProfileViewModel = getViewModel()
    Column {
        // Header
        Text("Notification Profiles", style = MaterialTheme.typography.headlineMedium)

        // Create new profile button
        Button(onClick = { /* Create profile */ }) {
            Text("Create New Profile")
        }

        ComingSoonScreen()
        // List of existing profiles
//        LazyColumn {
//            items(count = viewModel.profiles) { profile ->
//                ProfileItem(
//                    profile = profile,
//                    onEdit = { navController.navigate("editProfile/${profile.id}") },
//                    onActivate = { viewModel.activateProfile(profile.id) }
//                )
//            }
//        }
    }
}