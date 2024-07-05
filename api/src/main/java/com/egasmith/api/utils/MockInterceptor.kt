package com.egasmith.api.utils

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class MockInterceptor(private val jsonReader: AssetJsonReader) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val uri = request.url.toUri().toString()

        val responseString = when {
            uri.endsWith("tickets") -> jsonReader.getJsonFromAssets("tickets.json")
            uri.endsWith("tickets_offers") -> jsonReader.getJsonFromAssets("tickets_offers.json")
            uri.endsWith("offers") -> jsonReader.getJsonFromAssets("offers.json")
            else -> null
        }

        return if (responseString != null) {
            Response.Builder()
                .request(request)
                .code(200)
                .message("OK")
                .body(responseString.toResponseBody("application/json".toMediaTypeOrNull()))
                .addHeader("content-type", "application/json")
                .protocol(Protocol.HTTP_1_1)
                .build()
        } else {
            chain.proceed(request)
        }
    }
}