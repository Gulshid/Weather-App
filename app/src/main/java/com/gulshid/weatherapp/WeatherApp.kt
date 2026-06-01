package com.gulshid.weatherapp


import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * @HiltAndroidApp triggers Hilt's code generation.
 * This must be the first Hilt annotation in your app.
 * Every other @Inject will trace back to this class.
 */
@HiltAndroidApp
class WeatherApp : Application()