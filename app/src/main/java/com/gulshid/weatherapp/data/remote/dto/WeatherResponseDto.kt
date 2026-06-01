package com.gulshid.weatherapp.data.remote.dto


import com.google.gson.annotations.SerializedName

/**
 * The ROOT response object from:
 * GET https://api.openweathermap.org/data/2.5/weather?q=London&appid=KEY&units=metric
 *
 * This is the parent that contains all other DTOs as fields.
 */
data class WeatherResponseDto(
    @SerializedName("name")       val name: String,           // city name
    @SerializedName("main")       val main: MainDto,
    @SerializedName("weather")    val weather: List<WeatherDto>,
    @SerializedName("wind")       val wind: WindDto,
    @SerializedName("sys")        val sys: SysDto,
    @SerializedName("visibility") val visibility: Int,
    @SerializedName("dt")         val dt: Long                // unix timestamp
)