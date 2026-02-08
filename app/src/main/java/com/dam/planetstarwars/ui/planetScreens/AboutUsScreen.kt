package com.dam.planetstarwars.ui.planetScreens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dam.planetstarwars.ui.components.topAppBar.BaseTopAppBarState
import com.dam.planetstarwars.R
import com.dam.planetstarwars.ui.theme.PlanetStarWarsTheme

/**
 * About us screen con topBar personalizada
 *
 * @param onBack
 * @param onConfigureTopBar
 */

@Composable
fun AboutUsScreen(
    onBack:()->Unit,
    onConfigureTopBar: (BaseTopAppBarState) -> Unit
) {

    val backIconPainter = rememberVectorPainter(Icons.AutoMirrored.Filled.ArrowBack)

    LaunchedEffect(Unit) {
        onConfigureTopBar(
            BaseTopAppBarState(
                title = "Sobre Nosotros",
                iconUpAction = backIconPainter,
                upAction = { onBack() },
                actions = emptyList()
            )
        )
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {


            Image(
                painter = painterResource(id = R.drawable.planet),
                contentDescription = "Planet Icon",
                modifier = Modifier.size(180.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Creado por: "+stringResource(R.string.app_dev),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Preview(
    name = "About Us Small Font",
    group = "Pantallas",
    showSystemUi = false,
    fontScale = 0.85f,
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
fun AboutUsAdvancedPreview() {
    PlanetStarWarsTheme {
        AboutUsScreen(
            onBack = {},
            onConfigureTopBar = {}
        )
    }
}

