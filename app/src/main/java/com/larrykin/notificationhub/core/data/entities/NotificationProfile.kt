package com.larrykin.notificationhub.core.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *
 * Entity representing a notification profile.
 *
 * */
@Entity(
    tableName = "notification_profile"
)
data class NotificationProfile(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val isActive: Boolean,
    val activationStartTime: Long?,  // for scheduled profiles
    val activationEndTime: Long?,
    val activationDays: String?  //Stored as comma-separated day numbers
)
