package com.gulshid.weatherapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gulshid.weatherapp.data.local.dao.WeatherDao
import com.gulshid.weatherapp.data.local.entity.WeatherEntity

/**
 * The Room database class.
 *
 * @Database annotation:
 *   entities = list of all table classes
 *   version  = increment this whenever you change the schema
 *   exportSchema = false (keeps project clean, true for production)
 *
 * Room generates the full implementation of this abstract class.
 */
@Database(
    entities = [WeatherEntity::class],
    version = 1,
    exportSchema = false
)
abstract class WeatherDatabase : RoomDatabase() {
    /**
     * Room implements this automatically.
     * Call db.weatherDao() to get the DAO instance.
     */
    abstract fun weatherDao(): WeatherDao
}