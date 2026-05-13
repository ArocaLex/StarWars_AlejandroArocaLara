package com.dam.planetstarwars.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dam.planetstarwars.data.model.Film
import kotlinx.coroutines.flow.Flow

@Dao
interface FilmDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(film: Film)

    @Delete
    suspend fun delete(film: Film)

    @Update
    suspend fun update(film: Film)

    @Query("SELECT * FROM films")
    fun getAllFilms(): Flow<List<Film>>

    @Query("SELECT * FROM films WHERE filmId = :id")
    suspend fun getFilmById(id: Int): Film?

    @Query("SELECT COUNT(*) FROM films WHERE title = :title")
    suspend fun exists(title: String): Int
}
