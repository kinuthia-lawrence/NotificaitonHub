package com.larrykin.notificationhub.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.larrykin.notificationhub.core.converter.Converters
import com.larrykin.notificationhub.core.data.dao.AppNotificationSettingsDao
import com.larrykin.notificationhub.core.data.dao.NotificationHistoryItemDao
import com.larrykin.notificationhub.core.data.entities.AppNotificationSettings
import com.larrykin.notificationhub.core.data.entities.NotificationHistoryItem
import com.larrykin.notificationhub.core.data.entities.NotificationProfile

@Database(
    entities = [AppNotificationSettings::class, NotificationHistoryItem::class, NotificationProfile::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    // Define abstract methods with zero arguments and return an instance of DAO
    abstract fun appNotificationSettingsDao(): AppNotificationSettingsDao
    abstract fun notificationHistoryDao(): NotificationHistoryItemDao
}