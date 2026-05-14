package com.dam.planetstarwars.data.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dam.planetstarwars.data.model.Climate
import com.dam.planetstarwars.data.model.Planet
import com.dam.planetstarwars.data.model.Terrain
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.dam.planetstarwars.data.model.Film
import com.dam.planetstarwars.data.model.FilmPlanetCrossRef

@Database(
    version = 3,
    entities = [Planet::class, Film::class, FilmPlanetCrossRef::class],
    exportSchema = false
)
abstract class StarWarsDatabase : RoomDatabase() {

    abstract fun getPlanetDao(): PlanetDAO
    abstract fun getFilmDao(): FilmDAO

    companion object {
        @Volatile
        private var INSTANCE: StarWarsDatabase? = null

        fun getDatabase(context: Context): StarWarsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StarWarsDatabase::class.java,
                    "starwars_database.db"
                )
                .fallbackToDestructiveMigration()
                .addCallback(object : Callback() {
                    override fun onOpen(db: SupportSQLiteDatabase) {
                        super.onOpen(db)
                        // Al abrir la BD, comprobamos si está vacía para prepoblar
                        INSTANCE?.let { database ->
                            CoroutineScope(Dispatchers.IO).launch {
                                if (database.getPlanetDao().count() == 0) {
                                    prepopulateDatabase(database)
                                }
                            }
                        }
                    }
                })
                .build()
                INSTANCE = instance
                instance
            }
        }

        private suspend fun prepopulateDatabase(database: StarWarsDatabase) {
            val planetDao = database.getPlanetDao()
            val filmDao = database.getFilmDao()

            // 1. Insertamos Planetas
            val tatooineId = planetDao.insert(
                Planet(
                    name = "Tatooine",
                    rotationPeriod = "23",
                    orbitalPeriod = "304",
                    climate = Climate.ARID.textoInterfaz,
                    terrain = Terrain.DESERT.textoInterfaz,
                    population = "200000",
                    gravity = "1 standard",
                    diameter = "10465",
                    created = "01-01-1977",
                    isColonized = true
                )
            )

            val alderaanId = planetDao.insert(
                Planet(
                    name = "Alderaan",
                    rotationPeriod = "24",
                    orbitalPeriod = "364",
                    climate = Climate.TEMPERATE.textoInterfaz,
                    terrain = "${Terrain.GRASSLANDS.textoInterfaz}, ${Terrain.MOUNTAINS.textoInterfaz}",
                    population = "2000000000",
                    gravity = "1 standard",
                    diameter = "12500",
                    created = "01-01-1977",
                    isColonized = true
                )
            )

            val aNewHopeId = filmDao.insert(
                Film(
                    title = "A New Hope",
                    episodeId = 4,
                    director = "George Lucas",
                    producer = "Gary Kurtz",
                    releaseDate = "1977-05-25"
                )
            )

            val phantomMenaceId = filmDao.insert(
                Film(
                    title = "The Phantom Menace",
                    episodeId = 1,
                    director = "George Lucas",
                    producer = "Rick McCallum",
                    releaseDate = "1999-05-19"
                )
            )

            val revengeOfSithId = filmDao.insert(
                Film(
                    title = "Revenge of the Sith",
                    episodeId = 3,
                    director = "George Lucas",
                    producer = "Rick McCallum",
                    releaseDate = "2005-05-19"
                )
            )

            // Relaciones N:M: Tatooine aparece en los 3 episodios, Alderaan en Ep. IV
            filmDao.insertCrossRef(FilmPlanetCrossRef(filmId = aNewHopeId, planetId = tatooineId))
            filmDao.insertCrossRef(FilmPlanetCrossRef(filmId = aNewHopeId, planetId = alderaanId))
            filmDao.insertCrossRef(FilmPlanetCrossRef(filmId = phantomMenaceId, planetId = tatooineId))
            filmDao.insertCrossRef(FilmPlanetCrossRef(filmId = revengeOfSithId, planetId = tatooineId))
        }
    }
}
