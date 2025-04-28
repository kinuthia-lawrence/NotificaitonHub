package com.larrykin.notificationhub.core.presentation.viewModels

import android.adservices.ondevicepersonalization.AppInfo
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.PowerManager
import android.provider.Settings
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private var applicationContext: Context? = null

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
    private val _installedApps = MutableStateFlow<List<AppInfo>>(emptyList())
    val installedApps: StateFlow<List<AppInfo>> = _installedApps

    // StateFlow to manage loading
    private val _loadingApps = MutableStateFlow(false)
    val loadingApps: StateFlow<Boolean> = _loadingApps

    /**
     * initializes the ViewModel and starts checking for notification access permission.
     * */
    init {
        viewModelScope.launch {
            loadInstalledApps()
        }
        // Show permission dialog after delay
        viewModelScope.launch {
            delay(12000)
            _showPermissionDialog.value = true
        }
    }

    private fun loadInstalledApps() {
        viewModelScope.launch {
            _loadingApps.value = true
            // TODO: implement fetchig installed apps
//            _installedApps.value = repository.getInstalledApps()
            _installedApps.value = emptyList()
            _loadingApps.value = false

        }
    }

    // Check if the app has notification access permission
    fun checkNotificationPermission(context: Context) {
        applicationContext = context.applicationContext
        _hasNotificationAccess.value = isNotificationListenerEnabled(context)
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
    private fun isNotificationListenerEnabled(context: Context): Boolean {
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
        val context = applicationContext ?: return 0
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("dismissal_count", 0)
    }

    // Save the dismissal count to SharedPreferences or DataStore
    private fun saveDismissalCount(count: Int) {
        val context = applicationContext ?: return
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit() { putInt("dismissal_count", count) }
    }

    //for a feature
    fun useFeatureRequiringPermission(context: Context): Boolean {
        if (!_hasNotificationAccess.value) {
            _showPermissionDialog.value = true
            return false
        }
        return true
    }

    // for battery optimization
    fun checkBatteryOptimization(context: Context): Boolean {
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        return powerManager.isIgnoringBatteryOptimizations(context.packageName)
    }

    fun requestBatteryOptimizationExemption(context: Context) {
        val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
            data = Uri.parse("package: ${context.packageName}")
        }
        context.startActivity(intent)
    }
}