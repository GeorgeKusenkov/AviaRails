package com.egasmith.api

import com.egasmith.api.models.offer.OfferResponseDTO
import com.egasmith.api.models.ticket.TicketResponseDTO
import com.egasmith.api.models.ticketoffers.TicketOffersResponseDTO
import com.egasmith.api.utils.AssetJsonReader
import com.egasmith.api.utils.MockInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


interface AviaRailsApi {

    @GET("tickets")
    suspend fun getTickets(): TicketResponseDTO

    @GET("tickets_offers")
    suspend fun getTicketOffers(): TicketOffersResponseDTO

    @GET("offers")
    suspend fun getOffers(): OfferResponseDTO
}

class AviaRailsApiProvider (jsonReader: AssetJsonReader) {
    private val mockInterceptor = MockInterceptor(jsonReader)
//    private val aviaRailsApiKeyInterceptor = AviaRailsApiKeyInterceptor(BuildConfig.API_KEY)

    fun provideAviaRailsApi(): AviaRailsApi {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(mockInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        return retrofit.create(AviaRailsApi::class.java)
    }
}

