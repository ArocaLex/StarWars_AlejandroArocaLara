package com.dam.planetstarwars.ui.notification

import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import kotlin.collections.map


/**
 * Pide VARIOS permisos a la vez.
 *
 * Devuelve una lambda "trigger" que:
 * - Si ya están concedidos -> onAllGranted()
 * - Si falta alguno -> lanza el diálogo del sistema
 *
 * onResultMap te permite ver qué ha concedido/rechazado el usuario.
 */
@Composable
fun rememberPermissionsLauncher(
    permissions: List<AppRuntimePermission>,
    onAllGranted: () -> Unit,
    onDenied: (denied: List<String>) -> Unit = {},
    onResultMap: (Map<String, Boolean>) -> Unit = {}
): () -> Unit {
    val context = LocalContext.current

    val permissionsToRequest = remember(permissions) {
        permissions.filter { it.appliesToDevice() }.map { it.permission }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        onResultMap(result)

        val denied = result.filterValues { granted -> !granted }.keys.toList()
        if (denied.isEmpty()) onAllGranted() else onDenied(denied)
    }

    return remember(permissionsToRequest) {
        {
            if (permissionsToRequest.isEmpty()) {
                onAllGranted() //Qué hacer cuando está concedido
                return@remember //finaliza la ejecución del bloque remember {} sin salir de la función que lo contiene.
            }

            val allGranted = permissionsToRequest.all { perm ->
                ContextCompat.checkSelfPermission(context, perm) == PackageManager.PERMISSION_GRANTED
            }

            if (allGranted) {
                onAllGranted() //Ya están todos concedidos
            } else {
                launcher.launch(permissionsToRequest.toTypedArray()) //Lanza del popup del sistema
            }
        }
    }
}