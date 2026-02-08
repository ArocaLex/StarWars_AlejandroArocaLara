package com.dam.planetstarwars.data.model.exception

sealed class PlanetException(msg: String) : Exception(msg) {
    data class NotFound(val msg: String = "Planeta no encontrado") : PlanetException(msg)
    data class Exists(val msg: String = "Planeta ya existente") : PlanetException(msg)
    data class None(val msg: String = "") : PlanetException(msg)
}