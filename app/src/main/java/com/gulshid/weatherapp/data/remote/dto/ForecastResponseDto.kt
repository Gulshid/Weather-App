package com.gulshid.weatherapp.data.remote.dto


import com.google.gson.annotations.SerializedName

/**
 * Root response from:
 * GET https://api.openweathermap.org/data/2.5/forecast?q=London&appid=KEY&units=metric
 */
data class ForecastResponseDto(
    @SerializedName("list") val list: List<ForecastItemDto>,
    @SerializedName("city") val city: CityDto
)

data class CityDto(
    @SerializedName("name")    val name: String,
    @SerializedName("country") val country: String
)