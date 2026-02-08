package com.dam.planetstarwars.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dam.planetstarwars.R
import com.dam.planetstarwars.data.model.Planet
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.tooling.preview.Preview
import com.dam.planetstarwars.ui.theme.PlanetStarWarsTheme


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlanetItem(
    modifier: Modifier = Modifier,
    planet: Planet,
    onEdit: (Planet) -> Unit,
    onDelete: (Planet) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .combinedClickable(
                onClick = { onEdit(planet) },
                onLongClick = { onDelete(planet) }
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                painter = painterResource(id = R.drawable.planet),
                contentDescription = "Planet Icon",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(56.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {


                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = planet.name,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )

                }

                Spacer(modifier = Modifier.height(4.dp))

                // Detalles
                Text(
                    text = "${stringResource(R.string.climate_field)}: ${planet.climate}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = "${stringResource(R.string.terrain_field)}: ${planet.terrain}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = "${stringResource(R.string.population_field)}: ${planet.population}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Descubierto: ${planet.created}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

/**
 * Preview: Modo Oscuro + Fuente Gigante (2.0x)
 */
@Preview(
    name = "Item Dark & Giant Font",
    group = "Accesibilidad",
    showBackground = true,
    backgroundColor = 0xFF121212, // Fondo negro
    uiMode = Configuration.UI_MODE_NIGHT_YES, // Activa recursos night
    fontScale = 2.0f
)
@Composable
fun PlanetItemPreviewAdvanced() {
    val planetaEjemplo = Planet(
        name = "Tatooine (Planeta Desértico Muy Largo)",
        rotationPeriod = "23",
        orbitalPeriod = "304",
        climate = "Arid, Hot, Windy",
        terrain = "Desert",
        population = "200000",
        gravity = "1 standard",
        diameter = "10465",
        created = "01-01-1977",
        isColonized = true
    )

    PlanetStarWarsTheme {
        PlanetItem(
            planet = planetaEjemplo,
            onEdit = {},
            onDelete = {}
        )
    }
}