package com.example.exchangeofhandymen.data.db

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class ProviderWorkerDao {
    @Provides
    fun provideChannelDao(appDatabase: AppDataBase): WorkerDao {
        return appDatabase.workerDao()
    }

    @Provides
    fun provideUserDao(appDatabase: AppDataBase):UserDao {
        return appDatabase.userDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDataBase {
        return Room.databaseBuilder(
            appContext,
            AppDataBase::class.java ,
            "db"
        ).build()
    }
}