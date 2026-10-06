package com.example.medicineadmin.view.Notification


import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.medicineadmin.data.Repo.Repository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class NotificationWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: Repository
) : CoroutineWorker(
    appContext,
    workerParams
) {

    override suspend fun doWork(): Result {

        return try {

            val response =
                repository.getUnreadNotifications()

            if (!response.isSuccessful) {
                return Result.retry()
            }

            val notifications =
                response.body().orEmpty()

            notifications.forEach { notification ->

                NotificationHelper.show(
                    context = applicationContext,
                    title = notification.title,
                    message = notification.message,
                    notificationId = notification.id
                )
            }

            Result.success()

        } catch (e: Exception) {

            Result.retry()
        }
    }
}