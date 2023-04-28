package com.faa.watcher.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DispatcherDefault

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DispatcherIO

@Module
@InstallIn(SingletonComponent::class)
class ProvideModules {

    @DispatcherIO
    @Provides
    fun getDispatchersIo() = Dispatchers.IO

    @DispatcherDefault
    @Provides
    fun getDispatchersDefault() = Dispatchers.Default

    @Provides
    @Singleton
    fun getCoroutineScope() = CoroutineScope(SupervisorJob())
}