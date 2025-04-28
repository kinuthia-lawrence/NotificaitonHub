package com.larrykin.notificationhub.core.presentation.components

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ListItem
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.larrykin.notificationhub.core.domain.model.AppInfoDetails

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 13)
@Composable
fun AppListItem(appInfoDetails: AppInfoDetails, onClick: () -> Unit) {
    ListItem(
        headlineContent = {
            Text(appInfoDetails.name)
        },
        leadingContent = {
            AsyncImage(
                model = appInfoDetails.iconUri,
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )
        },
        trailingContent = {
            Switch(
                checked = appInfoDetails.notificationsEnabled,
                onCheckedChange = null
            )
        },
        modifier = Modifier.clickable(onClick = onClick)
    )
}