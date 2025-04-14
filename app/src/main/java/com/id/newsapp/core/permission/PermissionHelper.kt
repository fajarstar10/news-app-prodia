package com.id.newsapp.core.permission

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED

@Composable
fun rememberPermissionState(
    permissions: List<String>
): AppPermissionState {
    val context = LocalContext.current

    val grantedStates = remember {
        permissions.associateWith { permission ->
            mutableStateOf(
                ContextCompat.checkSelfPermission(context, permission) == PERMISSION_GRANTED
            )
        }.toMutableMap()
    }

    val launcherSingle = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        grantedStates[permissions.first()]?.value = granted
    }

    val launcherMultiple = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        results.forEach { (perm, granted) ->
            grantedStates[perm]?.value = granted
        }
    }

    return remember {
        AppPermissionState(
            permissions = permissions,
            grantedStates = grantedStates,
            requestPermissions = {
                if (permissions.size == 1) {
                    launcherSingle.launch(permissions.first())
                } else {
                    launcherMultiple.launch(permissions.toTypedArray())
                }
            })
    }
}

data class AppPermissionState(
    val permissions: List<String>,
    val grantedStates: Map<String, MutableState<Boolean>>,
    val requestPermissions: () -> Unit
) {
    fun isGranted(permission: String): Boolean {
        return grantedStates[permission]?.value == true
    }

    fun allGranted(): Boolean = grantedStates.all { it.value.value }

    fun anyDenied(): Boolean = grantedStates.any { !it.value.value }
}