package com.larrykin.notificationhub.core.domain.model

import android.net.Uri

data class AppInfoDetails(
    val name: String = "",
    val packageName: String = "",
    val iconUri: Uri? = null,
    val notificationsEnabled: Boolean = false,
    val soundEnabled: Boolean = false,
    val volumeLevel: Int = 50,
    val customRingtonePath: String? = null,
    val vibrationPattern: String? = null,
    val ledColor: Int? = null,
    val bypassDnD: Boolean = false,
    val priority: Int = 3,
    val notificationProfileId: Long? = null
)
