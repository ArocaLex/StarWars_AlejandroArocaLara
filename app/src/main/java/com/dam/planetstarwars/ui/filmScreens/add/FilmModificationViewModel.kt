package com.dam.planetstarwars.ui.filmScreens.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dam.planetstarwars.data.model.Film
import com.dam.planetstarwars.data.model.repository.FilmRepository
import com.dam.planetstarwars.network.BaseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class FilmScreenEvents {
    data class ShowMessage(val message: String) : FilmScreenEvents()
    object NavigateBack : FilmScreenEvents()
}

@HiltViewModel
class FilmModificationViewModel @Inject constructor(
    private val repository: FilmRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<Film?>(null)
    val uiState: StateFlow<Film?> = _uiState

    private val _events = MutableSharedFlow<FilmScreenEvents>()
    val events: SharedFlow<FilmScreenEvents> = _events

    fun setFilm(film: Film?) {
        _uiState.value = film
    }

    fun saveFilm(film: Film) {
        viewModelScope.launch {
            val isEditing = _uiState.value != null
            val result = if (isEditing) {
                repository.update(film.copy(filmId = _uiState.value!!.filmId))
            } else {
                repository.save(film)
            }

            when (result) {
                is BaseResult.Success -> {
                    _events.emit(FilmScreenEvents.ShowMessage("Película ${if (isEditing) "actualizada" else "guardada"} correctamente"))
                    _events.emit(FilmScreenEvents.NavigateBack)
                }
                is BaseResult.Error -> {
                    _events.emit(FilmScreenEvents.ShowMessage(result.exception.message ?: "Error desconocido"))
                }
            }
        }
    }
}
