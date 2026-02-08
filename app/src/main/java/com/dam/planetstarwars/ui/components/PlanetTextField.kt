package com.dam.liststarwars.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dam.planetstarwars.ui.common.LocalOutlinedTextFieldStyle

@Composable
fun PlanetTextField(
    labelText: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val style = LocalOutlinedTextFieldStyle.current

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(labelText) },
        singleLine = style.singleLine,
        modifier = modifier.fillMaxWidth()
    )
}
