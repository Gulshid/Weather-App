package com.gulshid.weatherapp.domain.repository

import com.gulshid.weatherapp.domain.model.WeatherModel
import com.gulshid.weatherapp.utils.Resource
import kotlinx.coroutines.flow.Flow

/**
 * Interface lives in DOMAIN layer.
 * ViewModel only knows about this interface — not the implementation.
 * This makes the code testable (we can swap with a fake in tests).
 */
interface WeatherRepository {

    /**
     * Returns a Flow of Resource<WeatherModel>.
     * Emits: Loading → Success (or Error)
     */
    fun getWeather(cityName: String): Flow<Resource<WeatherModel>>

    /**
     * Load the last city the user searched —
     * used to restore state when app reopens.
     */
    suspend fun getLastUpdatedWeather(): WeatherModel?
}