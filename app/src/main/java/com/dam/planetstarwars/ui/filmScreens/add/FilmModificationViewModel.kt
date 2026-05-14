package com.dam.planetstarwars.ui.filmScreens.add

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dam.planetstarwars.data.model.Film
import com.dam.planetstarwars.data.model.repository.FilmRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class FilmScreenEvents {
    data class ShowMessage(val message: String) : FilmScreenEvents()
    object NavigateBack : FilmScreenEvents()
    data class ShowDuplicateDialog(val filmTitle: String) : FilmScreenEvents()
    data class NotifyCreated(val filmTitle: String) : FilmScreenEvents()
}

@HiltViewModel
class FilmModificationViewModel @Inject constructor(
    private val repository: FilmRepository
) : ViewModel() {

    private var existingFilm: Film? = null

    var title by mutableStateOf("")
        private set
    var episodeId by mutableStateOf("")
        private set
    var director by mutableStateOf("")
        private set
    var producer by mutableStateOf("")
        private set
    var releaseDate by mutableStateOf("")
        private set

    var titleError by mutableStateOf<String?>(null)
        private set
    var episodeIdError by mutableStateOf<String?>(null)
        private set
    var directorError by mutableStateOf<String?>(null)
        private set
    var producerError by mutableStateOf<String?>(null)
        private set
    var releaseDateError by mutableStateOf<String?>(null)
        private set

    val isEditMode: Boolean get() = existingFilm != null

    private val _events = MutableSharedFlow<FilmScreenEvents>()
    val events: SharedFlow<FilmScreenEvents> = _events

    fun onTitleChange(v: String) { title = v; titleError = null }
    fun onEpisodeIdChange(v: String) { episodeId = v; episodeIdError = null }
    fun onDirectorChange(v: String) { director = v; directorError = null }
    fun onProducerChange(v: String) { producer = v; producerError = null }
    fun onReleaseDateChange(v: String) { releaseDate = v; releaseDateError = null }

    fun setFilm(film: Film?) {
        existingFilm = film
        if (film != null) {
            title = film.title
            episodeId = film.episodeId.toString()
            director = film.director
            producer = film.producer
            releaseDate = film.releaseDate
        }
    }

    fun saveFilm() {
        viewModelScope.launch {
            titleError = null
            episodeIdError = null
            directorError = null
            producerError = null
            releaseDateError = null

            if (title.isBlank()) {
                titleError = "El título es obligatorio"
                return@launch
            }
            val epId = episodeId.trim().toIntOrNull()
            if (epId == null || epId < 1) {
                episodeIdError = "Debe ser un número mayor que 0"
                return@launch
            }
            if (director.isBlank()) {
                directorError = "El director es obligatorio"
                return@launch
            }
            if (producer.isBlank()) {
                producerError = "El productor es obligatorio"
                return@launch
            }
            if (releaseDate.isBlank()) {
                releaseDateError = "La fecha de estreno es obligatoria"
                return@launch
            }

            if (!isEditMode && repository.exists(title.trim())) {
                _events.emit(FilmScreenEvents.ShowDuplicateDialog(title.trim()))
                return@launch
            }

            try {
                if (isEditMode) {
                    repository.update(
                        existingFilm!!.copy(
                            title = title.trim(),
                            episodeId = epId,
                            director = director.trim(),
                            producer = producer.trim(),
                            releaseDate = releaseDate.trim()
                        )
                    )
                    _events.emit(FilmScreenEvents.ShowMessage("Película actualizada correctamente"))
                } else {
                    repository.insert(
                        Film(
                            title = title.trim(),
                            episodeId = epId,
                            director = director.trim(),
                            producer = producer.trim(),
                            releaseDate = releaseDate.trim()
                        )
                    )
                    _events.emit(FilmScreenEvents.NotifyCreated(title.trim()))
                }
                _events.emit(FilmScreenEvents.NavigateBack)
            } catch (e: Exception) {
                _events.emit(FilmScreenEvents.ShowMessage(e.message ?: "Error desconocido"))
            }
        }
    }
}
