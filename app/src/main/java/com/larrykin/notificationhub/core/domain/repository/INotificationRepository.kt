package com.larrykin.notificationhub.core.domain.repository

import android.app.Notification
import com.larrykin.notificationhub.core.data.entities.AppNotificationSettings
import com.larrykin.notificationhub.core.data.entities.NotificationHistoryItem
import kotlinx.coroutines.flow.Flow

interface INotificationRepository {
    suspend fun getAppSettings(packageName : String): AppNotificationSettings?
    fun getAllAppSettings(): Flow<List<AppNotificationSettings>>
    suspend fun saveAppSettings(appNotificationSettings: AppNotificationSettings)
    suspend fun addToHistory(packageName: String, notification: Notification?)
    fun getNotificationHistory(): Flow<List<NotificationHistoryItem>>
}