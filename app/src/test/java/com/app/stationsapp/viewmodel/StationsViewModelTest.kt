package com.app.stationsapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.app.stationsapp.MainCoroutineRule
import com.app.stationsapp.model.Namen
import com.app.stationsapp.model.StationData
import com.app.stationsapp.repository.StationsRepositoryTest
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class StationsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()     // this helps to run tests on the same thread one after another

    @get:Rule
    val coroutineRule = MainCoroutineRule()                     // This Rule is needed when testing coroutine, avoiding test failure on Dispatcher.Main

    private lateinit var viewModel: StationsViewModel
    private lateinit var visibleRegionBounds: LatLngBounds      // sample map camera visible bounds
    private val outOfBoundStation = StationData(lat = 51.9213265245505, lng = 6.57862722873687, land = "NL", namen = Namen(kort = "Aalten"))

    @Before
    fun setup() {
        viewModel = StationsViewModel(StationsRepositoryTest()) // using test repository to avoid JSON parsing for testing
        visibleRegionBounds = LatLngBounds(
            LatLng(52.01207273516862, 5.045955665409564),
            LatLng(52.16373023727744, 5.180787965655327)
        )
    }

    @Test
    fun `get stations data, expected size 10 so test success`() {
        val sizeOfList = viewModel.allStationsList.size
        assertThat(sizeOfList).isEqualTo(10)
    }

    @Test
    fun `get stations data, expected size 12 so test failure`() {
        val sizeOfList = viewModel.allStationsList.size
        assertThat(sizeOfList).isNotEqualTo(12)
    }

    @Test
    fun `check visible stations on map, expected visible stations are 9 so test success`() {
        viewModel.stationsWithin(visibleRegionBounds)
        val sizeOfList = viewModel.stationsList.size
        assertThat(sizeOfList).isEqualTo(9)
    }

    @Test
    fun `check visible stations on map, expected visible stations are 12 so test failure`() {
        viewModel.stationsWithin(visibleRegionBounds)
        val sizeOfList = viewModel.stationsList.size
        assertThat(sizeOfList).isNotEqualTo(12)
    }

    @Test
    fun `check visible stations on map with bounds null, expected visible stations are empty so test success`() {
        viewModel.stationsWithin(null)
        assertThat(viewModel.stationsList).isEmpty()
    }

    @Test
    fun `check out of bounds station, expected should not be in the list so test success`() {
        viewModel.stationsWithin(visibleRegionBounds)
        assertThat(viewModel.stationsList).doesNotContain(outOfBoundStation)
    }

}