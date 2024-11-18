package com.oelnooc.gpscam.ui.view.screen

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@Composable
fun PhotoViewerScreen(photos: List<Uri>, onBack: () -> Unit) {
    var selectedPhoto by remember { mutableStateOf<Uri?>(null) }

    if (selectedPhoto != null) {
        FullScreenImageScreen(
            uri = selectedPhoto!!,
            onClose = { selectedPhoto = null }
        )
    } else {
        Column {
            Button(onClick = onBack) {
                Text("Volver")
            }

            LazyColumn {
                items(photos) { uri ->
                    Image(
                        painter = rememberAsyncImagePainter(model = uri),
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .clickable {
                                selectedPhoto = uri
                            }
                    )
                }
            }
        }
    }
}