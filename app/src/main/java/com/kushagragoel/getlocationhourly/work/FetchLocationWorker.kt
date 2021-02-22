package com.kushagragoel.getlocationhourly.work

import android.content.Context
import android.location.Location
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.android.gms.location.*
import com.kushagragoel.getlocationhourly.database.LocationDatabase
import com.kushagragoel.getlocationhourly.database.LocationEntity
import kotlinx.coroutines.*

class FetchLocationWorker(private val appContext: Context, params: WorkerParameters) : CoroutineWorker(
    appContext, params) {

    companion object {
        const val WORK_NAME = "com.kushagragoel.getlocationhourly.work.FetchLocationWorker"
    }

    private val TAG = "FetchLocationWorker"

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    private val UPDATE_INTERVAL_IN_MILLISECONDS: Long by lazy { 10000 }

    /**
     * The fastest rate for active location updates. Updates will never be more frequent
     * than this value.
     */
    private val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS by lazy { UPDATE_INTERVAL_IN_MILLISECONDS / 2 }

    /**
     * The current location.
     */
    private var mLocation: Location? = null

    /**
     * Provides access to the Fused Location Provider API.
     */
    private var mFusedLocationClient: FusedLocationProviderClient? = null

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    /**
     * Callback for changes in location.
     */
    private var mLocationCallback: LocationCallback? = null
    override suspend fun doWork(): Result {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(appContext)
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                val lastLocation = locationResult.lastLocation
                Log.d(TAG, "onLocationResult: ${lastLocation.latitude} ${lastLocation.longitude}")
            }
        }

        val mLocationRequest = LocationRequest.create()
        mLocationRequest.interval = UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest.fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        try {
            mFusedLocationClient?.lastLocation?.addOnCompleteListener { task ->
                    if (task.isSuccessful && task.result != null) {
                        mLocation = task.result
                        Log.d(TAG, "Location : $mLocation")
                        if (mLocation!=null) {
                            val entity = LocationEntity(
                                latitude = mLocation!!.latitude,
                                longitude = mLocation!!.longitude
                            )
                            uiScope.launch {
                                insertLocationDataInDb(entity)
                            }
                        }
                        mFusedLocationClient!!.removeLocationUpdates(mLocationCallback!!)
                    } else {
                        Log.w(TAG, "Failed to get location. $task")
                    }
                }?.addOnFailureListener {
                Log.d(TAG, "doWork: $it")
            }
        } catch (unlikely: SecurityException) {
            Log.e(TAG, "Lost location permission.$unlikely")
        }

        try {
            mFusedLocationClient?.requestLocationUpdates(mLocationRequest, null)
        } catch (unlikely: SecurityException) {
            Log.e(TAG, "Lost location permission. Could not request updates. $unlikely")
        }

        return Result.success()
    }

    private suspend fun insertLocationDataInDb(entity: LocationEntity) {
        withContext(Dispatchers.IO) {
            LocationDatabase.getInstance(appContext).locationDatabaseDao.insertLocationData(entity)
        }
    }
}