package com.larrykin.notificationhub.core.data.repository

import android.app.Notification
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import com.larrykin.notificationhub.core.data.dao.AppNotificationSettingsDao
import com.larrykin.notificationhub.core.data.dao.NotificationHistoryItemDao
import com.larrykin.notificationhub.core.data.entities.AppNotificationSettings
import com.larrykin.notificationhub.core.data.entities.NotificationHistoryItem
import com.larrykin.notificationhub.core.domain.model.AppInfoDetails
import com.larrykin.notificationhub.core.domain.repository.INotificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Date
import androidx.core.net.toUri

class NotificationRepository(
    private val settingsDao: AppNotificationSettingsDao,
    private val historyDao: NotificationHistoryItemDao
) : INotificationRepository {
    override suspend fun getAppSettings(packageName: String): AppNotificationSettings? {
        return settingsDao.getAppSettings(packageName);
    }

    override fun getAllAppSettings(): Flow<List<AppNotificationSettings>> {
        return settingsDao.getAllAppSettings();
    }

    override suspend fun saveAppSettings(appNotificationSettings: AppNotificationSettings) {
        settingsDao.insertOrUpdateAppSettings(appNotificationSettings);
    }

    override suspend fun addToHistory(
        packageName: String,
        notification: Notification?
    ) {
        //extract relevant info from the notification
        val title = notification?.extras?.getString(Notification.EXTRA_TITLE);
        val text = notification?.extras?.getString(Notification.EXTRA_TEXT)

        val historyItem = NotificationHistoryItem(
            packageName = packageName,
            appName = getAppName(packageName),
            title = title,
            content = text,
            timeStamp = Date().time,
            wasModified = true
        )
        historyDao.insertHistoryItem(historyItem);
    }

    override fun getNotificationHistory(): Flow<List<NotificationHistoryItem>> {
        return historyDao.getAllHistory();
    }

    override fun activateProfile(profileId: Long) {
        TODO("Implement functionality to activate a profile")
    }


    override fun getInstalledApps(packageManager: PackageManager?): Flow<List<AppInfoDetails>> {
        return flow {
            val appDetailsList = mutableListOf<AppInfoDetails>()

            if (packageManager == null) {
                emit(appDetailsList)
                Log.e("NotificationRepository", "PackageManager is null")
                return@flow
            }

            val intent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
            val resolveInfos = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Log.d("NotificationRepository", "queryIntentActivities with MATCH_ALL")
                packageManager.queryIntentActivities(
                    intent,
                    PackageManager.ResolveInfoFlags.of(PackageManager.MATCH_ALL.toLong())
                )

            } else {
                Log.d("NotificationRepository", "Deprecated queryIntentActivities used")
                @Suppress("DEPRECATION")
                packageManager.queryIntentActivities(intent, PackageManager.MATCH_ALL)
            }

            for (resolveInfo in resolveInfos) {
                val packageName = resolveInfo.activityInfo.packageName
                Log.d("NotificationRepository", "packageName: $packageName")

                try {
                    val appInfo = packageManager.getApplicationInfo(packageName, 0)
                    val appName = packageManager.getApplicationLabel(appInfo).toString()

                    // Create icon URI
                    val iconUri = try {
                        val iconId = appInfo.icon
                        "android.resource://${packageName}/${iconId}".toUri()
                    } catch (e: Exception) {
                        null
                    }

                    appDetailsList.add(
                        AppInfoDetails(
                            name = appName,
                            packageName = packageName,
                            iconUri = iconUri,
                            notificationsEnabled = false, // Default value
                            soundEnabled = true          // Default value
                        )
                    )
                } catch (e: Exception) {
                    // Skip this app if we can't get its information
                }
            }
            emit(appDetailsList)
        }
    }

    private fun getAppName(packageName: String): String {
        return packageName; //TODO("Implement functionality to get the application name from the package name")
    }
}