package com.gulshid.weatherapp.data.remote.dto


import com.google.gson.annotations.SerializedName

/**
 * One item in the forecast list — represents a 3-hour forecast slot.
 * The API returns 40 of these (5 days × 8 slots per day).
 */
data class ForecastItemDto(
    @SerializedName("dt")      val dt: Long,
    @SerializedName("main")    val main: MainDto,
    @SerializedName("weather") val weather: List<WeatherDto>,
    @SerializedName("wind")    val wind: WindDto,
    @SerializedName("dt_txt")  val dtTxt: String   // "2024-01-15 12:00:00"
)