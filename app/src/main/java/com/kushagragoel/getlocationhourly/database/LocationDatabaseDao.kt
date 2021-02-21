package com.kushagragoel.getlocationhourly.database

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.Query

interface LocationDatabaseDao {
    @Insert
    fun insertLocationData(locationEntity: LocationEntity)

    @Query("SELECT * FROM location_history ORDER BY date_added DESC")
    fun getAllLocationData(): LiveData<List<LocationEntity>?>

    @Query("DELETE FROM location_history")
    fun clearData()
}