package com.gulshid.weatherapp.data.remote.dto


import com.google.gson.annotations.SerializedName

/**
 * The "main" object from API — contains all temperature & pressure data.
 */
data class MainDto(
    @SerializedName("temp")       val temp: Double,
    @SerializedName("feels_like") val feelsLike: Double,
    @SerializedName("temp_min")   val tempMin: Double,
    @SerializedName("temp_max")   val tempMax: Double,
    @SerializedName("humidity")   val humidity: Int,
    @SerializedName("pressure")   val pressure: Int
)