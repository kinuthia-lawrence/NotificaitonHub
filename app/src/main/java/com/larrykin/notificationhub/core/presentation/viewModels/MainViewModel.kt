package com.larrykin.notificationhub.core.presentation.viewModels

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import androidx.core.content.edit
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.larrykin.notificationhub.core.data.entities.NotificationProfile
import com.larrykin.notificationhub.core.domain.model.AppInfoDetails
import com.larrykin.notificationhub.core.domain.repository.INotificationRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainViewModel(application: Application) : KoinComponent,
    AndroidViewModel(application) {

    private val repository: INotificationRepository by inject()


    // StateFlow to manage the visibility of the permission dialog
    private val _showPermissionDialog = MutableStateFlow(false)
    val showPermissionDialog: StateFlow<Boolean> = _showPermissionDialog

    // StateFlow to manage the notification access permission status
    private val _hasNotificationAccess = MutableStateFlow(false)
    val hasNotificationAccess: StateFlow<Boolean> = _hasNotificationAccess

    // StateFlow to manage the checking for permission status
    private val _checkingForPermission = MutableStateFlow(false)
    val checkingForPermission: StateFlow<Boolean> = _checkingForPermission

    // StateFlow to manage installed apps
    private val _installedApps = MutableStateFlow<List<AppInfoDetails>>(emptyList())
    val installedApps: StateFlow<List<AppInfoDetails>> = _installedApps

    // StateFlow to manage loading
    private val _loadingApps = MutableStateFlow(false)
    val loadingApps: StateFlow<Boolean> = _loadingApps

    //StateFlow for search functionality
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    /**
     * initializes the ViewModel and starts checking for notification access permission.
     * */
    init {
        viewModelScope.launch {
            loadInstalledApps()
            Log.d("MainViewModel", "Installed apps loaded")
        }
        // Show permission dialog after delay
        viewModelScope.launch {
            delay(12000)
            _showPermissionDialog.value = true
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    private fun loadInstalledApps() {
        viewModelScope.launch {
            _loadingApps.value = true

            repository.getInstalledApps(getApplication<Application>().packageManager)
                .collect { appDetails ->
                    _installedApps.value = appDetails
//                    Log.d("MainViewModel", "Installed apps: $appDetails")
                    _loadingApps.value = false
                }
        }
    }

    // Check if the app has notification access permission
    fun checkNotificationPermission() {
        _hasNotificationAccess.value = isNotificationListenerEnabled()
    }

    // Check if the app has notification access permission
    fun startCheckingPermission() {
        _checkingForPermission.value = true
        viewModelScope.launch {
            while (_checkingForPermission.value && !_hasNotificationAccess.value) {
                delay(1000)
            }
            _checkingForPermission.value = false
        }
    }

    // Stop checking for notification access permission
    fun stopCheckingPermission() {
        _checkingForPermission.value = false
    }

    // Show the permission dialog
    fun showPermissionDialog() {
        _showPermissionDialog.value = true
    }

    // Check if the app has notification access permission
    private fun isNotificationListenerEnabled(): Boolean {
        val context = getApplication<Application>()
        val packageName = context.packageName
        val flat =
            Settings.Secure.getString(context.contentResolver, "enabled_notification_listeners")
        return flat?.contains(packageName) == true
    }

    // Dismiss the permission dialog and set a backoff time
    fun dismissPermissionDialog() {
        _showPermissionDialog.value = false

        // Track dismissal count in SharedPreferences or DataStore
        val dismissCount = getDismissalCount() + 1
        saveDismissalCount(dismissCount)

        // Calculate backoff time (e.g., 30s, 2min, 10min, 1hr...)
        val delayTime = when (dismissCount) {
            1 -> 30_000L     // 30 seconds
            2 -> 120_000L    // 2 minutes
            3 -> 600_000L    // 10 minutes
            else -> 3_600_000L // 1 hour
        }

        viewModelScope.launch {
            delay(delayTime)
            _showPermissionDialog.value = true
        }
    }

    // Get the dismissal count from SharedPreferences or DataStore
    private fun getDismissalCount(): Int {
        // using SharedPreferences:
        val context = getApplication<Application>()
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("dismissal_count", 0)
    }

    // Save the dismissal count to SharedPreferences or DataStore
    private fun saveDismissalCount(count: Int) {
        val context = getApplication<Application>()
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit { putInt("dismissal_count", count) }
    }

    //! FOR Updating App Details
    fun setNotificationEnabled(packageName: String, enabled: Boolean) {
        viewModelScope.launch {
            repository.getAppSettings(packageName)?.let { currentSettings ->
                val updatedSettings = currentSettings.copy(isEnabled = enabled)
                repository.saveAppSettings(updatedSettings)
                _installedApps.value = _installedApps.value.map { app ->
                    if (app.packageName == packageName) {
                        app.copy(notificationsEnabled = enabled)
                    } else {
                        app
                    }
                }
            }
        }
    }

    fun setSoundEnabled(packageName: String, enabled: Boolean) {
        viewModelScope.launch {
            repository.getAppSettings(packageName)?.let { currentSettings ->
                val updatedSettings =
                    currentSettings.copy(isEnabled = enabled) // Changed to soundEnabled
                repository.saveAppSettings(updatedSettings)
                _installedApps.value = _installedApps.value.map { app ->
                    if (app.packageName == packageName) {
                        app.copy(soundEnabled = enabled) // Update correct property
                    } else {
                        app
                    }
                }
            }
        }
    }

    fun setVolumeLevel(packageName: String, level: Int) {
        viewModelScope.launch {
            repository.getAppSettings(packageName)?.let { currentSettings ->
                val updatedSettings = currentSettings.copy(volumeLevel = level)
                repository.saveAppSettings(updatedSettings)
                _installedApps.value = _installedApps.value.map { app ->
                    if (app.packageName == packageName) {
                        app.copy(volumeLevel = level)
                    } else {
                        app
                    }
                }
            }
        }
    }

    fun setCustomRingtone(packageName: String, path: String?) {
        viewModelScope.launch {
            repository.getAppSettings(packageName)?.let { currentSettings ->
                val updatedSettings = currentSettings.copy(customRingtonePath = path)
                repository.saveAppSettings(updatedSettings)
                _installedApps.value = _installedApps.value.map { app ->
                    if (app.packageName == packageName) {
                        app.copy(customRingtonePath = path)
                    } else {
                        app
                    }
                }
            }
        }
    }

    fun setVibrationPattern(packageName: String, pattern: String?) {
        viewModelScope.launch {
            repository.getAppSettings(packageName)?.let { currentSettings ->
                val updatedSettings = currentSettings.copy(vibrationPattern = pattern)
                repository.saveAppSettings(updatedSettings)
                _installedApps.value = _installedApps.value.map { app ->
                    if (app.packageName == packageName) {
                        app.copy(vibrationPattern = pattern)
                    } else {
                        app
                    }
                }
            }
        }
    }

    fun setLedColor(packageName: String, color: Int?) {
        viewModelScope.launch {
            repository.getAppSettings(packageName)?.let { currentSettings ->
                val updatedSettings = currentSettings.copy(ledColor = color)
                repository.saveAppSettings(updatedSettings)
                _installedApps.value = _installedApps.value.map { app ->
                    if (app.packageName == packageName) {
                        app.copy(ledColor = color)
                    } else {
                        app
                    }
                }
            }
        }
    }

    fun setBypassDnD(packageName: String, bypass: Boolean) {
        viewModelScope.launch {
            repository.getAppSettings(packageName)?.let { currentSettings ->
                val updatedSettings = currentSettings.copy(bypassDnD = bypass)
                repository.saveAppSettings(updatedSettings)
                _installedApps.value = _installedApps.value.map { app ->
                    if (app.packageName == packageName) {
                        app.copy(bypassDnD = bypass)
                    } else {
                        app
                    }
                }
            }
        }
    }

    fun setPriority(packageName: String, priority: Int) {
        viewModelScope.launch {
            repository.getAppSettings(packageName)?.let { currentSettings ->
                val updatedSettings = currentSettings.copy(priority = priority)
                repository.saveAppSettings(updatedSettings)
                _installedApps.value = _installedApps.value.map { app ->
                    if (app.packageName == packageName) {
                        app.copy(priority = priority)
                    } else {
                        app
                    }
                }
            }
        }
    }

    val availableProfiles = MutableStateFlow<List<NotificationProfile>>(emptyList())

    fun setProfile(packageName: String, profileId: Long?) {
        viewModelScope.launch {
            repository.getAppSettings(packageName)?.let { currentSettings ->
                val updatedSettings = currentSettings.copy(notificationProfileId = profileId)
                repository.saveAppSettings(updatedSettings)
                _installedApps.value = _installedApps.value.map { app ->
                    if (app.packageName == packageName) {
                        app.copy(notificationProfileId = profileId)
                    } else {
                        app
                    }
                }
            }
        }
    }

    //for a feature
    fun useFeatureRequiringPermission(): Boolean {
        if (!_hasNotificationAccess.value) {
            _showPermissionDialog.value = true
            return false
        }
        return true
    }

    // for battery optimization
    fun checkBatteryOptimization(): Boolean {
        val context = getApplication<Application>()

        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        return powerManager.isIgnoringBatteryOptimizations(context.packageName)
    }

    fun requestBatteryOptimizationExemption() {
        val context = getApplication<Application>()
        val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
            data = "package: ${context.packageName}".toUri()
        }
        context.startActivity(intent)
    }

}