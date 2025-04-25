package com.larrykin.notificationhub.core.service

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.larrykin.notificationhub.core.data.AppNotificationSettings
import com.larrykin.notificationhub.core.domain.INotificationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class NotificationHubListener : NotificationListenerService() {
    private val repository: INotificationRepository by inject()
    private val serviceScope = CoroutineScope(Dispatchers.IO)

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        val packageName = sbn?.packageName
        val notification = sbn?.notification

        serviceScope.launch {
            //Get app-specific settings
            val appSettings = repository.getAppSettings(packageName.toString())

            if (appSettings != null) {
                //Cancel original notification
                cancelNotification(sbn?.key)

                //create modified notification with custom settings
                val modifiedNotification = createModifiedNotification(notification, appSettings)

                // Post the modified notification
                //implementation depends on how we want to display modified notification

                //Log to notification history
                repository.addToHistory(packageName.toString(), notification)
            }
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
    }

    private fun createModifiedNotification(
        original: Notification?,
        appSettings: AppNotificationSettings
    ): Notification? {
        // TODO: implement creating the notification
        return original
    }
}