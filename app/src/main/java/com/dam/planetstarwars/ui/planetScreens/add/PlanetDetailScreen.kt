package com.dam.planetstarwars.ui.planetScreens.add


import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dam.liststarwars.ui.components.PlanetTextField
import com.dam.planetstarwars.R
import com.dam.planetstarwars.data.model.Climate
import com.dam.planetstarwars.data.model.Planet
import com.dam.planetstarwars.data.model.Terrain
import com.dam.planetstarwars.ui.common.LocalOutlinedTextFieldStyle
import com.dam.planetstarwars.ui.common.OutlinedTextFieldStyle
import com.dam.planetstarwars.ui.components.PlanetDropdownSelector
import com.dam.planetstarwars.ui.components.topAppBar.Action
import com.dam.planetstarwars.ui.components.topAppBar.BaseTopAppBarState
import com.dam.planetstarwars.ui.notification.AppPermissions
import com.dam.planetstarwars.ui.notification.NotificationHandler
import com.dam.planetstarwars.ui.notification.rememberPermissionsLauncher
import com.dam.planetstarwars.ui.theme.PlanetStarWarsTheme
import kotlinx.coroutines.launch


@Composable
fun PlanetDetailScreen(
    modifier: Modifier = Modifier,
    viewmodel: PlanetModificationViewModel = hiltViewModel(),
    onShowMessage: (String) -> Unit,
    onConfigureTopBar: (BaseTopAppBarState) -> Unit,
    onBack: () -> Unit
) {

    val context = LocalContext.current
    val notificationHandler = remember { NotificationHandler(context) }
    val scope = rememberCoroutineScope()

    val requestNotificationPermissionThenNotify = rememberPermissionsLauncher(
        permissions = listOf(AppPermissions.Notifications),

        onAllGranted = {

            notificationHandler.showSimpleNotification(
                contentTitle = "Planeta guardado",
                contentText = "Se ha procesado ${viewmodel.planetState.name}"
            )
            onBack() // Usamos el onBack que recibimos por parámetro
        },
        onDenied = {
            // Si deniega: Avisamos y volvemos atrás de todas formas (o lo que prefieras)
            onShowMessage("Permiso de notificaciones denegado, pero se guardó el planeta.")
            onBack()
        }
    )

    val saveAndNotify = {
        scope.launch {

            val error = viewmodel.savePlanet()

            if (error == null) {

                val successMsg = if (viewmodel.editMode) "Planeta actualizado" else "Planeta creado"

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requestNotificationPermissionThenNotify()
                } else {
                    notificationHandler.showSimpleNotification("Éxito", successMsg)
                    onBack()
                }

            } else {
                onShowMessage(error)
            }
        }
    }
    val backIconPainter = rememberVectorPainter(Icons.AutoMirrored.Filled.ArrowBack)
    val screenTitle = if (viewmodel.editMode) "Editar Planeta" else "Nuevo Planeta"

    LaunchedEffect(viewmodel.editMode) {
        onConfigureTopBar(
            BaseTopAppBarState(
                title = screenTitle,
                iconUpAction = backIconPainter,
                upAction = { onBack() },
                actions = listOf(
                    Action.ActionImageVector(
                        name = "Guardar",
                        icon = Icons.Default.Check,
                        contentDescription = "Guardar cambios",
                        onClick = { saveAndNotify() }
                    )
                )
            )
        )
    }

    // Actualizar las acciones para usar saveAndNotify
    val actions = PlanetAddActions(
        onNameChange = { viewmodel.onNameChange(it) },
        onClimateChange = { viewmodel.onClimateChange(it) },
        onTerrainChange = { viewmodel.onTerrainChange(it) },
        onDiameterChange = { viewmodel.onDiameterChange(it) },
        onGravityChange = { viewmodel.onGravityChange(it) },
        onPopulationChange = { viewmodel.onPopulationChange(it) },
        onRotationChange = { viewmodel.onRotationChange(it) },
        onOrbitalChange = { viewmodel.onOrbitalChange(it) },
        onCreatedChange = { viewmodel.onCreatedChange(it) },
        onColonizedChange = { viewmodel.onColonizedChange(it) },
        onSave = {
            saveAndNotify()
        },
    )

    CompositionLocalProvider(LocalOutlinedTextFieldStyle provides OutlinedTextFieldStyle) {
        PlanetAddScreenContent(
            modifier = modifier,
            planet = viewmodel.planetState,
            isEditMode = viewmodel.editMode,
            actions = actions
        )
    }
}
@Composable
fun PlanetAddScreenContent(
    modifier: Modifier = Modifier,
    planet: Planet,
    isEditMode: Boolean,
    actions: PlanetAddActions
) {
    val listState = rememberLazyListState()

    // Convertimos los Enums a una lista de Strings para el desplegable
    val climateOptions = remember { Climate.entries.map { it.textoInterfaz } }
    val terrainOptions = remember { Terrain.entries.map { it.textoInterfaz } }

    Column(
        modifier = modifier
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (isEditMode) "Editar Planeta" else "Añadir Planeta",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )

        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            // Campos de texto normal
            item { PlanetTextField("Nombre", planet.name, actions.onNameChange) }
            item { PlanetTextField("Periodo Rotación", planet.rotationPeriod, actions.onRotationChange) }
            item { PlanetTextField("Periodo Orbital", planet.orbitalPeriod, actions.onOrbitalChange) }

            // --- AQUÍ SUSTITUIMOS LOS TEXTFIELDS POR DROPDOWNS ---

            // Selector de Clima
            item {
                PlanetDropdownSelector(
                    label = "Clima",
                    options = climateOptions,
                    selectedOption = planet.climate,
                    onOptionSelected = actions.onClimateChange
                )
            }

            // Selector de Terreno
            item {
                PlanetDropdownSelector(
                    label = "Terreno",
                    options = terrainOptions,
                    selectedOption = planet.terrain,
                    onOptionSelected = actions.onTerrainChange
                )
            }
            // ----------------------------------------------------

            item { PlanetTextField("Diámetro", planet.diameter, actions.onDiameterChange) }
            item { PlanetTextField("Gravedad", planet.gravity, actions.onGravityChange) }
            item { PlanetTextField("Población", planet.population, actions.onPopulationChange) }
            item { PlanetTextField("Fecha Creación (DD-MM-YYYY)", planet.created, actions.onCreatedChange) }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "¿Está Colonizado?",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f)
                    )
                    Checkbox(
                        checked = planet.isColonized,
                        onCheckedChange = actions.onColonizedChange
                    )
                }
            }
        }

        Button(
            onClick = actions.onSave,
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
    name = "Formulario Accesible",
    group = "Pantallas",
    showSystemUi = true,
    fontScale = 1.3f,
    showBackground = true,
    backgroundColor = 0xFFE0F7FA
)
@Composable
fun PlanetFormPreviewAdvanced() {
    val planetaPrueba = Planet(
        name = "Hoth",
        rotationPeriod = "23",
        orbitalPeriod = "549",
        climate = "Frozen",
        terrain = "Tundra, Ice Caves",
        population = "Unknown",
        gravity = "1.1 standard",
        diameter = "7200",
        created = "21-05-1980",
        isColonized = false
    )


    val accionesEjemplo = PlanetAddActions({},{},{},{},{},{},{},{},{},{},{})


    PlanetStarWarsTheme {
        PlanetAddScreenContent(
            planet = planetaPrueba,
            isEditMode = true,
            actions = accionesEjemplo
        )
    }
}