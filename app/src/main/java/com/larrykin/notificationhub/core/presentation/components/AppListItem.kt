package com.larrykin.notificationhub.core.presentation.components

import android.graphics.pdf.models.ListItem
import android.health.connect.datatypes.AppInfo
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppListItem(app: AppInfo, onClick: () -> Unit) {
    ListItem(
        leadingContent = {
            AsyncImage(
                model = app.iconUri,
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )
        },
        headlineContent = {
            Text(app.name)
        },
        trailingContent = {
            Switch(
                checked = app.notificationsEnabled,
                onCheckedChange = null
            )
        },
        modifier = Modifier.clickable(onClick = onClick)
    )
}