package com.app.stationsapp.repository

import com.app.stationsapp.model.Namen
import com.app.stationsapp.model.StationData
import com.app.stationsapp.respository.StationsRepository

/**
* This class is used for testing only
* */
class StationsRepositoryTest : StationsRepository {

    // adding real 10 stations as sample data to avoid JSON parsing for testing
    private val sampleStationData = listOf(
        StationData(lat = 52.0888900756836, lng = 5.11027765274048, land = "NL", namen = Namen(kort = "Utrecht C")),
        StationData(lat = 52.087776184082, lng = 5.13138866424561, land = "NL", namen = Namen(kort = "Maliebaan")),
        StationData(lat = 52.078889, lng = 5.121667, land = "NL", namen = Namen(kort = "VaartscheR")),
        StationData(lat = 52.103055, lng = 5.09, land = "NL", namen = Namen(kort = "Zuilen")),
        StationData(lat = 52.09896, lng = 5.06523, land = "NL", namen = Namen(kort = "LeidscheRn")),
        StationData(lat = 52.1100006103516, lng = 5.12472200393677, land = "NL", namen = Namen(kort = "Overvecht")),
        StationData(lat = 52.0655555725098, lng = 5.14416646957397, land = "NL", namen = Namen(kort = "Lunetten")),
        StationData(lat = 52.03402, lng = 5.16821, land = "NL", namen = Namen(kort = "Houten")),
        StationData(lat = 52.01701, lng = 5.17949, land = "NL", namen = Namen(kort = "Castellum")),
        StationData(lat = 51.9213265245505, lng = 6.57862722873687, land = "NL", namen = Namen(kort = "Aalten"))
    )

    override suspend fun getStationsData(): List<StationData> {
        return sampleStationData
    }
}