package com.dam.planetstarwars.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import com.dam.planetstarwars.ui.components.topAppBar.BaseTopAppBarState

@Composable
fun SettingsScreen(
    onConfigureTopBar: (BaseTopAppBarState) -> Unit,
    onOpenDrawer: () -> Unit
) {
    val menuIcon = rememberVectorPainter(image = Icons.Default.Menu)

    LaunchedEffect(Unit) {
        onConfigureTopBar(
            BaseTopAppBarState(
                title = "Ajustes",
                iconUpAction = menuIcon,
                upAction = { onOpenDrawer() },
                actions = emptyList()
            )
        )
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Pantalla de Ajustes", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Preferencias de la aplicación (en construcción)", style = MaterialTheme.typography.bodyLarge)
        }
    }
}
