package com.oelnooc.gpscam.ui.view

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import com.oelnooc.gpscam.ui.theme.GPSCamTheme
import com.oelnooc.gpscam.ui.view.screen.AppScreen
import com.oelnooc.gpscam.ui.viewmodel.MainViewModel
import org.osmdroid.config.Configuration
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var takePictureLauncher: ActivityResultLauncher<Uri>
    private var photoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Configuration.getInstance().userAgentValue = packageName

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val cameraPermissionGranted = permissions[Manifest.permission.CAMERA] == true
            val locationPermissionGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true

            if (!cameraPermissionGranted || !locationPermissionGranted) {
                showPermissionRationaleDialog(cameraPermissionGranted, locationPermissionGranted)
            } else {
                Log.d("Permissions", "Todos los permisos fueron otorgados.")
            }
        }

        takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                Log.d("MainActivity", "Foto tomada con éxito")
                if (photoUri != null) {
                    mainViewModel.photos.add(photoUri!!)
                    photoUri = null
                } else {
                    Log.e("MainActivity", "URI nula después de tomar la foto")
                }
            } else {
                Log.e("MainActivity", "Error al tomar la foto")
            }
        }

        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )

        mainViewModel.orientation.observe(this, Observer { orientation ->
            requestedOrientation = orientation
        })

        setContent {
            GPSCamTheme {
                AppScreen(mainViewModel, ::capturePhoto)
            }
        }
    }

    private fun capturePhoto() {
        if (checkPermissions()) {
            val photoFile = createImageFile()
            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(this, "$packageName.fileprovider", photoFile)

                if (photoUri != null) {
                    val contentResolver = contentResolver
                    try {
                        contentResolver.openFileDescriptor(photoUri!!, "rw")?.use {
                            Log.d("MainActivity", "URI válida y con permisos para escritura.")
                        }

                        takePictureLauncher.launch(photoUri!!)

                    } catch (e: IOException) {
                        Log.e("MainActivity", "No se pueden obtener permisos de escritura para la URI", e)
                    }

                } else {
                    Log.e("MainActivity", "Error: La URI de la foto es nula")
                }
            } else {
                Log.e("MainActivity", "Error: No se pudo crear el archivo de imagen")
            }
        } else {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun checkPermissions(): Boolean {
        val cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        val locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)

        return cameraPermission == PackageManager.PERMISSION_GRANTED && locationPermission == PackageManager.PERMISSION_GRANTED
    }

    private fun createImageFile(): File? {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        if (storageDir != null) {
            try {
                val imageFile = File.createTempFile("JPEG_${timestamp}_", ".jpg", storageDir)
                Log.d("MainActivity", "Archivo creado: ${imageFile.absolutePath}")
                return imageFile
            } catch (e: IOException) {
                Log.e("MainActivity", "Error al crear el archivo de imagen", e)
            }
        } else {
            Log.e("MainActivity", "Error: El directorio de almacenamiento es nulo")
        }
        return null
    }

    private fun showPermissionRationaleDialog(
        cameraPermissionGranted: Boolean,
        locationPermissionGranted: Boolean
    ) {
        val permissionMessage = buildString {
            if (!cameraPermissionGranted) append("El acceso a la cámara es necesario para tomar fotos. ")
            if (!locationPermissionGranted) append("La ubicación es necesaria para registrar el lugar visitado.")
        }

        AlertDialog.Builder(this)
            .setTitle("Permisos necesarios")
            .setMessage(permissionMessage)
            .setPositiveButton("Aceptar") { dialog, _ ->
                permissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
    }
}