package com.mtu.ceit.hhk.traget.di

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mtu.ceit.hhk.traget.data.ClientDB
import com.mtu.ceit.hhk.traget.ui.AddClientFragment
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDb( app: Application , callBack:ClientDB.CallBack):ClientDB =
        Room.databaseBuilder(app,ClientDB::class.java,"client_database")
                .fallbackToDestructiveMigration()
                .addCallback(callBack)
                .build()

    @Provides
    fun provideDAO(db:ClientDB) = db.getDAO()

    @Provides
    @Singleton
    @ApplicationScope
    fun provideScope() = CoroutineScope(SupervisorJob())





}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope
