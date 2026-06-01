package com.gulshid.weatherapp.domain.model

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
    val iconCode: String,
    val visibility: Int,
    val sunrise: Long,
    val sunset: Long,
    val timestamp: Long
) {
    // ── Computed properties (no Android imports needed) ──────────────

    /** Icon URL ready to pass to Glide */
    val iconUrl: String
        get() = "https://openweathermap.org/img/wn/$iconCode@2x.png"

    /** Temperature in Celsius as display string */
    val tempFormattedCelsius: String
        get() = "${tempCelsius.toInt()}°C"

    /** Temperature converted to Fahrenheit */
    val tempFahrenheit: Double
        get() = (tempCelsius * 9 / 5) + 32

    /** Temperature in Fahrenheit as display string */
    val tempFormattedFahrenheit: String
        get() = "${tempFahrenheit.toInt()}°F"

    /** Feels like in Celsius */
    val feelsLikeFormatted: String
        get() = "Feels like ${feelsLike.toInt()}°C"

    /** Capitalize first letter of description */
    val descriptionFormatted: String
        get() = description.replaceFirstChar { it.uppercase() }

    /** Wind speed as display string */
    val windSpeedFormatted: String
        get() = "${windSpeed} m/s"

    /** Visibility in km */
    val visibilityFormatted: String
        get() = "${visibility / 1000} km"

    /**
     * How old is this data?
     * Returns true if data is older than 10 minutes → should refresh
     */
    val isStale: Boolean
        get() = System.currentTimeMillis() - timestamp > 10 * 60 * 1000
}