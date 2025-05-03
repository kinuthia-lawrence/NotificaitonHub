package com.larrykin.notificationhub.core.presentation.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.larrykin.notificationhub.core.presentation.components.ComingSoonScreen

@Composable
fun HistoryScreen(navController: NavController) {
    ComingSoonScreen(
        featureName = "History",
        message = "We're building powerful notification history to help you manage your notifications. Stay tuned!"
    )
}