package com.dam.planetstarwars.data.model.repository

import com.dam.planetstarwars.data.dao.FilmDAO
import com.dam.planetstarwars.data.model.Film
import com.dam.planetstarwars.network.BaseResult
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class FilmRepository @Inject constructor(
    private val filmDao: FilmDAO
) {
    fun getData(): Flow<List<Film>> = filmDao.getAllFilms()

    private fun validateFilm(film: Film): String? {
        if (film.title.isBlank() || film.director.isBlank())
            return "Título y director son obligatorios"
        
        if (film.title.length < 3)
            return "El título debe tener al menos 3 caracteres"
            
        return null
    }

    suspend fun save(film: Film): BaseResult<Film> {
        val validationError = validateFilm(film)
        if (validationError != null) return BaseResult.Error(Exception(validationError))

        return try {
            val count = filmDao.exists(film.title)
            if (count == 0) {
                filmDao.insert(film)
                BaseResult.Success(film)
            } else {
                BaseResult.Error(Exception("Error: La película ya existe"))
            }
        } catch (e: Exception) {
            BaseResult.Error(e)
        }
    }

    suspend fun delete(film: Film): BaseResult<Film> {
        return try {
            val count = filmDao.exists(film.title)
            if (count > 0) {
                filmDao.delete(film)
                BaseResult.Success(film)
            } else {
                BaseResult.Error(Exception("No existe la película a eliminar"))
            }
        } catch (e: Exception) {
            BaseResult.Error(e)
        }
    }

    suspend fun update(film: Film): BaseResult<Film> {
        val validationError = validateFilm(film)
        if (validationError != null) return BaseResult.Error(Exception(validationError))

        return try {
            val count = filmDao.exists(film.title)
            if (count > 0) {
                filmDao.update(film)
                BaseResult.Success(film)
            } else {
                BaseResult.Error(Exception("No se puede actualizar: La película no existe"))
            }
        } catch (e: Exception) {
            BaseResult.Error(e)
        }
    }
}
