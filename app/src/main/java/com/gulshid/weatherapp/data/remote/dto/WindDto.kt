package com.gulshid.weatherapp.data.remote.dto


import com.google.gson.annotations.SerializedName

/**
 * The "wind" object from API.
 */
data class WindDto(
    @SerializedName("speed") val speed: Double,
    @SerializedName("deg")   val deg: Int
)