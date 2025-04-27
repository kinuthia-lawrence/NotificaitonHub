package com.larrykin.notificationhub.core.domain.model

data class NotificationHistory(
    val id: Long,
    val appName: String,
    val title: String,
    val content: String,
    val timeStamp: Long,
    val isModified: Boolean
)