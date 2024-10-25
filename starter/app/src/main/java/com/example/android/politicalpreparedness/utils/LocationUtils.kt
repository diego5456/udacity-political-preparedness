package com.example.android.politicalpreparedness.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Looper
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale
import kotlin.coroutines.resume

object LocationUtils {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    // Function to check if location services are enabled
    fun isLocationEnabled(context: Context): Boolean {
        val locationMode: Int
        try {
            locationMode = Settings.Secure.getInt(context.contentResolver, Settings.Secure.LOCATION_MODE)
        } catch (e: Settings.SettingNotFoundException) {
            return false
        }
        return locationMode != Settings.Secure.LOCATION_MODE_OFF
    }

    // Function to prompt the user to enable location services
    fun promptEnableLocation(context: Context) {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        context.startActivity(intent)
    }

    // Function to get the address from a location
    private fun getAddressFromLocation(context: Context, latitude: Double, longitude: Double): Address? {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
        return addresses?.firstOrNull()
    }

    @SuppressLint("MissingPermission")
    suspend fun getLocationAndAddress(context: Context): Address? = suspendCancellableCoroutine { continuation ->
        if (!isLocationEnabled(context)) {
            promptEnableLocation(context)
            if (continuation.isActive) {
                continuation.resume(null)
            }
            return@suspendCancellableCoroutine
        }

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                context as android.app.Activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            if (continuation.isActive) {
                continuation.resume(null)
            }
            return@suspendCancellableCoroutine
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
            .setWaitForAccurateLocation(false)
            .build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if (continuation.isActive) {
                    locationResult.locations.firstOrNull()?.let { location ->
                        val address = getAddressFromLocation(context, location.latitude, location.longitude)
                        continuation.resume(address)
                    } ?: run {
                        continuation.resume(null)
                    }
                }

                // Stop location updates after receiving the first result
                fusedLocationClient.removeLocationUpdates(this)
            }

            override fun onLocationAvailability(availability: LocationAvailability) {
                if (!availability.isLocationAvailable && continuation.isActive) {
                    continuation.resume(null)
                }
            }
        }

        // Start receiving location updates
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())

        // Ensure the continuation is cancelled if the location updates are stopped manually
        continuation.invokeOnCancellation {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }

    // Constant for location permission request
    private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
}