package com.example.attractiveplaces.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.attractiveplaces.R
import com.example.attractiveplaces.data.Place
import com.example.attractiveplaces.viewmodels.PlaceViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import java.util.Locale

@Composable
fun MapScreen(
    placeViewModel: PlaceViewModel,
    onMapClick: (LatLng) -> Unit
) {
    val places by placeViewModel.places.collectAsState()
    val isLoading by placeViewModel.isLoading.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var selectedPlace by remember { mutableStateOf<Place?>(null) }
    var showInstruction by remember { mutableStateOf(true) }

    val oslo = LatLng(59.9139, 10.7522)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(oslo, 11f)
    }

    LaunchedEffect(Unit) {
        delay(5000L)
        showInstruction = false
    }

    LaunchedEffect(Unit) {
        placeViewModel.showSnackbar.collectLatest {
            snackbarHostState.showSnackbar(it)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                onMapClick = {
                    selectedPlace = null
                    onMapClick(it)
                }
            ) {
                places.forEach { place ->
                    Marker(
                        state = rememberMarkerState(key = place.id.toString(), position = LatLng(place.latitude, place.longitude)),
                        title = place.name,
                        onClick = {
                            selectedPlace = place
                            true
                        }
                    )
                }
            }

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            AnimatedVisibility(
                visible = showInstruction,
                modifier = Modifier.align(Alignment.TopCenter),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .background(Color.Black.copy(alpha = 0.7f), shape = RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Trykk på kartet for å legge til et nytt sted",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            selectedPlace?.let { place ->
                val cameraPosition = cameraPositionState.position

                cameraPositionState.projection?.let { projection ->
                    val screenPosition = projection.toScreenLocation(LatLng(place.latitude, place.longitude))
                    var infoWindowSize by remember { mutableStateOf(IntSize.Zero) }
                    val markerHeight = 120

                    Box(
                        modifier = Modifier
                            .offset {
                                IntOffset(
                                    x = screenPosition.x - (infoWindowSize.width / 2),
                                    y = screenPosition.y - infoWindowSize.height - markerHeight
                                )
                            }
                            .onSizeChanged { infoWindowSize = it }
                    ) {
                        Box(
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colorScheme.surface,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .border(
                                    1.dp,
                                    Color.Black,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .padding(12.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                val lat = String.format(Locale.US, "%.4f", place.latitude)
                                val lng = String.format(Locale.US, "%.4f", place.longitude)

                                Text(place.name, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(8.dp))
                                place.description?.let {
                                    Text(it, style = MaterialTheme.typography.bodyMedium)
                                }
                                place.streetAddress?.let {
                                    Text(it, style = MaterialTheme.typography.bodyMedium)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("GPS: $lat, $lng", style = MaterialTheme.typography.bodySmall)
                            }
                            Icon(
                                painter = painterResource(id = R.drawable.ic_exit),
                                contentDescription = "Close",
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .size(14.dp)
                                    .clickable { selectedPlace = null }
                            )
                        }
                    }
                }
            }
        }
    }
}