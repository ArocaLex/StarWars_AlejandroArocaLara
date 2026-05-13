package com.dam.planetstarwars.ui.filmScreens.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dam.planetstarwars.data.model.Film
import com.dam.planetstarwars.data.model.repository.FilmRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
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

    val uiState: StateFlow<FilmListState> = repository.getData()
        .map { films ->
            if (films.isEmpty()) FilmListState.Empty else FilmListState.Success(films)
        }
        .catch { e -> emit(FilmListState.Error(e.message ?: "Unknown error")) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = FilmListState.Loading
        )

    fun deleteFilm(film: Film) {
        viewModelScope.launch {
            repository.delete(film)
        }
    }
}
