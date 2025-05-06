package com.larrykin.notificationhub.core.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.larrykin.notificationhub.core.data.entities.NotificationProfile
import com.larrykin.notificationhub.core.domain.model.AppInfoDetails
import com.larrykin.notificationhub.core.presentation.viewModels.MainViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun AppDetailScreen(
    packageName: String,
    navController: NavController
) {
    val viewModel: MainViewModel = getViewModel()
    val installedApps by viewModel.installedApps.collectAsState()

    val appInfo = installedApps.find { app ->
        app.packageName.equals(packageName, ignoreCase = true)
    } ?: AppInfoDetails()

    LaunchedEffect(packageName) {
        viewModel.initializeAppSettings(packageName, appInfo.name)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Go back"
                )
            }

            Text(
                text = "App Details",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 8.dp),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            //* App header card
            item {
                androidx.compose.material3.Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = androidx.compose.material3.CardDefaults.cardElevation(4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        AsyncImage(
                            model = appInfo.iconUri,
                            contentDescription = "${appInfo.name} icon",
                            modifier = Modifier
                                .size(64.dp)
                                .background(Color.White, RoundedCornerShape(8.dp))
                                .padding(4.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = appInfo.name,
                                style = MaterialTheme.typography.headlineSmall
                            )
                        }
                    }
                }
            }

            //* Basic Settings section
            item {
                SettingsSection(title = "Basic Settings") {
                    SwitchSetting(
                        title = "Enable Notifications",
                        checked = appInfo.notificationsEnabled,
                        onCheckedChange = { viewModel.setNotificationEnabled(packageName, it) }
                    )

                    SwitchSetting(
                        title = "Bypass Do Not Disturb",
                        checked = appInfo.bypassDnD,
                        onCheckedChange = { viewModel.setBypassDnD(packageName, it) }
                    )
                }
            }

            //* Sound & Vibration section
            item {
                SettingsSection(title = "Sound & Vibration") {
                    SwitchSetting(
                        title = "Enable Sound",
                        checked = appInfo.soundEnabled,
                        onCheckedChange = { viewModel.setSoundEnabled(packageName, it) }
                    )

                    SliderSetting(
                        title = "Volume Level",
                        value = appInfo.volumeLevel.toFloat(),
                        onValueChange = { viewModel.setVolumeLevel(packageName, it.toInt()) },
                        range = 0f..100f,
                        enabled = appInfo.soundEnabled // Only enable if sound is enabled
                    )

                    RingtoneSetting(
                        title = "Custom Sound",
                        selected = appInfo.customRingtonePath,
                        onSelect = { viewModel.setCustomRingtone(packageName, it) },
                        enabled = appInfo.soundEnabled // Only enable if sound is enabled
                    )

                    VibrationSetting(
                        title = "Vibration Pattern",
                        selected = appInfo.vibrationPattern,
                        onSelect = { viewModel.setVibrationPattern(packageName, it) }
                    )
                }
            }

            // Visual Settings section
            item {
                SettingsSection(title = "Visual Settings") {
                    ColorSetting(
                        title = "LED Color",
                        color = appInfo.ledColor?.let { Color(it) } ?: Color.Transparent,
                        onColorSelected = { viewModel.setLedColor(packageName, it) }
                    )
                }
            }

            // Advanced section
            item {
                SettingsSection(title = "Advanced") {
                    DropdownSetting(
                        title = "Priority Level",
                        selected = appInfo.priority,
                        options = (0..5).toList(),
                        onSelect = { viewModel.setPriority(packageName, it) }
                    )

                    ProfileSelector(
                        profiles = viewModel.availableProfiles,
                        selectedProfileId = appInfo.notificationProfileId,
                        onProfileSelected = { viewModel.setProfile(packageName, it) }
                    )
                }
            }

            // Add some padding at the bottom
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun SettingsSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        androidx.compose.material3.Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            elevation = androidx.compose.material3.CardDefaults.cardElevation(2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                content()
            }
        }
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
        Text(text = title, style = MaterialTheme.typography.bodyLarge)
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
fun SliderSetting(
    title: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    range: ClosedFloatingPointRange<Float>,
    enabled: Boolean = true
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = if (enabled) MaterialTheme.colorScheme.onSurface
            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
        )
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = range,
            modifier = Modifier.padding(top = 8.dp),
            enabled = enabled
        )
    }
}

