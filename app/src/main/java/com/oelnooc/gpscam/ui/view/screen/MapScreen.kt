package com.oelnooc.gpscam.ui.view.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun MapScreen(latitude: Double, longitude: Double, onBack: () -> Unit) {
    Column {
        Button(onClick = onBack) {
            Text("Volver")
        }

        AndroidView(factory = { context ->
            MapView(context).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                val geoPoint = GeoPoint(latitude, longitude)
                overlays.add(Marker(this).apply {
                    position = geoPoint
                })
                controller.setCenter(geoPoint)
                controller.setZoom(15.0)
            }
        })
    }
}