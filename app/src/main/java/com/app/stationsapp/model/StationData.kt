package com.app.stationsapp.model

data class StationData(
    val lat: Double,
    val lng: Double,
    val land: String,
    val namen: Namen
)

data class Namen(
    val kort: String
)