@Composable
fun DropdownSetting(
    title: String,
    selected: Int,
    options: List<Int>,
    onSelect: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = title, style = MaterialTheme.typography.bodyLarge)
        Box {
            OutlinedButton(
                onClick = { expanded = true },
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Text("Priority: $selected")
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text("$option") },
                        onClick = {
                            onSelect(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun RingtoneSetting(
    title: String,
    selected: String?,
    onSelect: (String?) -> Unit,
    enabled: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }
    val ringtones = listOf("Default", "Chime", "Chirp", "Ding", "None")

    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = if (enabled) MaterialTheme.colorScheme.onSurface
            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.038f)
        )
        Box {
            OutlinedButton(
                onClick = { expanded = true },
                modifier = Modifier.padding(top = 4.dp),
                enabled = enabled
            ) {
                Text(selected ?: "Default Sound")
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                ringtones.forEach { ringtone ->
                    DropdownMenuItem(
                        text = { Text(ringtone) },
                        onClick = {
                            onSelect(if (ringtone == "Default") null else ringtone)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun VibrationSetting(
    title: String,
    selected: String?,
    onSelect: (String?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val patterns = listOf("Default", "Short", "Long", "SOS", "None")

    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = title, style = MaterialTheme.typography.bodyLarge)
        Box {
            OutlinedButton(
                onClick = { expanded = true },
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Text(selected ?: "Default Pattern")
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                patterns.forEach { pattern ->
                    DropdownMenuItem(
                        text = { Text(pattern) },
                        onClick = {
                            onSelect(if (pattern == "Default") null else pattern)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ColorSetting(
    title: String,
    color: androidx.compose.ui.graphics.Color,
    onColorSelected: (Int?) -> Unit
) {
    val colors = listOf(
        "Red" to androidx.compose.ui.graphics.Color.Red,
        "Green" to androidx.compose.ui.graphics.Color.Green,
        "Blue" to androidx.compose.ui.graphics.Color.Blue,
        "Yellow" to androidx.compose.ui.graphics.Color.Yellow,
        "Magenta" to androidx.compose.ui.graphics.Color.Magenta,
        "Cyan" to androidx.compose.ui.graphics.Color.Cyan,
        "None" to androidx.compose.ui.graphics.Color.Transparent
    )

    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = title, style = MaterialTheme.typography.bodyLarge)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .padding(end = 8.dp)
                    .background(color)
            )
            OutlinedButton(onClick = { expanded = true }) {
                Text("Select Color")
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                colors.forEach { (name, colorValue) ->
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .background(colorValue)
                                        .padding(end = 8.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(name)
                            }
                        },
                        onClick = {
                            onColorSelected(if (name == "None") null else colorValue.toArgb())
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

data class NotificationProfile(val id: Long, val name: String)

@Composable
fun ProfileSelector(
    profiles: MutableStateFlow<List<NotificationProfile>>,
    selectedProfileId: Long?,
    onProfileSelected: (Long?) -> Unit
) {
    val availableProfiles by profiles.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = "Notification Profile", style = MaterialTheme.typography.bodyLarge)
        Box {
            OutlinedButton(
                onClick = { expanded = true },
                modifier = Modifier.padding(top = 4.dp)
            ) {
                val selectedName =
                    availableProfiles.find { it.id == selectedProfileId }?.name ?: "Default"
                Text("Profile: $selectedName")
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Default") },
                    onClick = {
                        onProfileSelected(null)
                        expanded = false
                    }
                )
                availableProfiles.forEach { profile ->
                    DropdownMenuItem(
                        text = { Text(profile.name) },
                        onClick = {
                            onProfileSelected(profile.id)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun AppDetailScreenPreview() {
    val appInfo = AppInfoDetails(
        name = "Sample App",
        packageName = "com.example.sampleapp",
        iconUri = "https://example.com/icon.png".toUri(),
        notificationsEnabled = true,
        bypassDnD = false,
        volumeLevel = 50,
        customRingtonePath = null,
        vibrationPattern = null,
        ledColor = null,
        priority = 3,
        notificationProfileId = null
    )

    AppDetailScreen(
        packageName = appInfo.packageName,
        navController = NavController(context = LocalContext.current)
    )
}
