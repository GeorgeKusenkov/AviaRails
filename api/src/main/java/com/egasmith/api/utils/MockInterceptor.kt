package com.egasmith.api.utils

import android.util.Log
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockInterceptor @Inject constructor(private val jsonReader: AssetJsonReader): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val uri = request.url.toUri().toString()

        val responseString = when (uri) {
            "https://api.aviarails.dev/v1/tickets_offers" -> jsonReader.getJsonFromAssets("tickets_offers.json")
            "https://api.aviarails.dev/v1/tickets" -> jsonReader.getJsonFromAssets("tickets.json")
            "https://api.aviarails.dev/v1/offers" -> jsonReader.getJsonFromAssets("offers.json")
            else -> null
        }

        return if (responseString != null) {
            Log.d("MockInterceptor", "Returning mock response for: $uri")
            Log.d("MockInterceptor", "responseString: $responseString")
            Response.Builder()
                .request(request)
                .code(200)
                .message("OK")
                .body(responseString.toResponseBody("application/json".toMediaTypeOrNull()))
                .addHeader("content-type", "application/json")
                .protocol(Protocol.HTTP_1_1)
                .build()
        } else {
            Log.d("MockInterceptor", "No mock response found for: $uri")
            chain.proceed(request)
        }
    }
}