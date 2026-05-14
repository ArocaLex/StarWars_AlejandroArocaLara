package com.dam.planetstarwars.data.model.repository


import com.dam.planetstarwars.data.dao.PlanetDAO
import com.dam.planetstarwars.data.model.Planet
import com.dam.planetstarwars.network.BaseResult
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class PlanetRepository @Inject constructor(
    private val planetDAO: PlanetDAO
) {

    /**
     * Obtiene el flujo de datos directamente de la BD.
     * Cualquier cambio (insert/delete/update) se reflejará aquí automáticamente.
     */
    fun getData(): Flow<List<Planet>> = planetDAO.getAllPlanet()

    /**
     * Valida las Reglas de Negocio
     */
    private fun validatePlanet(planet: Planet): String? {

        if (planet.name.isBlank() || planet.rotationPeriod.isBlank() || planet.orbitalPeriod.isBlank())
            return "Nombre, rotación y órbita son obligatorios "

        if (planet.name.length < 3)
            return "El nombre debe tener al menos 3 caracteres "

        val nameRegex = Regex("^[a-zA-Z0-9- ,]+$")
        if (!planet.name.matches(nameRegex))
            return "El nombre contiene caracteres inválidos"

        if (!planet.created.matches(Regex("\\d{2}-\\d{2}-\\d{4}")))
            return "La fecha debe ser DD-MM-YYYY "

        return null
    }

    /**
     * Guarda un planeta.

     */
    suspend fun save(planet: Planet): BaseResult<Planet> {

        val validationError = validatePlanet(planet)
        if (validationError != null) return BaseResult.Error(Exception(validationError))

        try {

            val count = planetDAO.exists(planet.name)

            if (count == 0) {

                planetDAO.insert(planet)
                return BaseResult.Success(planet)
            } else {
                return BaseResult.Error(Exception("Error: El planeta ya existe "))
            }
        } catch (e: Exception) {
            return BaseResult.Error(e)
        }
    }

    /**
     * Borra un planeta.
     */
    suspend fun delete(planet: Planet): BaseResult<Planet> {
        try {

            val count = planetDAO.exists(planet.name)

            if (count > 0) {

                planetDAO.delete(planet)
                return BaseResult.Success(planet)
            } else {
                return BaseResult.Error(Exception("No existe el planeta a eliminar"))
            }
        } catch (e: Exception) {
            return BaseResult.Error(e)
        }
    }

    suspend fun update(planet: Planet): BaseResult<Planet> {

        val validationError = validatePlanet(planet)
        if (validationError != null)
            return BaseResult.Error(Exception(validationError))

        try {

            val count = planetDAO.exists(planet.name)

            if (count > 0) {
                planetDAO.update(planet)
                return BaseResult.Success(planet)
            } else {
                return BaseResult.Error(Exception("No se puede actualizar: El planeta no existe"))
            }
        } catch (e: Exception) {
            return BaseResult.Error(e)
        }
    }
}