package com.larrykin.notificationhub.core.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.larrykin.notificationhub.core.data.entities.NotificationProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {
    @Query("SELECT * FROM notification_profiles ORDER BY isDefault DESC, name ASC")
    fun getAllProfiles(): Flow<List<NotificationProfile>>

    @Query("SELECT * FROM notification_profiles WHERE isDefault = 1 LIMIT 1")
    suspend fun getDefaultProfile(): NotificationProfile?

    @Query("SELECT * FROM notification_profiles WHERE id = :profileId")
    suspend fun getProfileById(profileId: Long): NotificationProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: NotificationProfile): Long

    @Update
    suspend fun updateProfile(profile: NotificationProfile)

    @Delete
    suspend fun deleteProfile(profile: NotificationProfile)

    @Query("UPDATE notification_profiles SET isDefault = 0")
    suspend fun clearDefaultProfile()

    @Transaction
    suspend fun setDefaultProfile(profileId: Long) {
        clearDefaultProfile()
        updateProfileDefault(profileId, true)
    }

    @Query("UPDATE notification_profiles SET isDefault = :isDefault WHERE id = :profileId")
    suspend fun updateProfileDefault(profileId: Long, isDefault: Boolean)
}
