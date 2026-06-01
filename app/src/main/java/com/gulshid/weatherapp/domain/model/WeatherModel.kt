package com.gulshid.weatherapp.domain.model

/**
 * Pure Kotlin data class — ZERO Android imports.
 * This is what the UI layer works with exclusively.
 * It never knows about Retrofit DTOs or Room entities.
 */
data class WeatherModel(
    val cityName: String,
    val country: String,
    val tempCelsius: Double,
    val feelsLike: Double,
    val tempMin: Double,
    val tempMax: Double,
    val humidity: Int,
    val pressure: Int,
    val windSpeed: Double,
    val description: String,
    val iconCode: String,       // used to build icon URL
    val visibility: Int,
    val sunrise: Long,          // unix timestamp
    val sunset: Long,           // unix timestamp
    val timestamp: Long         // when data was fetched
) {
    /** Icon URL ready to load with Glide */
    val iconUrl: String
        get() = "https://openweathermap.org/img/wn/$iconCode@2x.png"

    /** Formatted temperature string */
    val tempFormatted: String
        get() = "${tempCelsius.toInt()}°C"

    /** Capitalize description */
    val descriptionFormatted: String
        get() = description.replaceFirstChar { it.uppercase() }
}