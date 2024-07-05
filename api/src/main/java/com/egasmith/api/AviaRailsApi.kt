package com.egasmith.api

import android.content.Context
import com.egasmith.api.models.offer.OfferResponse
import com.egasmith.api.models.ticket.TicketResponse
import com.egasmith.api.models.ticketoffers.TicketOffers
import com.egasmith.api.utils.AssetJsonReader
import com.egasmith.api.utils.MockInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface AviaRailsApi {
    @GET("tickets")
    suspend fun getTickets(): TicketResponse

    @GET("tickets_offers")
    suspend fun getTicketOffers(): TicketOffers

    @GET("offers")
    suspend fun getOffers(): OfferResponse
}

class AviaRailsApiProvider(private val context: Context) {
    fun provideFilmApi(): AviaRailsApi {
        val jsonReader = AssetJsonReader(context)
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(MockInterceptor(jsonReader))
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://localhost/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        return retrofit.create(AviaRailsApi::class.java)
    }
}


