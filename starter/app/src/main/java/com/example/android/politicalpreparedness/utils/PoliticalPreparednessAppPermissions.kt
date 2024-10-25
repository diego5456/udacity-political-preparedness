package com.example.android.politicalpreparedness.utils

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.android.politicalpreparedness.BuildConfig
import com.example.android.politicalpreparedness.R
import com.google.android.material.snackbar.Snackbar

val runningQOrLater = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
val runningTiramisuOrLater = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

fun createPermissionDeniedSnackbar(fragment: Fragment, content: String) {
    Snackbar.make(
        fragment.requireView(),
        content,
        Snackbar.LENGTH_LONG
    )
        .setAction(R.string.settings) {
            fragment.startActivity(Intent().apply {
                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                data = Uri.fromParts("package",   BuildConfig.APPLICATION_ID, null)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            })
        }.show()
}

fun permissionRequests(
    fragment: Fragment,
    requestPermissionLauncher: ActivityResultLauncher<String>,
    permission: PoliticalPreparednessAppPermissions
) {
    val permissionsCheck = ContextCompat.checkSelfPermission(
        fragment.requireContext(),
        permission.permission
    )
    if (permissionsCheck != PackageManager.PERMISSION_GRANTED) {

        requestPermissionLauncher.launch(permission.permission)
        createAlertDialog(
            fragment,
            permission.rationaleTitle,
            permission.rationaleMessage
        ) { requestPermissionLauncher.launch(permission.permission) }

    } else {
        requestPermissionLauncher.launch(permission.permission)
    }
}

fun checkPermission(fragment: Fragment, permission: PoliticalPreparednessAppPermissions): Boolean {
    return ContextCompat.checkSelfPermission(
        fragment.requireContext(),
        permission.permission
    ) == PackageManager.PERMISSION_GRANTED
}


private fun createAlertDialog(
    fragment: Fragment,
    title: String,
    message: String,
    positiveAction: () -> Unit
) {
    androidx.appcompat.app.AlertDialog.Builder(fragment.requireContext())
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(android.R.string.ok) { _, _ ->
            positiveAction()
        }
        .setNegativeButton(android.R.string.cancel, null)
        .show()
}


val TAG = "PermissionsUtil"
val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34
val REQUEST_TURN_DEVICE_LOCATION_ON = 29
val LOCATION_PERMISSION_INDEX = 0


val locationPermissions = PoliticalPreparednessAppPermissions(
    Manifest.permission.ACCESS_FINE_LOCATION,
    "Location permission",
    "You need to grant location permission in order to use this app.",
    "Please allow Location access"
)