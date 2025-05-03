package com.larrykin.notificationhub.core.presentation.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.larrykin.notificationhub.core.presentation.components.ComingSoonScreen

@Composable
fun ProfilesScreen(navController: NavController) {
    ComingSoonScreen(
        featureName = "Profiles",
        message = "We're building powerful notification profiles to help you manage your notifications. Stay tuned!"
    )
}