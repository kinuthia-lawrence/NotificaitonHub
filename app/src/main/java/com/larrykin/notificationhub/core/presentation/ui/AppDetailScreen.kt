package com.larrykin.notificationhub.core.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.larrykin.notificationhub.core.presentation.viewModels.AppDetailViewModel
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun AppDetailScreen(packageName: String, navController: NavController) {
    val viewModel: AppDetailViewModel = getViewModel { parametersOf(packageName) }
    val appInfo by viewModel.appInfo.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = appInfo.iconUri,
                contentDescription = null,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(appInfo.name, style = MaterialTheme.typography.headlineMedium)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Notification Settings
        SwitchSetting(
            title = "Enable Notifications",
            checked = appInfo.notificationsEnabled,
            onCheckedChange = { viewModel.setNotificationEnabled(it) }
        )

        // Sound settings
        SwitchSetting(
            title = "Play Sound",
            checked = appInfo.soundEnabled,
            onCheckedChange = { viewModel.setSoundEnabled(it) }
        )
    }
}

@Composable
fun SwitchSetting(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, style = MaterialTheme.typography.bodyLarge)
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}