package com.dam.planetstarwars.network

sealed class BaseResult<out T> {
    data class Success<T>(var data: T) : BaseResult<T>()
    data class Error(var exception: Exception) : BaseResult<Nothing>()
}