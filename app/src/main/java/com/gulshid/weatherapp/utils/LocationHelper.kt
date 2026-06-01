package com.gulshid.weatherapp.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import java.util.Locale

/**
 * Helper class to get the user's current city name via GPS.
 * Uses FusedLocationProviderClient for battery-efficient location.
 */
object LocationHelper {

    fun getCityFromLocation(
        context: Context,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        // Check permission first
        val hasPermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasPermission) {
            onError("Location permission not granted")
            return
        }

        val fusedClient = LocationServices.getFusedLocationProviderClient(context)
        val cancellationToken = CancellationTokenSource()

        fusedClient.getCurrentLocation(
            Priority.PRIORITY_BALANCED_POWER_ACCURACY,
            cancellationToken.token
        ).addOnSuccessListener { location ->
            if (location == null) {
                onError("Could not get location")
                return@addOnSuccessListener
            }

            try {
                // Reverse geocode: coordinates → city name
                val geocoder = Geocoder(context, Locale.getDefault())
                @Suppress("DEPRECATION")
                val addresses = geocoder.getFromLocation(
                    location.latitude, location.longitude, 1
                )
                val city = addresses?.firstOrNull()?.locality
                    ?: addresses?.firstOrNull()?.subAdminArea
                    ?: "Unknown"
                onSuccess(city)
            } catch (e: Exception) {
                onError("Could not determine city name")
            }
        }.addOnFailureListener {
            onError("Location request failed: ${it.message}")
        }
    }
}