package com.gulshid.weatherapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @Entity tells Room this is a database table named "weather_table".
 * Each property = one column in the table.
 * cityName is the primary key — one row per city.
 */
@Entity(tableName = "weather_table")
data class WeatherEntity(
    @PrimaryKey
    val cityName: String,
    val tempCelsius: Double,
    val feelsLike: Double,
    val tempMin: Double,
    val tempMax: Double,
    val humidity: Int,
    val pressure: Int,
    val windSpeed: Double,
    val description: String,
    val iconCode: String,
    val country: String,
    val visibility: Int,
    val sunrise: Long,
    val sunset: Long,
    val timestamp: Long     // when we last fetched this data
)