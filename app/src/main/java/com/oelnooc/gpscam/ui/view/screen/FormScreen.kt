package com.oelnooc.gpscam.ui.view.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.oelnooc.gpscam.ui.viewmodel.MainViewModel
import getUbicacion

@Composable
fun FormScreen(
    viewModel: MainViewModel,
    onNavigateToMap: () -> Unit,
    onNavigateToPhotoViewer: () -> Unit,
    onCapturePhoto: () -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = viewModel.placeName.value,
            onValueChange = { viewModel.placeName.value = it },
            label = { Text("Nombre del lugar visitado") }
        )

        Button(onClick = {
            getUbicacion(context) { location ->
                viewModel.latitude.value = location.latitude
                viewModel.longitude.value = location.longitude
            }
        }) {
            Text("Obtener ubicación")
        }

        Text("Latitud: ${viewModel.latitude.value}, Longitud: ${viewModel.longitude.value}")

        Button(onClick = onCapturePhoto) {
            Text("Capturar foto")
        }

        if (viewModel.photos.isNotEmpty()) {
            LazyRow {
                items(viewModel.photos.toList()) { uri ->
                    Image(
                        painter = rememberAsyncImagePainter(model = uri),
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .padding(4.dp)
                    )
                }
            }
        }

        Button(onClick = onNavigateToPhotoViewer) {
            Text("Ver fotos")
        }

        Button(onClick = onNavigateToMap) {
            Text("Ver en el mapa")
        }
    }
}
