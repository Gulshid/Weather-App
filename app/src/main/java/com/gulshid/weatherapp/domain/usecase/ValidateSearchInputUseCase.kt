package com.gulshid.weatherapp.domain.usecase


import javax.inject.Inject

/**
 * UseCase: Validate city search input in real-time.
 *
 * Used in SearchFragment as the user types —
 * enables/disables the search button based on input validity.
 *
 * Returns a sealed class so the UI knows exactly what's wrong.
 */
class ValidateSearchInputUseCase @Inject constructor() {

    operator fun invoke(input: String): ValidationResult {
        return when {
            input.isBlank() ->
                ValidationResult.Invalid("Enter a city name")

            input.trim().length < 2 ->
                ValidationResult.Invalid("Name must be at least 2 characters")

            input.trim().length > 50 ->
                ValidationResult.Invalid("Name is too long")

            !input.matches(Regex("^[a-zA-Z\\s\\-']+$")) ->
                ValidationResult.Invalid("Only letters, spaces, hyphens allowed")

            else ->
                ValidationResult.Valid
        }
    }
}

/**
 * Sealed class for validation results.
 * UI uses this to show/hide error messages under the search field.
 */
sealed class ValidationResult {
    object Valid : ValidationResult()
    data class Invalid(val message: String) : ValidationResult()
}