package com.dam.planetstarwars.data.model.repository

import com.dam.planetstarwars.data.dao.FilmDAO
import com.dam.planetstarwars.data.model.Film
import com.dam.planetstarwars.data.model.FilmPlanetCrossRef
import com.dam.planetstarwars.data.model.FilmWithPlanets
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FilmRepository @Inject constructor(
    private val filmDAO: FilmDAO
) {
    fun getData(): Flow<List<Film>> = filmDAO.getAllFilms()

    suspend fun getFilmById(id: Long): Film? = filmDAO.getFilmById(id)

    fun getFilmsByPlanet(planetId: Long): Flow<List<Film>> = filmDAO.getFilmsByPlanet(planetId)

    fun getFilmWithPlanetsById(filmId: Long): Flow<FilmWithPlanets> = filmDAO.getFilmWithPlanetsById(filmId)

    suspend fun insert(film: Film): Long = filmDAO.insert(film)

    suspend fun update(film: Film) = filmDAO.update(film)

    suspend fun delete(film: Film) = filmDAO.delete(film)

    suspend fun exists(title: String): Boolean = filmDAO.exists(title) > 0

    suspend fun insertCrossRef(crossRef: FilmPlanetCrossRef) = filmDAO.insertCrossRef(crossRef)

    suspend fun deleteCrossRef(crossRef: FilmPlanetCrossRef) = filmDAO.deleteCrossRef(crossRef)
}
