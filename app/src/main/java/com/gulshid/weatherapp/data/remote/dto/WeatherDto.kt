package com.gulshid.weatherapp.data.remote.dto


import com.google.gson.annotations.SerializedName

/**
 * Represents one item in the "weather" array from the API.
 * Example: { "id": 800, "main": "Clear", "description": "clear sky", "icon": "01d" }
 *
 * @SerializedName maps JSON field names to Kotlin property names.
 * We use it when JSON uses snake_case but Kotlin uses camelCase.
 */
data class WeatherDto(
    @SerializedName("id")          val id: Int,
    @SerializedName("main")        val main: String,
    @SerializedName("description") val description: String,
    @SerializedName("icon")        val icon: String
)