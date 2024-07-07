package com.egasmith.di

import com.egasmith.api.AviaRailsApi
import com.egasmith.data.TicketRepositoryImpl
import com.egasmith.domain.TicketRepository

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideTicketRepository(aviaRailsApi: AviaRailsApi): TicketRepository {
        return TicketRepositoryImpl(aviaRailsApi)
    }
}