package be.dekade.weekplanner.modules

import android.content.Context
import be.dekade.weekplanner.data.ActiviteitenDagGegevensDao
import be.dekade.weekplanner.data.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun provideActiviteitDao(appDatabase: AppDatabase): ActiviteitenDagGegevensDao{
        return appDatabase.activiteitDao()
    }
}