package com.dam.planetstarwars.data.di

import android.content.Context
import android.content.res.Resources
import com.dam.planetstarwars.data.dao.PlanetDAO
import com.dam.planetstarwars.data.dao.StarWarsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providerResources(@ApplicationContext context: Context): Resources {
        return context.resources
    }

    @Provides
    @Singleton
    fun provideStarWarsDatabase(@ApplicationContext context: Context): StarWarsDatabase {

        return StarWarsDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun providePlanetDao(db: StarWarsDatabase): PlanetDAO {
        return db.getPlanetDao()
    }

}