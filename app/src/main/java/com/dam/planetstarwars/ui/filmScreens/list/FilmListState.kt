package com.dam.planetstarwars.ui.filmScreens.list

import com.dam.planetstarwars.data.model.Film

sealed class FilmListState {
    object Loading : FilmListState()
    object Empty : FilmListState()
    data class Success(val films: List<Film>) : FilmListState()
    data class Error(val message: String) : FilmListState()
}
