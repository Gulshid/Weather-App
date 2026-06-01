package com.gulshid.weatherapp.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * The "sys" object — contains country code, sunrise/sunset times.
 */
data class SysDto(
    @SerializedName("country") val country: String,
    @SerializedName("sunrise") val sunrise: Long,
    @SerializedName("sunset")  val sunset: Long
)