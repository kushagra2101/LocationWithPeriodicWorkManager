package com.kushagragoel.getlocationhourly.database

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LocationDatabaseDao {
    @Insert
    fun insertLocationData(locationEntity: LocationEntity)

    @Query("SELECT * FROM location_history ORDER BY date_added DESC")
    fun getAllLocationData(): DataSource.Factory<Int, LocationEntity>

    @Query("DELETE FROM location_history")
    fun clearAllData()

//    @Query("DELETE FROM location_history WHERE locationId:${id}")
//    fun clearLocationDataById(id: String)
}