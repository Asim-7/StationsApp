package com.app.stationsapp.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.stationsapp.model.StationData
import com.app.stationsapp.respository.StationsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StationsViewModel @Inject constructor(
    private val repository: StationsRepository        // here the HelpRepository is an interface because it helps this view model to be tested with both DEFAULT and FAKE repository
) : ViewModel() {

    private var _stationsList = mutableStateListOf<StationData>()
    val stationsList: List<StationData>
        get() = _stationsList

    var rawStationsList: List<StationData> = listOf()

    init {
        getStationData()
    }

    private fun getStationData() {
        viewModelScope.launch {
            rawStationsList = repository.getStationsData()
        }
    }

    fun stationsWithin(latitude: Double, longitude: Double, radius: Double) {
        // TODO
    }
}