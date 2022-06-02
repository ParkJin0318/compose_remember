package com.example.compose_remember.data

sealed class Result<out T> {

    data class Success<T>(val value: T) : Result<T>()

    data class Failure(val throwable: Throwable) : Result<Nothing>()

    object Loading : Result<Nothing>()
}