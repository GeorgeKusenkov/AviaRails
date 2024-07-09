package com.egasmith.api.utils

import android.content.Context
import android.util.Log
import java.io.IOException

interface JsonReader {
    fun getJsonFromAssets(fileName: String): String?
}

class AssetJsonReader(private val context: Context) : JsonReader {
    override fun getJsonFromAssets(fileName: String): String? {
        Log.d("AssetJsonReader", "Attempting to read file: $fileName")

        return try {
            val json = context.assets.open(fileName).bufferedReader().use { it.readText() }
            Log.d("AssetJsonReader", "Successfully read $fileName: $json")
            json
        } catch (ex: IOException) {
            Log.e("AssetJsonReader", "Error reading $fileName", ex)
            null
        }
    }
}