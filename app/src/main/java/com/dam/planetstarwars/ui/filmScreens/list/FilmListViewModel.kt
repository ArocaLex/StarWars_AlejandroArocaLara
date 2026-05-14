package com.dam.planetstarwars.ui.filmScreens.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dam.planetstarwars.data.model.Film
import com.dam.planetstarwars.data.model.repository.FilmRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class FilmListState {
    object Loading : FilmListState()
    object Empty : FilmListState()
    data class Success(val films: List<Film>) : FilmListState()
    data class Error(val message: String) : FilmListState()
}

@HiltViewModel
class FilmListViewModel @Inject constructor(
    private val repository: FilmRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val uiState: StateFlow<FilmListState> = combine(
        repository.getData(),
        _searchQuery
    ) { films, query ->
        val filteredFilms = if (query.isEmpty()) {
            films
        } else {
            films.filter { it.title.contains(query, ignoreCase = true) }
        }

        if (filteredFilms.isEmpty()) {
            FilmListState.Empty
        } else {
            FilmListState.Success(filteredFilms)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = FilmListState.Loading
    )

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun deleteFilm(film: Film) {
        viewModelScope.launch {
            repository.delete(film)
        }
    }
}
