package com.faa.watcher.di

import com.faa.watcher.core.data.sources.local.DishesLocalDataSource
import com.faa.watcher.core.data.sources.local.DishesLocalDataSourceImpl
import com.faa.watcher.core.data.sources.network.DishesNetworkDataSource
import com.faa.watcher.core.data.sources.network.DishesNetworkDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface SourcesModule {

    @Binds
    @Singleton
    fun provideNetworkDishesDataSource(dishesDataSource: DishesNetworkDataSourceImpl): DishesNetworkDataSource

    @Binds
    @Singleton
    fun provideLocalDishesDataSource(dishesDataSource: DishesLocalDataSourceImpl): DishesLocalDataSource
}