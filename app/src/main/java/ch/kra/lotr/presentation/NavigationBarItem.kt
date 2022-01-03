package ch.kra.lotr.presentation

import androidx.compose.ui.graphics.painter.Painter

data class NavigationBarItem(
    val name: String,
    val route: String,
    val painter: Painter
)
