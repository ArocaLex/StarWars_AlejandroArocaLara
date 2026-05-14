package com.dam.planetstarwars.ui.planetScreens.list

import com.dam.planetstarwars.data.model.Planet

sealed class PlanetListState {
    object Loading : PlanetListState()
    object Empty : PlanetListState()
    data class Success(val planets: List<Planet>) : PlanetListState()
    data class Error(val message: String) : PlanetListState()
}
