package com.dam.planetstarwars.ui.theme


import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF00C853),
    secondary = Color(0xFF64DD17),
    tertiary = Color(0xFF00E5FF),
    background = Color(0xFFF1F8E9),
    surface = Color(0xFFFFFFFF),
    onPrimary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFF000000),
    onBackground = Color(0xFF1B1B1B),
    onSurface = Color(0xFF1B1B1B),
    outline = Color(0xFF81C784)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF00E676),
    secondary = Color(0xFF69F0AE),
    tertiary = Color(0xFF18FFFF),
    background = Color(0xFF0A0F0A),
    surface = Color(0xFF1A1F1A),
    onPrimary = Color(0xFF000000),
    onSecondary = Color(0xFF000000),
    onBackground = Color(0xFFE0F2F1),
    onSurface = Color(0xFFE0F2F1),
    outline = Color(0xFF00C853)
)

@Composable
fun PlanetStarWarsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}