package com.example.coffeeapps.screen

sealed class StateScreen <out T: Any?> {
    object Loading : StateScreen<Nothing>()

    data class Success<out T: Any>(val data: T) : StateScreen<T>()

    data class Error(val errorMessage: String) : StateScreen<Nothing>()
}
