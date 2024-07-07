package com.egasmith.api.utils

import android.content.Context
import android.util.Log
import java.io.IOException

interface JsonReader {
    fun getJsonFromAssets(fileName: String): String?
}

class AssetJsonReader (private val context: Context) : JsonReader {
    override fun getJsonFromAssets(fileName: String): String? {
        return try {
            context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ex: IOException) {
            Log.e("AssetJsonReader", "Error reading $fileName", ex)
            null
        }
    }
}