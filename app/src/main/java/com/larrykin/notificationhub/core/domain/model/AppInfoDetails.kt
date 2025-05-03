package com.larrykin.notificationhub.core.domain.model

import android.net.Uri

data class AppInfoDetails(
    val name: String = "",
    val packageName: String = "",
    val iconUri: Uri? = null,
    val notificationsEnabled: Boolean = false,
    val soundEnabled: Boolean = true
)
