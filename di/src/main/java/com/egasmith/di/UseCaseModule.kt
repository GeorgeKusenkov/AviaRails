package com.egasmith.di

import com.egasmith.domain.TicketRepository
import com.egasmith.domain.usecases.GetOffersUseCase
import com.egasmith.domain.usecases.GetTicketOffersUseCase
import com.egasmith.domain.usecases.GetTicketsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideGetTicketsUseCase(repository: TicketRepository): GetTicketsUseCase {
        return GetTicketsUseCase(repository)
    }

    @Provides
    fun provideGetTicketOffersUseCase(repository: TicketRepository): GetTicketOffersUseCase {
        return GetTicketOffersUseCase(repository)
    }

    @Provides
    fun provideGetOffersUseCase(repository: TicketRepository): GetOffersUseCase {
        return GetOffersUseCase(repository)
    }
}