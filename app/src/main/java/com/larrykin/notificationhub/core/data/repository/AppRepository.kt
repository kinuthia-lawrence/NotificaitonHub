package com.larrykin.notificationhub.core.data.repository

import com.larrykin.notificationhub.core.data.dao.AppNotificationSettingsDao
import com.larrykin.notificationhub.core.data.dao.ProfileDao
import com.larrykin.notificationhub.core.data.entities.NotificationProfile
import com.larrykin.notificationhub.core.domain.repository.IAppRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class AppRepository(
    private val profileDao: ProfileDao,
    private val appNotificationDao: AppNotificationSettingsDao
) : IAppRepository {

    override suspend fun getAllProfiles(): Flow<List<NotificationProfile>> {
        return profileDao.getAllProfiles()
    }

    override suspend fun getDefaultProfile(): NotificationProfile? {
        return profileDao.getDefaultProfile()
    }

    override suspend fun createProfile(profile: NotificationProfile): Long {
        // Create the new profile
        val profileId = profileDao.insertProfile(profile)

        // Clone all app settings for this new profile
        val allAppSettings = appNotificationDao.getAllAppSettings().first()

        // Create initial settings for each app under this profile
        coroutineScope {
            allAppSettings.forEach { appSetting ->
                val newSetting = appSetting.copy(
                    notificationProfileId = profileId
                )
                appNotificationDao.insertOrUpdateAppSettings(newSetting)
            }
        }

        return profileId
    }

    override suspend fun updateProfile(profile: NotificationProfile) {
        // Ensure we're not removing the default flag from the default profile
        if (profile.isDefault) {
            // If this profile is being set as default, clear other defaults first
            profileDao.clearDefaultProfile()
        } else {
            // Check if this was previously the default profile
            val currentProfile = profileDao.getProfileById(profile.id)
            if (currentProfile?.isDefault == true) {
                // Don't allow removing default status - keep it as default
                profileDao.updateProfile(profile.copy(isDefault = true))
                return
            }
        }

        profileDao.updateProfile(profile)
    }

    override suspend fun deleteProfile(profile: NotificationProfile) {
        // Don't allow deleting the default profile
        if (profile.isDefault) {
            return
        }

        // Delete the profile
        profileDao.deleteProfile(profile)

        // Optionally cleanup app settings associated with this profile
        appNotificationDao.deleteSettingsForProfile(profile.id)
    }

    override suspend fun activateProfile(profileId: Long) {
        // Set this profile as the default
        profileDao.setDefaultProfile(profileId)

        // Update all app settings to use this profile's settings
        val allApps = appNotificationDao.getAllUniqueAppPackages()

        coroutineScope {
            allApps.forEach { packageName ->
                // Get current settings for this app in this profile
                val profileSettings =
                    appNotificationDao.getAppSettingsForPackageAndProfile(packageName, profileId)

                if (profileSettings != null) {
                    // This app already has settings for this profile, make them active
                    appNotificationDao.updateActiveAppSettings(profileSettings)
                } else {
                    // No settings for this app in this profile yet - create default ones
                    val defaultAppSettings = appNotificationDao.getDefaultAppSettings(packageName)
                    if (defaultAppSettings != null) {
                        // Copy from default settings but assign to this profile
                        val newSettings = defaultAppSettings.copy(notificationProfileId = profileId)
                        appNotificationDao.insertOrUpdateAppSettings(newSettings)
                    }
                }
            }
        }
    }
}