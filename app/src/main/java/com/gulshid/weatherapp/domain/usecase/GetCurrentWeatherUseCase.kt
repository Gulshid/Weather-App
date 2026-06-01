package com.gulshid.weatherapp.domain.usecase

import com.gulshid.weatherapp.domain.model.WeatherModel
import com.gulshid.weatherapp.domain.repository.WeatherRepository
import com.gulshid.weatherapp.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * UseCase: Get current weather for a city.
 *
 * Validates input FIRST, then delegates to repository.
 * ViewModel calls: getWeatherUseCase("London")
 *
 * The 'operator fun invoke' trick lets us call the class
 * like a function: useCase("London") instead of useCase.execute("London")
 */
class GetCurrentWeatherUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    operator fun invoke(cityName: String): Flow<Resource<WeatherModel>> {

        // Validate before hitting network
        val validation = validateInput(cityName)
        if (validation != null) {
            // Return error flow immediately without touching repository
            return flow { emit(Resource.Error(validation)) }
        }

        // Input is valid — delegate to repository
        return repository.getWeather(cityName.trim())
    }

    /**
     * Returns an error message string if invalid, null if valid.
     */
    private fun validateInput(cityName: String): String? {
        return when {
            cityName.isBlank()   -> "City name cannot be empty"
            cityName.trim().length < 2 -> "City name is too short"
            cityName.trim().length > 50 -> "City name is too long"
            else -> null  // valid!
        }
    }
}