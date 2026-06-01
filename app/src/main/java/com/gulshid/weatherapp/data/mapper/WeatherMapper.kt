package com.gulshid.weatherapp.data.mapper


import com.gulshid.weatherapp.data.local.entity.WeatherEntity
import com.gulshid.weatherapp.data.remote.dto.WeatherResponseDto
import com.gulshid.weatherapp.domain.model.WeatherModel

/**
 * Mappers convert between layers — keeping each layer's data classes independent.
 *
 * Flow:
 *   API response (WeatherResponseDto)
 *       ↓ toDomain()
 *   Domain model (WeatherModel)       ← what the UI sees
 *       ↓ toEntity()
 *   Database row (WeatherEntity)      ← what Room stores
 *       ↑ toDomain()
 *   Domain model (WeatherModel)       ← loaded from cache
 */

/** Convert API response → Domain model */
fun WeatherResponseDto.toDomain(): WeatherModel {
    return WeatherModel(
        cityName    = name,
        country     = sys.country,
        tempCelsius = main.temp,
        feelsLike   = main.feelsLike,
        tempMin     = main.tempMin,
        tempMax     = main.tempMax,
        humidity    = main.humidity,
        pressure    = main.pressure,
        windSpeed   = wind.speed,
        description = weather.firstOrNull()?.description ?: "N/A",
        iconCode    = weather.firstOrNull()?.icon ?: "01d",
        visibility  = visibility,
        sunrise     = sys.sunrise,
        sunset      = sys.sunset,
        timestamp   = System.currentTimeMillis()
    )
}

/** Convert Domain model → Database entity (for caching) */
fun WeatherModel.toEntity(): WeatherEntity {
    return WeatherEntity(
        cityName    = cityName,
        tempCelsius = tempCelsius,
        feelsLike   = feelsLike,
        tempMin     = tempMin,
        tempMax     = tempMax,
        humidity    = humidity,
        pressure    = pressure,
        windSpeed   = windSpeed,
        description = description,
        iconCode    = iconCode,
        country     = country,
        visibility  = visibility,
        sunrise     = sunrise,
        sunset      = sunset,
        timestamp   = timestamp
    )
}

/** Convert Database entity → Domain model (when loading from cache) */
fun WeatherEntity.toDomain(): WeatherModel {
    return WeatherModel(
        cityName    = cityName,
        country     = country,
        tempCelsius = tempCelsius,
        feelsLike   = feelsLike,
        tempMin     = tempMin,
        tempMax     = tempMax,
        humidity    = humidity,
        pressure    = pressure,
        windSpeed   = windSpeed,
        description = description,
        iconCode    = iconCode,
        visibility  = visibility,
        sunrise     = sunrise,
        sunset      = sunset,
        timestamp   = timestamp
    )
}