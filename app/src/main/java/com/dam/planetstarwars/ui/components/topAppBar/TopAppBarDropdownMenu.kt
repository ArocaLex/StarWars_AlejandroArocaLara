package com.dam.planetstarwars.ui.components.topAppBar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue


@Composable
fun TopAppBarDropDownMenu(actions: List<Action>) {
    var expanded by remember { mutableStateOf(false) }
    IconButton(
        onClick = {
            expanded = true
        }
    ) {
        Icon(
            Icons.Default.MoreVert,
            contentDescription = "Abrir Menu",
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        actions.forEach {
            DropdownMenuItem(
                text = {
                    Text(it.name)
                },
                onClick = {
                    expanded = false
                    it.onClick()
                },
            )
        }
    }
}