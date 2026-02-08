package com.dam.planetstarwars.ui.planetScreens.list

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add

import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dam.planetstarwars.data.model.Planet
import com.dam.planetstarwars.ui.components.PlanetItem

import com.dam.planetstarwars.ui.components.topAppBar.Action
import com.dam.planetstarwars.ui.components.topAppBar.BaseTopAppBarState
import com.dam.planetstarwars.ui.theme.PlanetStarWarsTheme


/**
 * Eventos UI desacoplados
 */
data class PlanetListEvents(
    val onDelete: (Planet) -> Unit,
    val onEdit: (Planet) -> Unit,
    val onAdd: () -> Unit,
    val onShowMessage: (String) -> Unit,

    )

@Composable
fun PlanetListScreen(
    modifier: Modifier = Modifier,
    viewModel: PlanetListViewModel = hiltViewModel(),
    onAddPlanet: () -> Unit,
    onEditPlanet: (Planet) -> Unit,
    onShowSnackbar: (String) -> Unit,
    onAboutUs: () -> Unit,
    onConfigureTopBar: (BaseTopAppBarState) -> Unit,
    onOpenDrawer:()-> Unit
) {


    val menuIconPainter = rememberVectorPainter(Icons.Default.Menu)

    LaunchedEffect(Unit) {
        onConfigureTopBar(
            BaseTopAppBarState(
                title = "Lista de Planetas",
                iconUpAction = menuIconPainter,
                upAction = {},
                actions = listOf(
                    Action.ActionImageVector(
                        name = "Añadir",
                        icon = Icons.Default.Add,
                        contentDescription = "Boton para añadir planeta",
                        onClick = {onAddPlanet()},
                        isVisible = true
                    ),
                    Action.ActionImageVector(
                        icon = Icons.Default.MoreVert,
                        name = "AboutUs",
                        contentDescription = "Icono para AboutUs",
                        onClick = {onAboutUs()},
                        isVisible = true,
                    ),

                )
            )
        )
    }


    val state by viewModel.uiState.collectAsStateWithLifecycle()


    when (val currentState = state) {

        is PlanetListState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is PlanetListState.NoData -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No hay planetas disponibles. ¡Añade uno!")
            }
        }

        is PlanetListState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "Error: ${currentState.message}",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        is PlanetListState.Success -> {
            PlanetListContent(
                modifier = modifier,
                list = currentState.planets,
                events = PlanetListEvents(
                    onDelete = { planet -> viewModel.deletePlanet(planet) },
                    onAdd = onAddPlanet,
                    onEdit = onEditPlanet,
                    onShowMessage = onShowSnackbar
                )
            )
        }
    }
}

/**
 * Contenido de la lista
 */
@Composable
fun PlanetListContent(
    modifier: Modifier = Modifier,
    list: List<Planet>,
    events: PlanetListEvents
) {
    var planetToDelete by remember { mutableStateOf<Planet?>(null) }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items = list) { planet ->
            PlanetItem(
                planet = planet,
                onEdit = { events.onEdit(planet) },
                onDelete = { planetToDelete = planet }
            )
        }
    }

    // Lógica del AlertDialog
    if (planetToDelete != null) {
        AlertDialog(
            onDismissRequest = { planetToDelete = null },
            title = { Text("Eliminar Planeta") },
            text = { Text("¿Desea eliminar ${planetToDelete?.name}?") },
            confirmButton = {
                TextButton(onClick = {
                    planetToDelete?.let { events.onDelete(it) }
                    events.onShowMessage("Planeta eliminado")
                    planetToDelete = null
                }) {
                    Text("Sí")
                }
            },
            dismissButton = {
                TextButton(onClick = { planetToDelete = null }) {
                    Text("No")
                }
            }
        )
    }
}

@Preview(
    name = "Lista: Oscuro + Fuente 1.5x",
    group = "Pantallas",
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    fontScale = 1.5f,
    showBackground = true,
    backgroundColor = 0xFF121212
)
@Composable
fun PlanetListScreenAdvancedPreview() {

    val planetasEjemplo = listOf(
        Planet(
            name = "Tatooine", rotationPeriod = "23", orbitalPeriod = "304",
            climate = "Arid", terrain = "Desert", population = "200000",
            gravity = "1", diameter = "10465", created = "01-01-1977", isColonized = true
        ),
        Planet(
            name = "Hoth (Planeta Helado)", rotationPeriod = "23", orbitalPeriod = "549",
            climate = "Frozen", terrain = "Tundra, Ice Caves", population = "Unknown",
            gravity = "1.1", diameter = "7200", created = "21-05-1980", isColonized = false
        ),
        Planet(
            name = "Endor", rotationPeriod = "18", orbitalPeriod = "402",
            climate = "Temperate", terrain = "Forests, Mountains", population = "30000",
            gravity = "0.85", diameter = "4900", created = "25-05-1983", isColonized = true
        )
    )

    PlanetStarWarsTheme {

        PlanetListContent(
            modifier = Modifier.fillMaxSize(),
            list = planetasEjemplo,
            events = PlanetListEvents(
                onDelete = {},
                onEdit = {},
                onAdd = {},
                onShowMessage = {}
            )
        )
    }
}