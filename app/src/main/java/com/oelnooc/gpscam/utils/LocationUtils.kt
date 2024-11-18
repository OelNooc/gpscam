import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

fun getUbicacion(context: Context, onUbicacionOk: (Location) -> Unit) {
    val fusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    fusedLocationProviderClient.getCurrentLocation(
        Priority.PRIORITY_HIGH_ACCURACY, null
    ).addOnSuccessListener { location ->
        location?.let { onUbicacionOk(it) }
    }
}