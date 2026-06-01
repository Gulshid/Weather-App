package com.gulshid.weatherapp.data.remote.api


import com.gulshid.weatherapp.data.remote.dto.ForecastResponseDto
import com.gulshid.weatherapp.data.remote.dto.WeatherResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit turns this interface into actual HTTP calls automatically.
 *
 * BASE URL: https://api.openweathermap.org/data/2.5/
 *
 * Two endpoints:
 * 1. /weather  → current weather for a city
 * 2. /forecast → 5-day / 3-hour forecast for a city
 */
interface WeatherApiService {

    /**
     * GET https://api.openweathermap.org/data/2.5/weather
     *   ?q=London
     *   &appid=YOUR_KEY
     *   &units=metric
     */
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("q")     city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): WeatherResponseDto

    /**
     * GET https://api.openweathermap.org/data/2.5/forecast
     *   ?q=London
     *   &appid=YOUR_KEY
     *   &units=metric
     */
    @GET("forecast")
    suspend fun getForecast(
        @Query("q")     city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): ForecastResponseDto
}