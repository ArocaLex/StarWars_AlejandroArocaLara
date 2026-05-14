package com.dam.planetstarwars.ui.planetScreens.add

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.dam.planetstarwars.data.model.Planet
import com.dam.planetstarwars.data.model.repository.PlanetRepository
import com.dam.planetstarwars.network.BaseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

sealed class PlanetSaveResult {
    object Success : PlanetSaveResult()
    object FieldError : PlanetSaveResult()
    data class Duplicate(val name: String) : PlanetSaveResult()
    data class UnknownError(val message: String) : PlanetSaveResult()
}

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

    var nameError by mutableStateOf<String?>(null)
        private set
    var rotationError by mutableStateOf<String?>(null)
        private set
    var orbitalError by mutableStateOf<String?>(null)
        private set
    var climateError by mutableStateOf<String?>(null)
        private set
    var terrainError by mutableStateOf<String?>(null)
        private set
    var diameterError by mutableStateOf<String?>(null)
        private set
    var createdError by mutableStateOf<String?>(null)
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

    fun onNameChange(newValue: String) { planetState = planetState.copy(name = newValue); nameError = null }
    fun onRotationChange(newValue: String) { planetState = planetState.copy(rotationPeriod = newValue); rotationError = null }
    fun onOrbitalChange(newValue: String) { planetState = planetState.copy(orbitalPeriod = newValue); orbitalError = null }
    fun onClimateChange(newValue: String) { planetState = planetState.copy(climate = newValue); climateError = null }
    fun onTerrainChange(newValue: String) { planetState = planetState.copy(terrain = newValue); terrainError = null }
    fun onPopulationChange(newValue: String) { planetState = planetState.copy(population = newValue) }
    fun onGravityChange(newValue: String) { planetState = planetState.copy(gravity = newValue) }
    fun onDiameterChange(newValue: String) { planetState = planetState.copy(diameter = newValue); diameterError = null }
    fun onCreatedChange(newValue: String) { planetState = planetState.copy(created = newValue); createdError = null }
    fun onColonizedChange(newValue: Boolean) { planetState = planetState.copy(isColonized = newValue) }

    suspend fun savePlanet(): PlanetSaveResult {
        nameError = null
        rotationError = null
        orbitalError = null
        climateError = null
        terrainError = null
        diameterError = null
        createdError = null

        val name = planetState.name.trim()
        val rotation = planetState.rotationPeriod.trim()
        val orbital = planetState.orbitalPeriod.trim()
        val diameter = planetState.diameter.trim()

        if (name.isEmpty()) {
            nameError = "El nombre es obligatorio"
            return PlanetSaveResult.FieldError
        }
        if (planetState.climate.isBlank()) {
            climateError = "El clima es obligatorio"
            return PlanetSaveResult.FieldError
        }
        if (planetState.terrain.isBlank()) {
            terrainError = "El terreno es obligatorio"
            return PlanetSaveResult.FieldError
        }
        if (rotation.toIntOrNull() == null || rotation.toInt() < 0) {
            rotationError = "Debe ser un número positivo"
            return PlanetSaveResult.FieldError
        }
        if (orbital.toIntOrNull() == null || orbital.toInt() < 0) {
            orbitalError = "Debe ser un número positivo"
            return PlanetSaveResult.FieldError
        }
        if (diameter.toIntOrNull() == null || diameter.toInt() <= 0) {
            diameterError = "Debe ser mayor que 0"
            return PlanetSaveResult.FieldError
        }
        if (!planetState.created.trim().matches(Regex("^\\d{2}-\\d{2}-\\d{4}$"))) {
            createdError = "Formato DD-MM-YYYY"
            return PlanetSaveResult.FieldError
        }

        val planetToSave = planetState.copy(
            name = name,
            rotationPeriod = rotation,
            orbitalPeriod = orbital,
            diameter = diameter,
            population = planetState.population.trim(),
            created = planetState.created.trim(),
            gravity = planetState.gravity.trim()
        )

        val result = if (editMode) repository.update(planetToSave) else repository.save(planetToSave)

        return when (result) {
            is BaseResult.Success -> PlanetSaveResult.Success
            is BaseResult.Error -> {
                val msg = result.exception.message ?: "Error desconocido"
                if (msg.contains("ya existe", ignoreCase = true)) {
                    PlanetSaveResult.Duplicate(name)
                } else {
                    PlanetSaveResult.UnknownError(msg)
                }
            }
        }
    }
}
