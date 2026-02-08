package com.dam.planetstarwars.ui.notification


import android.Manifest
import android.os.Build

object AppPermissions {

    val Notifications = AppRuntimePermission(
        permission = Manifest.permission.POST_NOTIFICATIONS,
        minSdk = Build.VERSION_CODES.TIRAMISU // 33
    )

    val Camera = AppRuntimePermission(
        permission = Manifest.permission.CAMERA,
        minSdk = Build.VERSION_CODES.M // 23
    )
}
