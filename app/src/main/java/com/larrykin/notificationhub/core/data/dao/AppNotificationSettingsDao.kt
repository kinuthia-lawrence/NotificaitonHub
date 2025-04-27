package com.larrykin.notificationhub.core.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.larrykin.notificationhub.core.data.entities.AppNotificationSettings
import kotlinx.coroutines.flow.Flow

@Dao
interface AppNotificationSettingsDao {

    // insert or update app settings
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateAppSettings(appNotificationSettings: AppNotificationSettings)

    // get app settings by package name
    @Query("SELECT * FROM app_notification_settings WHERE packageName=:packageName")
    suspend fun getAppSettings(packageName: String): AppNotificationSettings?

    // get all app settings
    @Query("SELECT * FROM app_notification_settings")
    fun getAllAppSettings(): Flow<List<AppNotificationSettings>>

}
