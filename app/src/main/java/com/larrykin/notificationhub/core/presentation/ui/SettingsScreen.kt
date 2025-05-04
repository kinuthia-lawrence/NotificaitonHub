package com.larrykin.notificationhub.core.presentation.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.larrykin.notificationhub.core.presentation.components.ComingSoonScreen
import com.larrykin.notificationhub.core.presentation.viewModels.SettingsViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun SettingsScreen(navController: NavController) {
    val viewModel: SettingsViewModel = getViewModel()
    ComingSoonScreen()
//    LazyColumn {
//        item {
//            SettingsHeader("Notification Access")
//            NotificationAccessSetting(
//                hasAccess = viewModel.hasNotificationAccess,
//                onClick = { viewModel.requestNotificationAccess() }
//            )
//        }
//
//        item {
//            SettingsHeader("Global Settings")
//            SwitchSetting(
//                title = "Enable All Notifications",
//                checked = viewModel.globalNotificationsEnabled,
//                onCheckedChange = { viewModel.setGlobalNotificationsEnabled(it) }
//            )
//
//            SliderSetting(
//                title = "Default Volume",
//                value = viewModel.defaultVolume,
//                onValueChange = { viewModel.setDefaultVolume(it) },
//                range = 0f..100f
//            )
//
//            DropdownSetting(
//                title = "Default Priority",
//                selected = viewModel.defaultPriority,
//                options = (0..5).toList(),
//                onSelect = { viewModel.setDefaultPriority(it) }
//            )
//
//            SwitchSetting(
//                title = "Bypass DND by Default",
//                checked = viewModel.defaultBypassDnd,
//                onCheckedChange = { viewModel.setDefaultBypassDnd(it) }
//            )
//        }
//
//        item {
//            SettingsHeader("App Theme")
//            // Theme settings
//        }
//    }
}