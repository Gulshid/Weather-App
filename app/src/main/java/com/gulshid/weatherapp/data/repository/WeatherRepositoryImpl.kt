package com.gulshid.weatherapp.data.repository

import com.gulshid.weatherapp.data.local.dao.WeatherDao
import com.gulshid.weatherapp.data.mapper.toDomain
import com.gulshid.weatherapp.data.mapper.toEntity
import com.gulshid.weatherapp.data.remote.api.WeatherApiService
import com.gulshid.weatherapp.domain.model.WeatherModel
import com.gulshid.weatherapp.domain.repository.WeatherRepository
import com.gulshid.weatherapp.utils.Resource
import com.gulshid.weatherapp.BuildConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * The actual implementation of WeatherRepository.
 * Lives in DATA layer — knows about both Retrofit and Room.
 *
 * Strategy: NETWORK FIRST
 *   1. Emit Loading
 *   2. Try to fetch from API
 *   3a. API success → map to domain → save to Room → emit Success
 *   3b. API fails   → load from Room cache
 *       - Cache found → emit Success (stale data)
 *       - Cache empty → emit Error
 */
class WeatherRepositoryImpl @Inject constructor(
    private val apiService: WeatherApiService,
    private val weatherDao: WeatherDao
) : WeatherRepository {

    override fun getWeather(cityName: String): Flow<Resource<WeatherModel>> = flow {
        // TEMP: verify key is loaded — remove after testing
        android.util.Log.d("API_KEY", "Key = '${BuildConfig.WEATHER_API_KEY}'")

        emit(Resource.Loading)

        try {
            // Step 2: Hit the network
            val response = apiService.getCurrentWeather(
                city = cityName,
                apiKey = BuildConfig.WEATHER_API_KEY
            )

            // Step 3a: Map DTO → Domain model
            val weatherModel = response.toDomain()

            // Step 4: Save to Room for offline use
            weatherDao.insertWeather(weatherModel.toEntity())

            // Step 5: Emit success with fresh data
            emit(Resource.Success(weatherModel))

        } catch (e: Exception) {
            // Network failed — try Room cache
            weatherDao.getWeatherByCity(cityName).collect { cachedEntity ->
                if (cachedEntity != null) {
                    // Return stale cached data
                    emit(Resource.Success(cachedEntity.toDomain()))
                } else {
                    // Nothing in cache either
                    emit(
                        Resource.Error(
                            message = e.localizedMessage ?: "Unknown error occurred"
                        )
                    )
                }
            }
        }
    }

    override suspend fun getLastUpdatedWeather(): WeatherModel? {
        return weatherDao.getLastUpdatedWeather()?.toDomain()
    }
}