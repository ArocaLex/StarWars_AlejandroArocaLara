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
        val filtered = if (query.isEmpty()) films
                       else films.filter { it.title.contains(query, ignoreCase = true) }
        if (filtered.isEmpty()) FilmListState.Empty
        else FilmListState.Success(filtered)
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
