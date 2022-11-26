package com.app.stationsapp

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.stationsapp.ui.theme.StationsAppTheme
import com.app.stationsapp.viewmodel.StationsViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@ExperimentalPermissionsApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel = hiltViewModel<StationsViewModel>()

            val multiplePermissionState = rememberMultiplePermissionsState(
                permissions = listOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )

            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(LatLng(52.087966, 5.113372), 12f)
            }

            val refreshMarkers = {
                viewModel.stationsWithin(cameraPositionState.projection?.visibleRegion?.latLngBounds)
            }

            if (!cameraPositionState.isMoving) {
                refreshMarkers()
            }

            LaunchedEffect(Unit) {
                multiplePermissionState.launchMultiplePermissionRequest()
            }

            StationsAppTheme {

                val sheetState = rememberBottomSheetState(
                    initialValue = BottomSheetValue.Collapsed
                )
                val scaffoldState = rememberBottomSheetScaffoldState(
                    bottomSheetState = sheetState
                )
                val scope = rememberCoroutineScope()

                BottomSheetScaffold(
                    scaffoldState = scaffoldState,
                    sheetContent = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Bottom Sheet", fontSize = 15.sp)
                        }
                    },
                    sheetBackgroundColor = Color.Green,
                    sheetPeekHeight = 0.dp
                ) {
                    if (multiplePermissionState.allPermissionsGranted) {
                        GoogleMap(
                            cameraPositionState = cameraPositionState,
                            properties = MapProperties(isMyLocationEnabled = true),
                            uiSettings = MapUiSettings(compassEnabled = true),
                            onMapLoaded = refreshMarkers
                        ) {
                            viewModel.stationsList.forEach { station ->
                                Marker(
                                    state = rememberMarkerState(position = LatLng(station.lat, station.lng)),
                                    title = station.namen.kort,
                                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                                )
                            }
                        }
                    }
                }

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
        }
    }
}

@Composable
fun GoogleMarkers() {
    Marker(
        state = rememberMarkerState(position = LatLng(52.087966, 5.113372)),
        title = "Marker1",
        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    StationsAppTheme {
        //Greeting("Android")
    }
}