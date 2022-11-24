package com.app.stationsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.stationsapp.ui.theme.StationsAppTheme
import com.app.stationsapp.viewmodel.StationsViewModel
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel = hiltViewModel<StationsViewModel>()
            
            // TODO handle recomposition when screen rotates + add markers dynamically + location permission

            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(LatLng(52.087966, 5.113372), 12f)
            }

            StationsAppTheme {
                GoogleMap(
                    cameraPositionState = cameraPositionState,
                    properties = MapProperties(isMyLocationEnabled = true),
                    uiSettings = MapUiSettings(compassEnabled = true)
                ) {
                    GoogleMarkers()
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