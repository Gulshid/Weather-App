package com.gulshid.weatherapp.di


import com.gulshid.weatherapp.data.repository.WeatherRepositoryImpl
import com.gulshid.weatherapp.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    /**
     * @Binds tells Hilt:
     * "When someone asks for WeatherRepository,
     *  give them WeatherRepositoryImpl"
     *
     * We use abstract + @Binds instead of @Provides
     * because we're just mapping an interface to its implementation.
     */
    @Binds
    @Singleton
    abstract fun bindWeatherRepository(
        impl: WeatherRepositoryImpl
    ): WeatherRepository
}