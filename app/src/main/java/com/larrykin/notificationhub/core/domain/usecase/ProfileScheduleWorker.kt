package com.larrykin.notificationhub.core.domain.usecase

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.larrykin.notificationhub.core.domain.repository.INotificationRepository
import org.koin.java.KoinJavaComponent
import java.util.concurrent.TimeUnit

class ProfileScheduleWorker(
    context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    override fun doWork(): Result {
        val profileId = inputData.getLong("profileId", -1)
        if (profileId == -1L) return Result.failure()

        // Apply profile settings
        val repository =
            KoinJavaComponent.get<INotificationRepository>(INotificationRepository::class.java)
        repository.activateProfile(profileId)

        return Result.success()
    }

    companion object {
        // Schedule a profile to be applied at a specific time
        fun scheduleProfile(context: Context, profileId: Long, timeInMillis: Long) {
            val data = workDataOf("profileId" to profileId)

            val workRequest = OneTimeWorkRequestBuilder<ProfileScheduleWorker>()
                .setInputData(data)
                .setInitialDelay(timeInMillis - System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .build()

            WorkManager.getInstance(context).enqueue(workRequest)
        }
    }
}