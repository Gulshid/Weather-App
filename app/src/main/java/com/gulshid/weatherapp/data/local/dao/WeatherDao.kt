package com.gulshid.weatherapp.data.local.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gulshid.weatherapp.data.local.entity.WeatherEntity
import kotlinx.coroutines.flow.Flow

/**
 * @Dao = Data Access Object.
 * Room generates the actual SQL implementation of these functions at compile time.
 */
@Dao
interface WeatherDao {

    /**
     * Get cached weather for a city.
     * Returns Flow so the UI auto-updates when data changes.
     */
    @Query("SELECT * FROM weather_table WHERE cityName = :cityName")
    fun getWeatherByCity(cityName: String): Flow<WeatherEntity?>

    /**
     * Insert or update weather data.
     * REPLACE strategy = if city already exists, overwrite it.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: WeatherEntity)

    /**
     * Get the most recently updated city
     * (used to reload last viewed city on app start)
     */
    @Query("SELECT * FROM weather_table ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLastUpdatedWeather(): WeatherEntity?

    /**
     * Delete old cached data for a city.
     */
    @Query("DELETE FROM weather_table WHERE cityName = :cityName")
    suspend fun deleteWeather(cityName: String)

    /**
     * Clear entire cache.
     */
    @Query("DELETE FROM weather_table")
    suspend fun clearAll()
}