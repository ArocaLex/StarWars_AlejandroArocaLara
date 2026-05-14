package com.dam.planetstarwars.ui.planetScreens.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dam.planetstarwars.data.model.Planet
import com.dam.planetstarwars.data.model.repository.PlanetRepository
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
class PlanetListViewModel @Inject constructor(
    private val repository: PlanetRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val uiState: StateFlow<PlanetListState> = combine(
        repository.getData(),
        _searchQuery
    ) { planets, query ->
        val filtered = if (query.isEmpty()) planets
                       else planets.filter { it.name.contains(query, ignoreCase = true) }
        if (filtered.isEmpty()) PlanetListState.Empty
        else PlanetListState.Success(filtered)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PlanetListState.Loading
    )

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun deletePlanet(planet: Planet) {
        viewModelScope.launch {
            repository.delete(planet)
        }
    }
}
