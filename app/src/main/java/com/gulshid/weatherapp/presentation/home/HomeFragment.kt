package com.gulshid.weatherapp.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gulshid.weatherapp.utils.showToast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.gulshid.weatherapp.presentation.MainActivity
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
    private var isCelsius = true

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentHomeBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cityName = arguments?.getString("cityName") ?: ""
        if (cityName.isNotBlank()) {
            viewModel.fetchWeather(cityName)
        }
    }

    override fun setupViews() {
        // Search buttons
        binding.btnSearch.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_search)
        }
        binding.btnSearchEmpty.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_search)
        }

        // Retry button
        binding.btnRetry.setOnClickListener {
            viewModel.retry()
        }

        // Pull-to-refresh
        binding.swipeRefresh.setColorSchemeColors(
            requireContext().getColor(R.color.colorAccent),
            requireContext().getColor(R.color.colorPrimary)
        )
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }

        // GPS location button
        binding.btnLocation.setOnClickListener {
            requestLocationWeather()
        }

        // Unit toggle — °C click
        binding.tvCelsius.setOnClickListener {
            if (!isCelsius) viewModel.toggleUnit()
        }



        // Theme toggle
        binding.btnToggleTheme.setOnClickListener {
            (requireActivity() as MainActivity).toggleTheme()
        }

        // Unit toggle — °F click
        binding.tvFahrenheit.setOnClickListener {
            if (isCelsius) viewModel.toggleUnit()
        }
    }

    // ── Location ──────────────────────────────────────────────────────

    private val locationPermissionRequest = registerForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts
            .RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[android.Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            fetchLocationWeather()
        } else {
            showToast("Location permission denied")
        }
    }

    private fun requestLocationWeather() {
        val fineGranted = androidx.core.content.ContextCompat.checkSelfPermission(
            requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED

        if (fineGranted) {
            fetchLocationWeather()
        } else {
            locationPermissionRequest.launch(arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ))
        }
    }

    private fun fetchLocationWeather() {
        binding.layoutLoading.show()
        binding.layoutContent.hide()
        binding.layoutEmpty.hide()

        com.gulshid.weatherapp.utils.LocationHelper.getCityFromLocation(
            context    = requireContext(),
            onSuccess  = { city -> viewModel.fetchWeather(city) },
            onError    = { error -> showError(error) }
        )
    }


    override fun observeViewModel() {
        // Weather data
        viewModel.weatherState.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> showLoading()
                is Resource.Success -> showWeather(resource.data)
                is Resource.Error   -> showError(resource.message)
            }
        }

        // Pull-to-refresh spinner
        viewModel.isRefreshing.observe(viewLifecycleOwner) { refreshing ->
            binding.swipeRefresh.isRefreshing = refreshing
        }

        // Unit toggle UI update
        viewModel.isCelsius.observe(viewLifecycleOwner) { celsius ->
            isCelsius = celsius
            updateUnitToggleUI(celsius)
        }
    }

    // ── UI State Handlers ─────────────────────────────────────────

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
            tvDescription.text = weather.descriptionFormatted
            tvLastUpdated.text = "Last updated: ${formatTime(weather.timestamp / 1000)}"

            // Temperature — respects unit toggle
            if (isCelsius) {
                tvTemperature.text = weather.tempFormattedCelsius
                tvHighLow.text = "H: ${weather.tempMax.toInt()}°C  " +
                        "L: ${weather.tempMin.toInt()}°C"
                tvFeelsLike.text   = "${weather.feelsLike.toInt()}°C"
            } else {
                tvTemperature.text = weather.tempFormattedFahrenheit
                val maxF = ((weather.tempMax * 9 / 5) + 32).toInt()
                val minF = ((weather.tempMin * 9 / 5) + 32).toInt()
                tvHighLow.text = "H: ${maxF}°F  L: ${minF}°F"
                tvFeelsLike.text   = "${((weather.feelsLike * 9/5)+32).toInt()}°F"
            }

            tvHumidity.text    = "${weather.humidity}%"
            tvWindSpeed.text   = weather.windSpeedFormatted
            tvVisibility.text  = weather.visibilityFormatted
            tvPressure.text    = "${weather.pressure} hPa"
            tvSunrise.text     = formatTime(weather.sunrise)

            // Load weather icon
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

    // ── Helpers ───────────────────────────────────────────────────

    private fun updateUnitToggleUI(isCelsius: Boolean) {
        if (isCelsius) {
            binding.tvCelsius.alpha   = 1.0f
            binding.tvFahrenheit.alpha = 0.5f
            binding.tvCelsius.textSize   = 14f
            binding.tvFahrenheit.textSize = 12f
        } else {
            binding.tvCelsius.alpha   = 0.5f
            binding.tvFahrenheit.alpha = 1.0f
            binding.tvCelsius.textSize   = 12f
            binding.tvFahrenheit.textSize = 14f
        }
    }

    private fun formatTime(unixSeconds: Long): String {
        val date = Date(unixSeconds * 1000)
        return SimpleDateFormat("hh:mm a", Locale.getDefault()).format(date)
    }
}