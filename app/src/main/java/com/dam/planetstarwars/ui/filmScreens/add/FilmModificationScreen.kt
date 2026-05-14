package com.dam.planetstarwars.ui.filmScreens.add

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dam.planetstarwars.ui.components.topAppBar.Action
import com.dam.planetstarwars.ui.components.topAppBar.BaseTopAppBarState
import com.dam.planetstarwars.ui.notification.AppPermissions
import com.dam.planetstarwars.ui.notification.NotificationHandler
import com.dam.planetstarwars.ui.notification.rememberPermissionsLauncher
import com.dam.planetstarwars.ui.theme.PlanetStarWarsTheme

@Composable
fun FilmModificationScreen(
    modifier: Modifier = Modifier,
    viewModel: FilmModificationViewModel = hiltViewModel(),
    onConfigureTopBar: (BaseTopAppBarState) -> Unit,
    onShowMessage: (String) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val notificationHandler = remember { NotificationHandler(context) }

    var showDuplicateDialog by remember { mutableStateOf(false) }
    var duplicateTitle by remember { mutableStateOf("") }

    val requestPermissionThenNotify = rememberPermissionsLauncher(
        permissions = listOf(AppPermissions.Notifications),
        onAllGranted = { notificationHandler.showSimpleNotification("Película creada", "Se ha guardado '${viewModel.title}'") },
        onDenied = {}
    )

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is FilmScreenEvents.ShowMessage -> onShowMessage(event.message)
                is FilmScreenEvents.NavigateBack -> onBack()
                is FilmScreenEvents.ShowDuplicateDialog -> {
                    duplicateTitle = event.filmTitle
                    showDuplicateDialog = true
                }
                is FilmScreenEvents.NotifyCreated -> {
                    requestPermissionThenNotify()
                }
            }
        }
    }

    val backIconPainter = rememberVectorPainter(Icons.AutoMirrored.Filled.ArrowBack)
    val screenTitle = if (viewModel.isEditMode) "Editar Película" else "Nueva Película"

    LaunchedEffect(viewModel.isEditMode) {
        onConfigureTopBar(
            BaseTopAppBarState(
                title = screenTitle,
                iconUpAction = backIconPainter,
                upAction = { onBack() },
                actions = listOf(
                    Action.ActionImageVector(
                        name = "Guardar",
                        icon = Icons.Default.Check,
                        contentDescription = "Guardar película",
                        onClick = { viewModel.saveFilm() }
                    )
                )
            )
        )
    }

    if (showDuplicateDialog) {
        AlertDialog(
            onDismissRequest = { showDuplicateDialog = false },
            title = { Text("Película duplicada") },
            text = { Text("La película '$duplicateTitle' ya existe en la base de datos. Por favor, utiliza otro título.") },
            confirmButton = {
                TextButton(onClick = { showDuplicateDialog = false }) { Text("Aceptar") }
            }
        )
    }

    FilmFormContent(
        modifier = modifier,
        title = viewModel.title,
        titleError = viewModel.titleError,
        episodeId = viewModel.episodeId,
        episodeIdError = viewModel.episodeIdError,
        director = viewModel.director,
        directorError = viewModel.directorError,
        producer = viewModel.producer,
        producerError = viewModel.producerError,
        releaseDate = viewModel.releaseDate,
        releaseDateError = viewModel.releaseDateError,
        isEditMode = viewModel.isEditMode,
        onTitleChange = { viewModel.onTitleChange(it) },
        onEpisodeIdChange = { viewModel.onEpisodeIdChange(it) },
        onDirectorChange = { viewModel.onDirectorChange(it) },
        onProducerChange = { viewModel.onProducerChange(it) },
        onReleaseDateChange = { viewModel.onReleaseDateChange(it) },
        onSave = { viewModel.saveFilm() }
    )
}

@Composable
fun FilmFormContent(
    modifier: Modifier = Modifier,
    title: String,
    titleError: String? = null,
    episodeId: String,
    episodeIdError: String? = null,
    director: String,
    directorError: String? = null,
    producer: String,
    producerError: String? = null,
    releaseDate: String,
    releaseDateError: String? = null,
    isEditMode: Boolean,
    onTitleChange: (String) -> Unit,
    onEpisodeIdChange: (String) -> Unit,
    onDirectorChange: (String) -> Unit,
    onProducerChange: (String) -> Unit,
    onReleaseDateChange: (String) -> Unit,
    onSave: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (isEditMode) "Editar Película" else "Nueva Película",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item {
                OutlinedTextField(
                    value = title,
                    onValueChange = onTitleChange,
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = titleError != null,
                    supportingText = titleError?.let { { Text(it) } }
                )
            }
            item {
                OutlinedTextField(
                    value = episodeId,
                    onValueChange = onEpisodeIdChange,
                    label = { Text("Episodio (número)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = episodeIdError != null,
                    supportingText = episodeIdError?.let { { Text(it) } }
                )
            }
            item {
                OutlinedTextField(
                    value = director,
                    onValueChange = onDirectorChange,
                    label = { Text("Director") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = directorError != null,
                    supportingText = directorError?.let { { Text(it) } }
                )
            }
            item {
                OutlinedTextField(
                    value = producer,
                    onValueChange = onProducerChange,
                    label = { Text("Productor") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = producerError != null,
                    supportingText = producerError?.let { { Text(it) } }
                )
            }
            item {
                OutlinedTextField(
                    value = releaseDate,
                    onValueChange = onReleaseDateChange,
                    label = { Text("Fecha de estreno (YYYY-MM-DD)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = releaseDateError != null,
                    supportingText = releaseDateError?.let { { Text(it) } }
                )
            }
        }

        Button(
            onClick = onSave,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(text = if (isEditMode) "Actualizar" else "Guardar")
        }
    }
}

@Preview(
    name = "Formulario Película - Claro",
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun FilmFormPreviewLight() {
    PlanetStarWarsTheme {
        FilmFormContent(
            title = "A New Hope",
            episodeId = "4",
            director = "George Lucas",
            producer = "Gary Kurtz",
            releaseDate = "1977-05-25",
            isEditMode = false,
            onTitleChange = {}, onEpisodeIdChange = {}, onDirectorChange = {},
            onProducerChange = {}, onReleaseDateChange = {}, onSave = {}
        )
    }
}

@Preview(
    name = "Formulario Película - Con errores",
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun FilmFormPreviewErrors() {
    PlanetStarWarsTheme {
        FilmFormContent(
            title = "",
            titleError = "El título es obligatorio",
            episodeId = "abc",
            episodeIdError = "Debe ser un número mayor que 0",
            director = "",
            directorError = "El director es obligatorio",
            producer = "",
            producerError = "El productor es obligatorio",
            releaseDate = "",
            releaseDateError = "La fecha de estreno es obligatoria",
            isEditMode = false,
            onTitleChange = {}, onEpisodeIdChange = {}, onDirectorChange = {},
            onProducerChange = {}, onReleaseDateChange = {}, onSave = {}
        )
    }
}
