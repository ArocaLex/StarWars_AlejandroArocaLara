package com.dam.planetstarwars.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "Films")
@Parcelize
data class Film(
    @PrimaryKey(autoGenerate = true)
    val filmId: Long = 0,
    val title: String,
    val episodeId: Int,
    val director: String,
    val producer: String,
    val releaseDate: String
) : Parcelable