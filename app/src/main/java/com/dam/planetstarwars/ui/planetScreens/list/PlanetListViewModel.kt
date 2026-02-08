package com.dam.planetstarwars.ui.planetScreens.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dam.planetstarwars.data.model.Planet
import com.dam.planetstarwars.data.model.repository.PlanetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlanetListViewModel @Inject constructor(
    private val repository: PlanetRepository
) : ViewModel() {

    private val _state = MutableStateFlow<PlanetListState>(PlanetListState.Loading)


    val uiState: StateFlow<PlanetListState> = _state.asStateFlow()

    init {

        getPlanets()
    }

    private fun getPlanets() {
        viewModelScope.launch {
            // Obtenemos el Flow de la base de datos
            repository.getData().collect { list ->
                    if (list.isEmpty()) {
                        _state.value = PlanetListState.NoData
                    } else {
                        _state.value = PlanetListState.Success(list)
                    }
                }
        }
    }

    fun deletePlanet(planet: Planet) {
        viewModelScope.launch {
            repository.delete(planet)
        }
    }
}