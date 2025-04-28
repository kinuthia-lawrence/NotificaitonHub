package com.larrykin.notificationhub.core.presentation.viewModels

import android.net.Uri
import com.larrykin.notificationhub.core.domain.model.AppInfoDetails
import com.larrykin.notificationhub.core.domain.repository.INotificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AppDetailViewModel(
    private val packageName: String,
    private val repository: INotificationRepository
) {
    private val _appInfo = MutableStateFlow(AppInfoDetails())
    val appInfo: StateFlow<AppInfoDetails> = _appInfo

    init {
        loadAppInfo()
    }

    private fun loadAppInfo() {
        viewModelScope.launch {
            repository.getAppSettings(packageName)?.let { settings ->
                _appInfo.value = AppInfoDetails(
                    name = settings.appName ?: packageName,
                    iconUri = Uri.parse("android.resource:// $packageName/mipmap/ic_launcher"),
                    notificationsEnabled = settings.enabled,
                    soundEnabled = settings.soundEnabled
                )

            }
        }
    }

    fun setNotificationEnabled(enabled: Boolean) {
        viewModelScope.launch {
            repository.updateAppSettings(packageName, enabled = enabled)
            _appInfo.value = _appInfo.value.copy(setNotificationEnabled = enabled)
        }
    }

    fun setSoundEnabled(enabled: Boolean) {
        viewModelScope.launch {
            repository.updateAppSettings(packageName, soundEnabled = enabled)
            _appInfo.value = _appInfo.value.copy(soundEnabled = enabled)
        }
    }
}