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

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val getLastUpdatedWeatherUseCase: GetLastUpdatedWeatherUseCase
) : BaseViewModel() {

    private val _weatherState = MutableLiveData<Resource<WeatherModel>>()
    val weatherState: LiveData<Resource<WeatherModel>> = _weatherState

    // Unit toggle — true = Celsius, false = Fahrenheit
    private val _isCelsius = MutableLiveData(true)
    val isCelsius: LiveData<Boolean> = _isCelsius

    // Track refresh state separately for SwipeRefresh spinner
    private val _isRefreshing = MutableLiveData(false)
    val isRefreshing: LiveData<Boolean> = _isRefreshing

    private var lastSearchedCity: String = ""
    private var cachedWeather: WeatherModel? = null

    init {
        loadLastCity()
    }

    fun fetchWeather(cityName: String) {
        lastSearchedCity = cityName
        viewModelScope.launch {
            getCurrentWeatherUseCase(cityName).collect { resource ->
                if (resource is Resource.Success) {
                    cachedWeather = resource.data
                }
                _weatherState.value = resource
                _isRefreshing.value = false
            }
        }
    }

    fun retry() {
        if (lastSearchedCity.isNotBlank()) {
            fetchWeather(lastSearchedCity)
        }
    }

    /**
     * Called by SwipeRefreshLayout — shows spinner at top instead
     * of replacing the whole content with a loading state.
     */
    fun refresh() {
        _isRefreshing.value = true
        if (lastSearchedCity.isNotBlank()) {
            fetchWeather(lastSearchedCity)
        } else {
            _isRefreshing.value = false
        }
    }

    /**
     * Toggle between Celsius and Fahrenheit.
     * Does NOT re-fetch — just flips the unit flag.
     * The Fragment observes isCelsius and rebinds the temperature text.
     */
    fun toggleUnit() {
        _isCelsius.value = !(_isCelsius.value ?: true)
        // Re-emit cached weather so Fragment rebinds with new unit
        cachedWeather?.let {
            _weatherState.value = Resource.Success(it)
        }
    }

    private fun loadLastCity() {
        viewModelScope.launch {
            val lastWeather = getLastUpdatedWeatherUseCase()
            if (lastWeather != null) {
                cachedWeather = lastWeather
                _weatherState.value = Resource.Success(lastWeather)
                fetchWeather(lastWeather.cityName)
            }
        }
    }
}