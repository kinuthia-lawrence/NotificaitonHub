package com.larrykin.notificationhub.core.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *
 * Entity representing a notification profile.
 *
 * */
@Entity(tableName = "notification_profiles")
data class NotificationProfile(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val isDefault: Boolean = false,
    val description: String?,
    val createdAt: Long = System.currentTimeMillis(),
    val iconResId: Int?,
    val color: Int?
)
