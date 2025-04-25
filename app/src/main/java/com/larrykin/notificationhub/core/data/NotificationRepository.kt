package com.larrykin.notificationhub.core.data

import android.app.Notification
import com.larrykin.notificationhub.core.domain.INotificationRepository
import kotlinx.coroutines.flow.Flow
import java.util.Date

class NotificationRepository(
    private val settingsDao: AppNotificationSettingsDao,
    private val historyDao: NotificationHistoryItemDao
) : INotificationRepository {
    override suspend fun getAppSettings(packageName: String): AppNotificationSettings? {
        return settingsDao.getAppSettings(packageName);
    }

    override fun getAllAppSettings(): Flow<List<AppNotificationSettings>> {
        return settingsDao.getAllAppSettings();
    }

    override suspend fun saveAppSettings(appNotificationSettings: AppNotificationSettings) {
        settingsDao.insertOrUpdateAppSettings(appNotificationSettings);
    }

    override suspend fun addToHistory(
        packageName: String,
        notification: Notification?
    ) {
        //extract relevant info from the notification
        val title = notification?.extras?.getString(Notification.EXTRA_TITLE);
        val text = notification?.extras?.getString(Notification.EXTRA_TEXT)

        val historyItem = NotificationHistoryItem(
            packageName = packageName,
            appName = getAppName(packageName),
            title = title,
            content = text,
            timeStamp = Date().time,
            wasModified = true
        )
        historyDao.insertHistoryItem(historyItem);
    }

    override fun getNotificationHistory(): Flow<List<NotificationHistoryItem>> {
        return historyDao.getAllHistory();
    }

    private fun getAppName(packageName: String): String {
        return packageName; //TODO("Implement functionality to get the application name from the package name")
    }
}