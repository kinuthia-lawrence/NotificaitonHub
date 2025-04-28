package com.larrykin.notificationhub.core.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.larrykin.notificationhub.core.presentation.viewModels.MainViewModel
import com.larrykin.notificationhub.ui.theme.lightPurpleColor
import com.larrykin.notificationhub.ui.theme.purpleColor

@Composable
fun PermissionBanner(viewModel: MainViewModel) {
    val hasAccess by viewModel.hasNotificationAccess.collectAsState()

    AnimatedVisibility(visible = !hasAccess) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(lightPurpleColor.copy(alpha = 0.1f))
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = null,
                tint = purpleColor
            )
            Text("Enable notifications for full features")
            Spacer(Modifier.weight(1f))
            TextButton(onClick = {
                viewModel.showPermissionDialog()
                viewModel.startCheckingPermission()
            }) {
                Text("Enable")
            }
        }
    }
}