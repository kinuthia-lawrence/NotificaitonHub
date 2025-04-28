package com.larrykin.notificationhub.core.presentation.ui

import android.content.Intent
import android.provider.Settings
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import com.larrykin.notificationhub.core.presentation.viewModels.MainViewModel

@Composable
fun SettingsScreen(navController: NavController) {

}

// This function is used to display the settings screen for the app.
@Composable
fun AppSettings(viewModel: MainViewModel, startActivity: (Intent) -> Unit) {
    val hasAccess by viewModel.hasNotificationAccess.collectAsState()

    SettingsItem(
        title = "Notification Access",
        description = if (hasAccess) "Enabled" else "Disabled",
        icon = Icons.Default.Notifications,
        enabled = !hasAccess,
        onClick = {
            if (!hasAccess) {
                startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
            }
        }
    )
}

@Composable
fun SettingsItem(
    title: String,
    description: String,
    icon: Notifications,
    enabled: not,
    onClick: () -> Unit
) {
    TODO("Not yet implemented")
}