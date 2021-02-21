package com.kushagragoel.getlocationhourly.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

abstract class LocationDatabase: RoomDatabase() {
    abstract val locationDatabaseDao: LocationDatabaseDao
    companion object {
        @Volatile
        private var INSTANCE: LocationDatabase? = null
        fun getInstance(context: Context): LocationDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        LocationDatabase::class.java,
                        "location_history_database"
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}