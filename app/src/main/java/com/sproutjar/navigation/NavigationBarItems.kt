package com.sproutjar.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ViewComfy
import androidx.compose.material.icons.outlined.AutoGraph
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ViewComfy
import androidx.compose.ui.graphics.vector.ImageVector
import com.sproutjar.R

sealed class NavigationBarItem(
    @StringRes val title: Int,
    val iconUnSelected: ImageVector,
    val iconSelected: ImageVector,
    val route: String
) {

    object Pots :
        NavigationBarItem(
            R.string.pots,
            Icons.Outlined.ViewComfy,
            Icons.Default.ViewComfy,
            Screens.PotsScreen.name
        )

    object Simulations :
        NavigationBarItem(
            R.string.simulations,
            Icons.Outlined.Calculate,
            Icons.Default.Calculate,
            Screens.SimulationsScreen.name
        )

    object Projections :
        NavigationBarItem(
            R.string.projections,
            Icons.Outlined.AutoGraph,
            Icons.Default.AutoGraph,
            Screens.ProjectionsScreen.name
        )

    object Settings :
        NavigationBarItem(
            R.string.settings,
            Icons.Outlined.Settings,
            Icons.Default.Settings,
            Screens.SettingsScreen.name
        )

    companion object {
        val items: List<NavigationBarItem> = listOf(
            Pots,
            Simulations,
            Projections,
            Settings
        )
    }
}