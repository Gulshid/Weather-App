package com.gulshid.weatherapp.presentation.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.gulshid.weatherapp.databinding.FragmentSearchBinding
import com.gulshid.weatherapp.domain.usecase.ValidateSearchInputUseCase
import com.gulshid.weatherapp.domain.usecase.ValidationResult
import com.gulshid.weatherapp.presentation.common.BaseFragment
import com.gulshid.weatherapp.utils.hide
import com.gulshid.weatherapp.utils.show
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>() {

    @Inject
    lateinit var validateInput: ValidateSearchInputUseCase

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSearchBinding.inflate(inflater, container, false)

    override fun setupViews() {

        // Back button
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        // Real-time validation as user types
        binding.etSearch.doOnTextChanged { text, _, _, _ ->
            val input = text.toString()
            when (val result = validateInput(input)) {
                is ValidationResult.Valid -> {
                    binding.tvValidationError.hide()
                    binding.btnSearch.isEnabled = true
                }
                is ValidationResult.Invalid -> {
                    if (input.isNotEmpty()) {
                        binding.tvValidationError.text = result.message
                        binding.tvValidationError.show()
                    } else {
                        binding.tvValidationError.hide()
                    }
                    binding.btnSearch.isEnabled = false
                }
            }
        }

        // Search on button click
        binding.btnSearch.setOnClickListener {
            performSearch()
        }

        // Search on keyboard "Search" button
        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
                true
            } else false
        }
    }

    private fun performSearch() {
        val city = binding.etSearch.text.toString().trim()
        if (city.isNotBlank()) {
            // ✅ Pass city back via savedStateHandle — NOT navigate()
            // This goes back to HomeFragment and delivers the city name
            findNavController()
                .previousBackStackEntry
                ?.savedStateHandle
                ?.set("cityName", city)
            findNavController().popBackStack()
        }
    }
}