package com.kushagragoel.getlocationhourly.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location_history")
data class LocationEntity (
    @PrimaryKey(autoGenerate = true) val locationId: Long = 0L,
    @ColumnInfo(name = "latitude")  val latitude: Double,
    @ColumnInfo(name = "longitude")  val longitude: Double,
    @ColumnInfo(name = "date_added")  val time: Long = System.currentTimeMillis()
)