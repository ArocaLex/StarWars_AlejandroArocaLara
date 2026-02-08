package com.dam.planetstarwars.ui.common

import androidx.compose.runtime.staticCompositionLocalOf

data class OutlinedTextFieldStyleDataClass(
    val singleLine: Boolean,
)

val OutlinedTextFieldStyle = OutlinedTextFieldStyleDataClass(
    singleLine = true,
)

val LocalOutlinedTextFieldStyle = staticCompositionLocalOf { OutlinedTextFieldStyle }
