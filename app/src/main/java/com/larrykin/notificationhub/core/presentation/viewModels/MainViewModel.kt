package com.larrykin.notificationhub.core.presentation.viewModels

import android.content.Context
import android.provider.Settings
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.core.content.edit

class MainViewModel : ViewModel() {

    private var applicationContext: Context? = null

    private val _showPermissionDialog = MutableStateFlow(false)
    val showPermissionDialog: StateFlow<Boolean> = _showPermissionDialog

    private val _hasNotificationAccess = MutableStateFlow(false)
    val hasNotificationAccess: StateFlow<Boolean> = _hasNotificationAccess

    private val _checkingForPermission = MutableStateFlow(false)
    val checkingForPermission: StateFlow<Boolean> = _checkingForPermission

    init {
        // Show permission dialog after delay
        viewModelScope.launch {
            delay(12000)
            _showPermissionDialog.value = true
        }
    }

    fun checkNotificationPermission(context: Context) {
        applicationContext = context.applicationContext
        _hasNotificationAccess.value = isNotificationListenerEnabled(context)
    }

    fun startCheckingPermission() {
        _checkingForPermission.value = true
        viewModelScope.launch {
            while (_checkingForPermission.value && !_hasNotificationAccess.value) {
                delay(1000)
            }
            _checkingForPermission.value = false
        }
    }

    fun stopCheckingPermission() {
        _checkingForPermission.value = false
    }
    fun showPermissionDialog() {
        _showPermissionDialog.value = true
    }

    private fun isNotificationListenerEnabled(context: Context): Boolean {
        val packageName = context.packageName
        val flat =
            Settings.Secure.getString(context.contentResolver, "enabled_notification_listeners")
        return flat?.contains(packageName) == true
    }

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

    private fun getDismissalCount(): Int {
        // using SharedPreferences:
        val context = applicationContext ?: return 0
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("dismissal_count", 0)
    }

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

    /*//insettings
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
}*/
}