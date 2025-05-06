package com.larrykin.notificationhub.core.presentation.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.larrykin.notificationhub.core.data.entities.NotificationProfile
import com.larrykin.notificationhub.core.domain.repository.IAppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repository: IAppRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _profiles = MutableStateFlow<List<NotificationProfile>>(emptyList())
    val profiles: StateFlow<List<NotificationProfile>> = _profiles

    private val _activeProfileId = MutableStateFlow<Long?>(null)
    val activeProfileId: StateFlow<Long?> = _activeProfileId

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        viewModelScope.launch {
            repository.getAllProfiles().collect { profilesList ->
                _profiles.value = profilesList
                val defaultProfile = profilesList.find { it.isDefault }
                _activeProfileId.value = defaultProfile?.id
            }
        }
    }

    fun createProfile(name: String, description: String?, color: Int?, iconResId: Int?) {
        viewModelScope.launch {
            _isLoading.value = true
            val profile = NotificationProfile(
                name = name,
                description = description,
                color = color,
                iconResId = iconResId
            )
            val profileId = repository.createProfile(profile)
            _isLoading.value = false
        }
    }

    fun updateProfile(profile: NotificationProfile) {
        viewModelScope.launch {
            repository.updateProfile(profile)
        }
    }

    fun deleteProfile(profile: NotificationProfile) {
        viewModelScope.launch {
            repository.deleteProfile(profile)
        }
    }

    fun activateProfile(profileId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.activateProfile(profileId)
            _activeProfileId.value = profileId
            _isLoading.value = false
        }
    }
}