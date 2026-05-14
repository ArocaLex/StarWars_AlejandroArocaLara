package com.dam.planetstarwars.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dam.planetstarwars.data.model.Planet
import kotlinx.coroutines.flow.Flow

@Dao
interface PlanetDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(planet: Planet): Long

    @Delete
    suspend fun delete(planet: Planet)

    @Update
    suspend fun update(planet: Planet)

    @Query("Select * from planets")
    fun getAllPlanet(): Flow<List<Planet>>

    @Query("SELECT * FROM planets WHERE planetId = :id")
    suspend fun getPlanetById(id: Long): Planet?

    @Query("SELECT COUNT(*) FROM planets WHERE name = :name")
    suspend fun exists(name: String): Int

    @Query("SELECT COUNT(*) FROM planets")
    suspend fun count(): Int
}
