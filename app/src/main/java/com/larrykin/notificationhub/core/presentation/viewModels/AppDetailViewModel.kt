package com.larrykin.notificationhub.core.presentation.viewModels

import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.larrykin.notificationhub.core.domain.model.AppInfoDetails
import com.larrykin.notificationhub.core.domain.repository.INotificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AppDetailViewModel(
    private val packageName: String,
    private val repository: INotificationRepository
) : ViewModel() {
    private val _appInfo = MutableStateFlow(AppInfoDetails())
    val appInfo: StateFlow<AppInfoDetails> = _appInfo

    init {
        loadAppInfo()
    }

    private fun loadAppInfo() {
        viewModelScope.launch {
            repository.getAppSettings(packageName)?.let { settings ->
                _appInfo.value = AppInfoDetails(
                    name = settings.appName,
                    iconUri = "android.resource://$packageName/mipmap/ic_launcher".toUri(),
                    notificationsEnabled = settings.isEnabled,
                    soundEnabled = settings.isEnabled // TODO: add sound enabled to the entity
                )

            }
        }
    }

    fun setNotificationEnabled(enabled: Boolean) {
        viewModelScope.launch {
            repository.getAppSettings(packageName)?.let { currentSettings ->
                val updatedSettings = currentSettings.copy(isEnabled = enabled)
                repository.saveAppSettings(updatedSettings)
                _appInfo.value = _appInfo.value.copy(notificationsEnabled = enabled)
            }
        }
    }

    fun setSoundEnabled(enabled: Boolean) {
        viewModelScope.launch {
            repository.getAppSettings(packageName)?.let { currentSettings ->
                val updatedSettings = currentSettings.copy(isEnabled = enabled)
                repository.saveAppSettings(updatedSettings)
                _appInfo.value = _appInfo.value.copy(soundEnabled = enabled)
            }
        }
    }
}