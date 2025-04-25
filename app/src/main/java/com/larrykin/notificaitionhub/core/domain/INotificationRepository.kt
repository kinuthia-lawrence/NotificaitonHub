package com.larrykin.notificaitionhub.core.domain

import android.app.Notification
import com.larrykin.notificaitionhub.core.data.AppNotificationSettings
import com.larrykin.notificaitionhub.core.data.NotificationHistoryItem
import kotlinx.coroutines.flow.Flow

interface INotificationRepository {
    suspend fun getAppSettings(packageName : String): AppNotificationSettings?
    fun getAllAppSettings(): Flow<List<AppNotificationSettings>>
    suspend fun saveAppSettings(appNotificationSettings: AppNotificationSettings)
    suspend fun addToHistory(packageName: String, notification: Notification?)
    fun getNotificationHistory(): Flow<List<NotificationHistoryItem>>
}