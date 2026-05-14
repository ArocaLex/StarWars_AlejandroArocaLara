package com.dam.planetstarwars.ui.components

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
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    val style = LocalOutlinedTextFieldStyle.current

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(labelText) },
        singleLine = style.singleLine,
        isError = isError,
        supportingText = if (isError && errorMessage != null) { { Text(errorMessage) } } else null,
        modifier = modifier.fillMaxWidth()
    )
}
