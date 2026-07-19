package com.activemap.platform

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.activemap.model.GeoLocation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class AndroidLocationProvider(private val context: Context) : LocationProvider {

    private val locationManager: LocationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    override suspend fun getCurrentLocation(): GeoLocation? {
        if (!hasPermission()) return null
        return requestSingleUpdate()
    }

    override suspend fun hasPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
    }

    override suspend fun requestPermission(): Boolean {
        return hasPermission()
    }

    @SuppressLint("MissingPermission")
    private suspend fun requestSingleUpdate(): GeoLocation? {
        return suspendCancellableCoroutine { cont ->
            val provider = when {
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) -> LocationManager.GPS_PROVIDER
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) -> LocationManager.NETWORK_PROVIDER
                else -> {
                    cont.resume(null)
                    return@suspendCancellableCoroutine
                }
            }

            val listener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    locationManager.removeUpdates(this)
                    if (cont.isActive) {
                        cont.resume(GeoLocation(location.latitude, location.longitude))
                    }
                }

                @Deprecated("Deprecated in Java")
                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
                override fun onProviderEnabled(provider: String) {}
                override fun onProviderDisabled(provider: String) {
                    locationManager.removeUpdates(this)
                    if (cont.isActive) cont.resume(null)
                }
            }

            locationManager.requestSingleUpdate(provider, listener, null)

            cont.invokeOnCancellation {
                locationManager.removeUpdates(listener)
            }
        }
    }
}
