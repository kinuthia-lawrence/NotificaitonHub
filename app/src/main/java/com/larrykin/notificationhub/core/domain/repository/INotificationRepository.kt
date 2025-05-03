package com.larrykin.notificationhub.core.domain.repository

import android.app.Notification
import android.content.pm.PackageManager
import com.larrykin.notificationhub.core.data.entities.AppNotificationSettings
import com.larrykin.notificationhub.core.data.entities.NotificationHistoryItem
import com.larrykin.notificationhub.core.domain.model.AppInfoDetails
import kotlinx.coroutines.flow.Flow

interface INotificationRepository {
    suspend fun getAppSettings(packageName: String): AppNotificationSettings?
    fun getAllAppSettings(): Flow<List<AppNotificationSettings>>
    suspend fun saveAppSettings(appNotificationSettings: AppNotificationSettings)
    suspend fun addToHistory(packageName: String, notification: Notification?)
    fun getNotificationHistory(): Flow<List<NotificationHistoryItem>>
    fun activateProfile(profileId: kotlin.Long)
    fun getInstalledApps(packageManager: PackageManager?): Flow<List<AppInfoDetails>>
}