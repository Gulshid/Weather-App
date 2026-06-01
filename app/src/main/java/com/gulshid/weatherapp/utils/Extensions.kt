package com.gulshid.weatherapp.utils

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData

/** Show a short toast from anywhere with context */
fun Fragment.showToast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}

/** Make a view visible */
fun View.show() {
    visibility = View.VISIBLE
}

/** Make a view gone (takes no space) */
fun View.hide() {
    visibility = View.GONE
}

/** Make a view invisible (takes space but not visible) */
fun View.invisible() {
    visibility = View.INVISIBLE
}

/**
 * Observe a Resource<T> LiveData and route to the right callback.
 * Use this in every Fragment to handle loading/success/error cleanly.
 *
 * Example:
 *   observeResource(viewModel.weather,
 *       onLoading = { showShimmer() },
 *       onSuccess = { data -> bindWeather(data) },
 *       onError   = { msg -> showError(msg) }
 *   )
 */
fun <T> LifecycleOwner.observeResource(
    liveData: LiveData<Resource<T>>,
    onLoading: () -> Unit = {},
    onSuccess: (T) -> Unit,
    onError: (String) -> Unit = {}
) {
    liveData.observe(this) { resource ->
        when (resource) {
            is Resource.Loading  -> onLoading()
            is Resource.Success  -> onSuccess(resource.data)
            is Resource.Error    -> onError(resource.message)
        }
    }
}