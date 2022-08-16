package be.dekade.weekplanner.modules

import android.content.Context
import be.dekade.weekplanner.data.ActiviteitenDagGegevensDao
import be.dekade.weekplanner.helpers.migration
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.realm.Realm
import io.realm.RealmConfiguration
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    private val realmVersion = 2L

    @Singleton
    @Provides
    fun providesRealmConfig(): RealmConfiguration =
        RealmConfiguration.Builder()
            .schemaVersion(realmVersion)
            .migration(migration)
            .build()

    @Provides
    fun provideActiviteitDao(configuration: RealmConfiguration): ActiviteitenDagGegevensDao{
        return ActiviteitenDagGegevensDao(configuration)
    }
}