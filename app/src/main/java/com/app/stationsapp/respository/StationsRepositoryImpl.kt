package com.app.stationsapp.respository

import android.content.Context
import com.app.stationsapp.R
import com.app.stationsapp.model.StationData
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import java.io.BufferedReader
import javax.inject.Inject

class StationsRepositoryImpl @Inject constructor(private val context: Context) : StationsRepository {

    override suspend fun getStationsData(): List<StationData> {
        var stationsList: List<StationData> = listOf()
        var reader: BufferedReader? = null

        try {
            // Create a reader and read the file contents
            reader = context.resources.openRawResource(R.raw.stations).bufferedReader()
            val rawData = reader.use { it.readText() }

            // Create a Type token that Gson knows how to parse the raw data
            val cityListType = object : TypeToken<List<StationData>>() {}.type

            // Parse the raw data using Gson
            stationsList = Gson().fromJson(rawData, cityListType)

        } catch (e: Exception) {
            // Handle exception
        } finally {
            // Close the reader to release system resources
            reader?.close()
        }

        return stationsList
    }
}