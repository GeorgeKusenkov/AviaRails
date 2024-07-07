package com.egasmith.di


import android.content.Context
import com.egasmith.api.AviaRailsApi
import com.egasmith.api.AviaRailsApiProvider
import com.egasmith.api.utils.AssetJsonReader
import com.egasmith.api.utils.JsonReader
import dagger.Binds
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
    fun provideContext(@ApplicationContext context: Context): Context = context

    @Provides
    @Singleton
    fun provideAssetJsonReader(@ApplicationContext context: Context): AssetJsonReader {
        return AssetJsonReader(context)
    }

    @Provides
    @Singleton
    fun provideAviaRailsApi(jsonReader: AssetJsonReader): AviaRailsApi {
        return AviaRailsApiProvider(jsonReader).provideAviaRailsApi()
    }
}