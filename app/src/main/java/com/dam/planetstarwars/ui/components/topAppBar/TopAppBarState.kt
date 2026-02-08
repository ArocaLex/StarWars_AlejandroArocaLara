package com.dam.planetstarwars.ui.components.topAppBar

import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Clase que representa el estado de la Top App Bar.
 * @property title         el título de la App Bar.
 * @property iconUpAction  el ícono utilizado para la navigationButton.
 * @property upAction      el evento de la navigationButton.
 * @property actions       la lista de acciones disponibles en la App Bar.
 */
data class BaseTopAppBarState(
    val title:String,
    val iconUpAction: Painter,
    val upAction:()->Unit,
    val actions:List<Action>
)

/**
 * clase sellada que presenta los actions de la topappbar
 * @property name nombre de la action
 * @property contentDescription descripcion de la action
 * @property onClick evento de la action
 * @property isVisible si la action es visible o no
 */


sealed class Action(
    open val name:String,
    open val contentDescription:String,
    open val onClick: ()->Unit,
    open val isVisible:Boolean=true){

    /**
     * data class que construye el action con un painter
     * @property icono en formato painter
     */
    data class ActionPainter(
        override val name:String,
        val icon: Painter?,
        override val contentDescription:String,
        override val onClick: ()->Unit,
        override val isVisible:Boolean=true
    ): Action(name, contentDescription, onClick ,isVisible)
    /**
     * data class que construye el action con un imageVector
     * @property icon icono en formato imageVector
     */
    data class ActionImageVector(
        override val name:String,
        val icon: ImageVector?,
        override val contentDescription:String,
        override val onClick: ()->Unit,
        override val isVisible:Boolean=true
    ): Action(name, contentDescription, onClick ,isVisible)
}