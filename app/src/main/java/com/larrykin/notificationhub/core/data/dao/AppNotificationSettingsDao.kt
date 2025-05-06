package com.larrykin.notificationhub.core.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
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

    @Query("SELECT DISTINCT packageName FROM app_notification_settings")
    suspend fun getAllUniqueAppPackages(): List<String>

    @Query("SELECT * FROM app_notification_settings WHERE packageName = :packageName AND notificationProfileId = :profileId")
    suspend fun getAppSettingsForPackageAndProfile(
        packageName: String,
        profileId: Long
    ): AppNotificationSettings?

    @Query("SELECT * FROM app_notification_settings WHERE packageName = :packageName ORDER BY notificationProfileId LIMIT 1")
    suspend fun getDefaultAppSettings(packageName: String): AppNotificationSettings?

    @Query("DELETE FROM app_notification_settings WHERE notificationProfileId = :profileId")
    suspend fun deleteSettingsForProfile(profileId: Long)

    @Update
    suspend fun updateActiveAppSettings(settings: AppNotificationSettings)

}
