package iti.student.finalproject.worker

import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import iti.student.finalproject.WeatherApp

class AlertCleanupWorker(
    context: android.content.Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val alertId = inputData.getInt(KEY_ALERT_ID, -1)
        if (alertId < 0) return Result.failure()

        val app = applicationContext as? WeatherApp ?: return Result.failure()
        app.database.alertDao().deleteById(alertId)
        return Result.success()
    }

    companion object {
        const val KEY_ALERT_ID = "alert_id"
        const val WORK_NAME_PREFIX = "alert_cleanup_"
    }
}
