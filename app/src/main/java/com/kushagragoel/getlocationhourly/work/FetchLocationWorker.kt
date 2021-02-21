package com.kushagragoel.getlocationhourly.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class FetchLocationWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {
    companion object {
        const val WORK_NAME = "com.kushagragoel.getlocationhourly.work.FetchLocationWorker"
    }
    override suspend fun doWork(): Result {
        return Result.success()
    }
}