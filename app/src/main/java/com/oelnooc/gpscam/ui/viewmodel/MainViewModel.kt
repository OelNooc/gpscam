package com.oelnooc.gpscam.ui.viewmodel

import android.content.pm.ActivityInfo
import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    private val _orientation = MutableLiveData<Int>()
    val orientation: LiveData<Int> get() = _orientation

    val placeName = mutableStateOf("")
    val photos = mutableStateListOf<Uri>()
    val latitude = mutableStateOf(0.0)
    val longitude = mutableStateOf(0.0)

    init {
        _orientation.value = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }
}