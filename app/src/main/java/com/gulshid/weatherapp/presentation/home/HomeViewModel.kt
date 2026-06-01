package com.gulshid.weatherapp.presentation.home


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gulshid.weatherapp.domain.model.WeatherModel
import com.gulshid.weatherapp.domain.usecase.GetCurrentWeatherUseCase
import com.gulshid.weatherapp.domain.usecase.GetLastUpdatedWeatherUseCase
import com.gulshid.weatherapp.presentation.common.BaseViewModel
import com.gulshid.weatherapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @HiltViewModel — Hilt creates and injects this ViewModel automatically.
 * Never instantiate ViewModels manually — always use 'by viewModels()'.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val getLastUpdatedWeatherUseCase: GetLastUpdatedWeatherUseCase
) : BaseViewModel() {

    // Private mutable — only ViewModel writes to this
    private val _weatherState = MutableLiveData<Resource<WeatherModel>>()

    // Public immutable — Fragment only reads this
    val weatherState: LiveData<Resource<WeatherModel>> = _weatherState

    // Remember the last searched city for retry button
    private var lastSearchedCity: String = ""

    init {
        // On app start, load last searched city from Room
        loadLastCity()
    }

    /**
     * Called when user searches for a city or retry is pressed.
     */
    fun fetchWeather(cityName: String) {
        lastSearchedCity = cityName
        viewModelScope.launch {
            getCurrentWeatherUseCase(cityName).collect { resource ->
                _weatherState.value = resource
            }
        }
    }

    /**
     * Retry the last search — called from error state retry button.
     */
    fun retry() {
        if (lastSearchedCity.isNotBlank()) {
            fetchWeather(lastSearchedCity)
        }
    }

    /**
     * Load the last cached city when app opens.
     * Shows data instantly while fresh data loads in background.
     */
    private fun loadLastCity() {
        viewModelScope.launch {
            val lastWeather = getLastUpdatedWeatherUseCase()
            if (lastWeather != null) {
                // Show cached data immediately
                _weatherState.value = Resource.Success(lastWeather)
                // Then refresh in background
                fetchWeather(lastWeather.cityName)
            }
        }
    }
}