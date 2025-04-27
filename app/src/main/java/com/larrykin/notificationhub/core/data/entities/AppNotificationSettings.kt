package com.larrykin.notificationhub.core.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity representing the notification settings for a specific app.
 * This class is used to store and retrieve notification settings
 * stored in room database in a table named "app_notification_settings".
 *
 * @property packageName The package name of the app.
 * */
@Entity(
    tableName = "app_notification_settings"
)
data class AppNotificationSettings(
    @PrimaryKey val packageName: String,
    val appName: String,
    val volumeLevel: Int, //0-100
    val customRingtonePath: String?,
    val vibrationPattern: String?, //Stored as comma-separated values
    val ledColor: Int?,
    val bypassDnD: Boolean,
    val priority: Int, // 0-5
    val isEnabled: Boolean,
    val notificationProfileId: Long?
)