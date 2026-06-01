package com.gulshid.weatherapp.domain.usecase


import com.gulshid.weatherapp.domain.model.WeatherModel
import com.gulshid.weatherapp.domain.repository.WeatherRepository
import javax.inject.Inject

/**
 * UseCase: Load the last city the user searched.
 *
 * Used when the app starts — restores the last viewed city
 * so the user doesn't see a blank screen.
 *
 * Returns null if the user has never searched before.
 */
class GetLastUpdatedWeatherUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(): WeatherModel? {
        return repository.getLastUpdatedWeather()
    }
}