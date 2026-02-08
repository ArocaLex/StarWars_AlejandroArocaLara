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

@Database(
    version = 1,
    entities = [Planet::class],
    exportSchema = false
)
abstract class StarWarsDatabase : RoomDatabase() {


    abstract fun getPlanetDao(): PlanetDAO


    companion object {
        @Volatile
        private var INSTANCE: StarWarsDatabase? = null

        fun getDatabase(context: Context): StarWarsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StarWarsDatabase::class.java,
                    "starwars_database.db"
                ).fallbackToDestructiveMigration().addCallback(StarWarsDatabaseCallback()).build()
                INSTANCE = instance
                instance
            }
        }

        private class StarWarsDatabaseCallback : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                INSTANCE?.let { database ->
                    // Usamos un Scope IO para operaciones de base de datos
                    // Esto reemplaza al Executor + runBlocking
                    CoroutineScope(Dispatchers.IO).launch {
                        prepopulateDatabase(database)
                    }
                }
            }
        }

        suspend fun prepopulateDatabase(database: StarWarsDatabase) {
            val planetDao = database.getPlanetDao()

            // 1. Tatooine
            planetDao.insert(
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

            // 2. Alderaan
            planetDao.insert(
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

            planetDao.insert(
                Planet(
                    name = "Yavin IV",
                    rotationPeriod = "24",
                    orbitalPeriod = "4818",
                    climate = "${Climate.TEMPERATE.textoInterfaz}, ${Climate.TROPICAL.textoInterfaz}",
                    terrain = "${Terrain.SWAMP.textoInterfaz}, ${Terrain.GRASSLANDS.textoInterfaz}",
                    population = "1000",
                    gravity = "1 standard",
                    diameter = "10200",
                    created = "25-05-1977",
                    isColonized = false
                )
            )


            planetDao.insert(
                Planet(
                    name = "Hoth",
                    rotationPeriod = "23",
                    orbitalPeriod = "549",
                    climate = Climate.FROZEN.textoInterfaz,
                    terrain = "${Terrain.TUNDRA.textoInterfaz}, ${Terrain.ICE_CAVES.textoInterfaz}",
                    population = "unknown",
                    gravity = "1.1 standard",
                    diameter = "7200",
                    created = "21-05-1980",
                    isColonized = false
                )
            )


            planetDao.insert(
                Planet(
                    name = "Dagobah",
                    rotationPeriod = "23",
                    orbitalPeriod = "341",
                    climate = Climate.MURKY.textoInterfaz,
                    terrain = Terrain.SWAMP.textoInterfaz,
                    population = "unknown",
                    gravity = "N/A",
                    diameter = "8900",
                    created = "21-05-1980",
                    isColonized = false
                )
            )
        }
    }
}