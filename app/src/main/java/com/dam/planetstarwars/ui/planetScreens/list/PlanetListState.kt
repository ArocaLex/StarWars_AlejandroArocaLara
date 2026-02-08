package com.dam.planetstarwars.ui.planetScreens.list

import com.dam.planetstarwars.data.model.Planet

sealed interface PlanetListState {
    data object Loading : PlanetListState
    data object NoData : PlanetListState
    data class Success(val planets: List<Planet>) : PlanetListState
    data class Error(val message: String) : PlanetListState
}