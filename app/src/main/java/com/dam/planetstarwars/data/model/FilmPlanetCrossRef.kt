package com.dam.planetstarwars.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "film_planet_cross_ref",
    primaryKeys = ["filmId", "planetId"],
    foreignKeys = [
        ForeignKey(
            entity = Film::class,
            parentColumns = ["filmId"],
            childColumns = ["filmId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Planet::class,
            parentColumns = ["planetId"],
            childColumns = ["planetId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("planetId")]
)
data class FilmPlanetCrossRef(
    val filmId: Long,
    val planetId: Long
)
