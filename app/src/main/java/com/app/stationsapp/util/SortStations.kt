package com.app.stationsapp.util

import com.app.stationsapp.model.StationData
import com.google.android.gms.maps.model.LatLng
import kotlin.math.*

/**
 * This class is needed to compare data for sorting
 * */
class SortStations(private var currentLoc: LatLng) : Comparator<StationData> {
    override fun compare(place1: StationData, place2: StationData): Int {
        val lat1: Double = place1.lat
        val lon1: Double = place1.lng
        val lat2: Double = place2.lat
        val lon2: Double = place2.lng
        val distanceToPlace1 = distance(currentLoc.latitude, currentLoc.longitude, lat1, lon1)
        val distanceToPlace2 = distance(currentLoc.latitude, currentLoc.longitude, lat2, lon2)
        return (distanceToPlace1 - distanceToPlace2).toInt()
    }

    private fun distance(fromLat: Double, fromLon: Double, toLat: Double, toLon: Double): Double {
        val radius = 6378137.0 // approximate Earth radius, *in meters*
        val deltaLat = toLat - fromLat
        val deltaLon = toLon - fromLon
        val angle = 2 * asin(sqrt(sin(deltaLat / 2).pow(2.0) + cos(fromLat) * cos(toLat) * sin(deltaLon / 2).pow(2.0)))
        return radius * angle
    }
}