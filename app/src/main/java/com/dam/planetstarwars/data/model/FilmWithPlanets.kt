package com.dam.planetstarwars.data.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class FilmWithPlanets(
    @Embedded val film: Film,
    @Relation(
        parentColumn = "filmId",         // ID de la película en la tabla Film
        entityColumn = "planetId",       // ID del planeta en la tabla Planet
        associateBy = Junction(FilmPlanetCrossRef::class) // La tabla que los une
    )
    val planets: List<Planet>
)
