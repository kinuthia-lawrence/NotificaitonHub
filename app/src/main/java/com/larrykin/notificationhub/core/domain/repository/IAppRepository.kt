package com.larrykin.notificationhub.core.domain.repository

import com.larrykin.notificationhub.core.data.entities.NotificationProfile
import kotlinx.coroutines.flow.Flow

interface IAppRepository {
    suspend fun getAllProfiles(): Flow<List<NotificationProfile>>
    suspend fun getDefaultProfile(): NotificationProfile?
    suspend fun createProfile(profile: NotificationProfile): Long
    suspend fun updateProfile(profile: NotificationProfile)
    suspend fun deleteProfile(profile: NotificationProfile)
    suspend fun activateProfile(profileId: Long)
}