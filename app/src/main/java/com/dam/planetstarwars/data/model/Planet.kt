package com.dam.planetstarwars.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity(tableName = "Planets")
@Parcelize
data class Planet(
    @PrimaryKey(autoGenerate = true)
    val planetId: Int = 0,
    val name: String,
    val rotationPeriod: String,
    val orbitalPeriod: String,
    val climate: String,
    val terrain: String,
    val population: String,
    val gravity: String,
    val diameter: String,
    val created: String,
    val isColonized: Boolean
) : Parcelable