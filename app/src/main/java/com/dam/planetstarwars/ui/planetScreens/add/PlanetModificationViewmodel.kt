package com.dam.planetstarwars.ui.planetScreens.add

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dam.planetstarwars.data.model.Planet
import com.dam.planetstarwars.data.model.repository.PlanetRepository
import com.dam.planetstarwars.network.BaseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlanetModificationViewModel @Inject constructor(
    private val repository: PlanetRepository
) : ViewModel() {


    private val emptyPlanet = Planet(
        name = "",
        rotationPeriod = "",
        orbitalPeriod = "",
        climate = "",
        terrain = "",
        population = "",
        gravity = "",
        diameter = "",
        created = "",
        isColonized = false
    )

    var planetState by mutableStateOf(emptyPlanet)

    var editMode by mutableStateOf(false)
        private set



    fun setPlanet(planet: Planet?) {
        if (planet != null) {
            editMode = true
            planetState = planet.copy()
        } else {
            editMode = false
            planetState = emptyPlanet.copy()
        }
    }


    fun onNameChange(newValue: String) { planetState = planetState.copy(name = newValue) }
    fun onRotationChange(newValue: String) { planetState = planetState.copy(rotationPeriod = newValue) }
    fun onOrbitalChange(newValue: String) { planetState = planetState.copy(orbitalPeriod = newValue) }
    fun onClimateChange(newValue: String) { planetState = planetState.copy(climate = newValue) }
    fun onTerrainChange(newValue: String) { planetState = planetState.copy(terrain = newValue) }
    fun onPopulationChange(newValue: String) { planetState = planetState.copy(population = newValue) }
    fun onGravityChange(newValue: String) { planetState = planetState.copy(gravity = newValue) }
    fun onDiameterChange(newValue: String) { planetState = planetState.copy(diameter = newValue) }
    fun onCreatedChange(newValue: String) { planetState = planetState.copy(created = newValue) }
    fun onColonizedChange(newValue: Boolean) { planetState = planetState.copy(isColonized = newValue) }

    /**
     * Intenta guardar el planeta.
     * @return null si tuvo éxito, o un String con el mensaje de error si falló.
     */
    suspend fun savePlanet(): String? {

        val name = planetState.name.trim()
        val rotation = planetState.rotationPeriod.trim()
        val orbital = planetState.orbitalPeriod.trim()
        val diameter = planetState.diameter.trim()
        val population = planetState.population.trim()
        val created = planetState.created.trim()
        val gravity = planetState.gravity.trim()

        if (name.isEmpty()) return "El nombre es obligatorio"

        if (planetState.climate.isBlank()) return "El clima es obligatorio"

        if (planetState.terrain.isBlank()) return "El terreno es obligatorio"

        if (rotation.toIntOrNull() == null || rotation.toInt() < 0)
            return "Rotación debe ser un número positivo"

        if (orbital.toIntOrNull() == null || orbital.toInt() < 0)
            return "Periodo orbital debe ser un número positivo"

        if (diameter.toIntOrNull() == null || diameter.toInt() <= 0)
            return "Diámetro debe ser mayor que 0"

        if (!created.matches(Regex("^\\d{2}-\\d{2}-\\d{4}$")))
            return "La fecha debe ser DD-MM-YYYY"


        val planetToSave = planetState.copy(
            name = name,
            rotationPeriod = rotation,
            orbitalPeriod = orbital,
            diameter = diameter,
            population = population,
            created = created,
            gravity = gravity
        )

        val result = if (editMode) {
            repository.update(planetToSave)
        } else {
            repository.save(planetToSave)
        }

        return when (result) {
            is BaseResult.Success -> null
            is BaseResult.Error -> result.exception.message ?: "Error desconocido"
        }
    }

}