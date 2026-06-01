package com.gulshid.weatherapp.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.gulshid.weatherapp.R
import com.gulshid.weatherapp.databinding.FragmentHomeBinding
import com.gulshid.weatherapp.domain.model.WeatherModel
import com.gulshid.weatherapp.presentation.common.BaseFragment
import com.gulshid.weatherapp.utils.Resource
import com.gulshid.weatherapp.utils.hide
import com.gulshid.weatherapp.utils.show
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val viewModel: HomeViewModel by viewModels()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentHomeBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Check if SearchFragment passed a city back via navigation arguments
        val cityName = arguments?.getString("cityName") ?: ""
        if (cityName.isNotBlank()) {
            viewModel.fetchWeather(cityName)
        }
    }

    override fun setupViews() {
        binding.btnSearch.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_search)
        }
        binding.btnSearchEmpty.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_search)
        }
        binding.btnRetry.setOnClickListener {
            viewModel.retry()
        }
    }

    override fun observeViewModel() {
        viewModel.weatherState.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> showLoading()
                is Resource.Success -> showWeather(resource.data)
                is Resource.Error   -> showError(resource.message)
            }
        }
    }

    private fun showLoading() {
        binding.layoutLoading.show()
        binding.layoutContent.hide()
        binding.layoutError.hide()
        binding.layoutEmpty.hide()
    }

    private fun showWeather(weather: WeatherModel) {
        binding.layoutLoading.hide()
        binding.layoutError.hide()
        binding.layoutEmpty.hide()
        binding.layoutContent.show()

        with(binding) {
            tvCityName.text    = "${weather.cityName}, ${weather.country}"
            tvTemperature.text = weather.tempFormattedCelsius
            tvDescription.text = weather.descriptionFormatted
            tvHighLow.text     = "H: ${weather.tempMax.toInt()}°C  L: ${weather.tempMin.toInt()}°C"
            tvHumidity.text    = "${weather.humidity}%"
            tvWindSpeed.text   = weather.windSpeedFormatted
            tvFeelsLike.text   = "${weather.feelsLike.toInt()}°C"
            tvVisibility.text  = weather.visibilityFormatted
            tvPressure.text    = "${weather.pressure} hPa"
            tvSunrise.text     = formatTime(weather.sunrise)
            tvLastUpdated.text = "Last updated: ${formatTime(weather.timestamp / 1000)}"

            Glide.with(requireContext())
                .load(weather.iconUrl)
                .into(ivWeatherIcon)
        }
    }

    private fun showError(message: String) {
        binding.layoutLoading.hide()
        binding.layoutContent.hide()
        binding.layoutEmpty.hide()
        binding.layoutError.show()
        binding.tvErrorMessage.text = message
    }

    private fun formatTime(unixSeconds: Long): String {
        val date = Date(unixSeconds * 1000)
        return SimpleDateFormat("hh:mm a", Locale.getDefault()).format(date)
    }
}