package com.app.stationsapp

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.stationsapp.model.StationData
import com.app.stationsapp.ui.theme.StationsAppTheme
import com.app.stationsapp.viewmodel.StationsViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@ExperimentalMaterialApi
@ExperimentalPermissionsApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StationsAppTheme {
                MainApp(viewModel = getViewModel())
            }
        }
    }
}

@ExperimentalMaterialApi
@ExperimentalPermissionsApi
@Composable
fun MainApp(viewModel: StationsViewModel) {

    // Location permissions are required for the app
    val multiplePermissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    )

    // The data provided to the camera position is default station data
    val cameraPositionState = rememberCameraPositionState { position = CameraPosition.fromLatLngZoom(LatLng(52.087966, 5.113372), 12f) }
    // Whenever user moves the camera / zoom in/out, stations are checked
    if (!cameraPositionState.isMoving) viewModel.stationsWithin(cameraPositionState.projection?.visibleRegion?.latLngBounds)

    LaunchedEffect(Unit) { multiplePermissionState.launchMultiplePermissionRequest() }  // Check the permission when app starts

    val sheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)    // default state of bottom sheet
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = sheetState)

    StationsBottomSheet(
        scaffoldState = scaffoldState,
        viewModel = viewModel,
        multiplePermissionState = multiplePermissionState,
        cameraPositionState = cameraPositionState
    )

    AllStationsButton(sheetState = sheetState)      // shows/hides list of stations shown in bottom sheet, sorted according to distance from default LatLong
}

@ExperimentalPermissionsApi
@ExperimentalMaterialApi
@Composable
fun StationsBottomSheet(scaffoldState: BottomSheetScaffoldState, viewModel: StationsViewModel, multiplePermissionState: MultiplePermissionsState, cameraPositionState: CameraPositionState) {
    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentAlignment = Alignment.Center
            ) {
                val listState = rememberLazyListState()
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = listState,
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    contentPadding = PaddingValues(10.dp)
                ) {
                    items(viewModel.allStationsList) { item ->
                        ListItem(stationData = item)
                    }
                }
            }
        },
        sheetPeekHeight = 0.dp
    ) {
        StationsMap(multiplePermissionState = multiplePermissionState, viewModel = viewModel, cameraPositionState = cameraPositionState)
    }
}

@Composable
fun ListItem(stationData: StationData) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = stationData.namen.kort)
            Text(text = "(${stationData.lat}, ${stationData.lng})", fontSize = 10.sp, modifier = Modifier.padding(start = 4.dp))
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun AllStationsButton(sheetState: BottomSheetState) {
    val scope = rememberCoroutineScope()

    FloatingActionButton(
        backgroundColor = Color.Red,
        modifier = Modifier.padding(12.dp),
        onClick = {
            scope.launch {
                if (sheetState.isCollapsed) {
                    sheetState.expand()
                } else {
                    sheetState.collapse()
                }
            }
        }
    ) {
        Text(text = "Stations", modifier = Modifier.padding(5.dp), color = Color.White)
    }
}

@ExperimentalPermissionsApi
@Composable
fun StationsMap(multiplePermissionState: MultiplePermissionsState, viewModel: StationsViewModel, cameraPositionState: CameraPositionState) {
    if (multiplePermissionState.allPermissionsGranted) {
        GoogleMap(
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isMyLocationEnabled = true),
            uiSettings = MapUiSettings(compassEnabled = true),
            onMapLoaded = {
                viewModel.stationsWithin(cameraPositionState.projection?.visibleRegion?.latLngBounds)
            }
        ) {
            viewModel.stationsList.forEach { station ->     // Markers are dynamically added if the map region is visible to user
                Marker(
                    state = rememberMarkerState(position = LatLng(station.lat, station.lng)),
                    title = station.namen.kort,
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                )
            }
        }
    }
}