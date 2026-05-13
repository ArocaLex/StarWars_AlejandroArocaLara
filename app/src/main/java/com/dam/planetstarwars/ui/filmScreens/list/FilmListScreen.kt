package com.dam.planetstarwars.ui.filmScreens.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dam.planetstarwars.data.model.Film
import com.dam.planetstarwars.ui.components.FilmItem
import com.dam.planetstarwars.ui.components.topAppBar.Action
import com.dam.planetstarwars.ui.components.topAppBar.BaseTopAppBarState

data class FilmListEvents(
    val onDelete: (Film) -> Unit,
    val onEdit: (Film) -> Unit,
    val onAdd: () -> Unit,
    val onShowMessage: (String) -> Unit,
)

@Composable
fun FilmListScreen(
    modifier: Modifier = Modifier,
    viewModel: FilmListViewModel = hiltViewModel(),
    onAddFilm: () -> Unit,
    onEditFilm: (Film) -> Unit,
    onShowSnackbar: (String) -> Unit,
    onConfigureTopBar: (BaseTopAppBarState) -> Unit,
    onOpenDrawer: () -> Unit
) {
    val menuIconPainter = rememberVectorPainter(Icons.Default.Menu)

    LaunchedEffect(Unit) {
        onConfigureTopBar(
            BaseTopAppBarState(
                title = "Películas de Star Wars",
                iconUpAction = menuIconPainter,
                upAction = { onOpenDrawer() },
                actions = listOf(
                    Action.ActionImageVector(
                        name = "Añadir",
                        icon = Icons.Default.Add,
                        contentDescription = "Boton para añadir película",
                        onClick = { onAddFilm() },
                        isVisible = true
                    )
                )
            )
        )
    }

    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    Column(modifier = modifier.fillMaxSize()) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.onSearchQueryChange(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            label = { Text("Buscar película") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") }
        )

        Box(modifier = Modifier.weight(1f).fillMaxSize()) {
            when (val currentState = state) {
        is FilmListState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is FilmListState.Empty -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No hay películas disponibles. ¡Añade una!")
            }
        }
        is FilmListState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Error: ${currentState.message}", color = MaterialTheme.colorScheme.error)
            }
        }
        is FilmListState.Success -> {
            FilmListContent(
                modifier = modifier,
                list = currentState.films,
                events = FilmListEvents(
                    onDelete = { film -> viewModel.deleteFilm(film) },
                    onAdd = onAddFilm,
                    onEdit = onEditFilm,
                    onShowMessage = onShowSnackbar
                )
            )
        }
    }
}
}

@Composable
fun FilmListContent(
    modifier: Modifier = Modifier,
    list: List<Film>,
    events: FilmListEvents
) {
    var filmToDelete by remember { mutableStateOf<Film?>(null) }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items = list) { film ->
            FilmItem(
                film = film,
                onEdit = { events.onEdit(film) },
                onDelete = { filmToDelete = film }
            )
        }
    }

    if (filmToDelete != null) {
        AlertDialog(
            onDismissRequest = { filmToDelete = null },
            title = { Text("Eliminar Película") },
            text = { Text("¿Desea eliminar ${filmToDelete?.title}?") },
            confirmButton = {
                TextButton(onClick = {
                    filmToDelete?.let { events.onDelete(it) }
                    events.onShowMessage("Película eliminada")
                    filmToDelete = null
                }) {
                    Text("Sí")
                }
            },
            dismissButton = {
                TextButton(onClick = { filmToDelete = null }) {
                    Text("No")
                }
            }
        )
    }
}
