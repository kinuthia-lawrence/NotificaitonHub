package com.larrykin.notificaitionhub.core.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *
 * Entity representing a notification history item.
 * */
@Entity(tableName = "notification_history")
data class NotificationHistoryItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val packageName: String,
    val appName: String,
    val title: String?,
    val content: String?,
    val timeStamp: Long,
    val wasModified: Boolean

)
