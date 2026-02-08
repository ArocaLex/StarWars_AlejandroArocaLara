package com.dam.planetstarwars.ui.components.topAppBar

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseTopAppBar(state: BaseTopAppBarState) {
    val visible = state.actions.filter { it.isVisible }
    val notVisible = state.actions.filter { !it.isVisible }
    TopAppBar(
        title = {
            Text(
                text = state.title, modifier = Modifier.fillMaxWidth(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
            )
        },
        navigationIcon = {
            IconButton(onClick = state.upAction) {
                Icon(
                    painter = state.iconUpAction,
                    contentDescription = "",
                    tint = Color.Black
                )
            }
        },
        actions = {
            if (visible.isNotEmpty()) visible.forEach {
                IconButton(onClick = it.onClick) {
                    when (it) {
                        is Action.ActionImageVector -> {
                            Icon(
                                imageVector = it.icon!!,
                                contentDescription = it.contentDescription,
                                tint = Color.Black
                            )
                        }

                        is Action.ActionPainter -> {
                            Icon(
                                painter = it.icon!!,
                                contentDescription = it.contentDescription,
                                tint = Color.Black
                            )
                        }
                    }
                }
            }
            if (notVisible.isNotEmpty()) TopAppBarDropDownMenu(state.actions.filter { !it.isVisible })
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White
        )
    )
}