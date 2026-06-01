package com.gulshid.weatherapp.utils

sealed class Resource<out T> {

    /** Data is being fetched — show loading UI */
    object Loading : Resource<Nothing>()

    /** Data fetched successfully */
    data class Success<T>(val data: T) : Resource<T>()

    /** Something went wrong — message is user-facing */
    data class Error(val message: String) : Resource<Nothing>()
}