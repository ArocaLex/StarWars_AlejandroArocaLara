package com.dam.planetstarwars.ui.planetScreens.add


/**
 * Clase que almacena las acciones de la pantalla de añadir/editar
 *
 * @property onNameChange
 * @property onClimateChange
 * @property onTerrainChange
 * @property onDiameterChange
 * @property onGravityChange
 * @property onPopulationChange
 * @property onRotationChange
 * @property onOrbitalChange
 * @property onCreatedChange
 * @property onColonizedChange
 * @property onSave
 * @constructor
 */

data class PlanetAddActions(
    val onNameChange: (String) -> Unit,
    val onClimateChange: (String) -> Unit,
    val onTerrainChange: (String) -> Unit,
    val onDiameterChange: (String) -> Unit,
    val onGravityChange: (String) -> Unit,
    val onPopulationChange: (String) -> Unit,
    val onRotationChange: (String) -> Unit,
    val onOrbitalChange: (String) -> Unit,
    val onCreatedChange: (String) -> Unit,
    val onColonizedChange: (Boolean) -> Unit,
    val onSave: () -> Unit
)