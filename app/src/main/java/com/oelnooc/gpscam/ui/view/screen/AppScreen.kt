package com.oelnooc.gpscam.ui.view.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.oelnooc.gpscam.ui.viewmodel.MainViewModel

@Composable
fun AppScreen(viewModel: MainViewModel, onCapturePhoto: () -> Unit) {
    val currentScreen = remember { mutableStateOf("FORM") }

    when (currentScreen.value) {
        "FORM" -> FormScreen(
            viewModel,
            onNavigateToMap = { currentScreen.value = "MAP" },
            onNavigateToPhotoViewer = { currentScreen.value = "PHOTO_VIEWER" },
            onCapturePhoto = onCapturePhoto
        )
        "MAP" -> MapScreen(viewModel.latitude.value, viewModel.longitude.value) {
            currentScreen.value = "FORM"
        }
        "PHOTO_VIEWER" -> PhotoViewerScreen(viewModel.photos) {
            currentScreen.value = "FORM"
        }
    }
}