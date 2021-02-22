package com.kushagragoel.getlocationhourly.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.paging.toLiveData
import com.kushagragoel.getlocationhourly.database.LocationDatabase

class LocationFetchViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = LocationDatabase.getInstance(application).locationDatabaseDao

    val allLocations = dao.getAllLocationData().toLiveData(5)
}