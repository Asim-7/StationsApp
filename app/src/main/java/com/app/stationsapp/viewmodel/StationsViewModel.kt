package com.app.stationsapp.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.stationsapp.model.StationData
import com.app.stationsapp.respository.StationsRepository
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import kotlinx.coroutines.launch

class StationsViewModel(
    private val repository: StationsRepository        // here the HelpRepository is an interface because it helps this view model to be tested with both DEFAULT and FAKE repository
) : ViewModel() {

    private var _stationsList = mutableStateListOf<StationData>()
    val stationsList: List<StationData>
        get() = _stationsList

    var allStationsList: List<StationData> = listOf()
    private var stationsWithoutMarker: ArrayList<StationData> = ArrayList()

    init {
        getStationData()
    }

    private fun getStationData() {
        viewModelScope.launch {
            allStationsList = repository.getStationsData()
            stationsWithoutMarker = ArrayList(allStationsList)
        }
    }

    fun stationsWithin(latLngBound: LatLngBounds?) {
        if (latLngBound != null && stationsWithoutMarker.isNotEmpty()) {
            val newStations = ArrayList<StationData>()
            val iterator = stationsWithoutMarker.iterator()
            while (iterator.hasNext()) {
                val station = iterator.next()
                if (latLngBound.contains(LatLng(station.lat, station.lng))) {
                    iterator.remove()
                    newStations.add(station)
                }
            }
            if (newStations.isNotEmpty()) {
                _stationsList.addAll(newStations)
            }
        }
    }
}