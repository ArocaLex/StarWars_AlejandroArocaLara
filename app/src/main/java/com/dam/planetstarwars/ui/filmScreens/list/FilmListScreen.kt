package com.dam.planetstarwars.ui.filmScreens.list

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dam.planetstarwars.data.model.Film
import com.dam.planetstarwars.ui.components.FilmItem
import com.dam.planetstarwars.ui.components.topAppBar.Action
import com.dam.planetstarwars.ui.components.topAppBar.BaseTopAppBarState
import com.dam.planetstarwars.ui.theme.PlanetStarWarsTheme

/**
 * Eventos UI desacoplados para la lista de películas
 */
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
                title = "Lista de Películas",
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
        // Buscador de películas
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.onSearchQueryChange(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            label = { Text("Buscar película") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
            singleLine = true
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
                        Text(
                            text = "Error: ${currentState.message}",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                is FilmListState.Success -> {
                    FilmListContent(
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
}

/**
 * Contenido de la lista de películas
 */
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
        items(items = list, key = { it.filmId }) { film ->
            FilmItem(
                film = film,
                onEdit = { events.onEdit(film) },
                onDelete = { filmToDelete = film }
            )
        }
    }

    // Diálogo de confirmación para eliminar
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

@Preview(
    name = "Lista Películas: Oscuro",
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
fun FilmListScreenPreview() {
    val filmsEjemplo = listOf(
        Film(filmId = 1, title = "A New Hope", episodeId = 4, director = "George Lucas", producer = "Gary Kurtz", releaseDate = "1977-05-25"),
        Film(filmId = 2, title = "The Empire Strikes Back", episodeId = 5, director = "Irvin Kershner", producer = "Gary Kurtz", releaseDate = "1980-05-17")
    )

    PlanetStarWarsTheme {
        Surface {
            FilmListContent(
                list = filmsEjemplo,
                events = FilmListEvents({}, {}, {}, {})
            )
        }
    }
}
