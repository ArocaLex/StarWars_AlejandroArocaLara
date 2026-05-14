package com.dam.planetstarwars.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.dam.planetstarwars.data.model.Film
import com.dam.planetstarwars.data.model.FilmPlanetCrossRef
import com.dam.planetstarwars.data.model.FilmWithPlanets
import kotlinx.coroutines.flow.Flow

@Dao
interface FilmDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(film: Film): Long

    @Delete
    suspend fun delete(film: Film)

    @Update
    suspend fun update(film: Film)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrossRef(crossRef: FilmPlanetCrossRef)

    @Delete
    suspend fun deleteCrossRef(crossRef: FilmPlanetCrossRef)

    @Query("SELECT * FROM films")
    fun getAllFilms(): Flow<List<Film>>

    @Query("SELECT * FROM films WHERE filmId = :id")
    suspend fun getFilmById(id: Long): Film?

    @Query("""
        SELECT Films.* FROM Films
        INNER JOIN film_planet_cross_ref ON Films.filmId = film_planet_cross_ref.filmId
        WHERE film_planet_cross_ref.planetId = :planetId
    """)
    fun getFilmsByPlanet(planetId: Long): Flow<List<Film>>

    @Query("SELECT COUNT(*) FROM films WHERE title = :title")
    suspend fun exists(title: String): Int

    @Transaction
    @Query("SELECT * FROM Films WHERE filmId = :filmId")
    fun getFilmWithPlanetsById(filmId: Long): Flow<FilmWithPlanets>
}
