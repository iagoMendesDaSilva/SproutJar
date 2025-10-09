package com.sproutjar.navigation


import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.sproutjar.data.models.AppSettings
import com.sproutjar.data.models.GlobalDialogState
import com.sproutjar.data.models.Pot
import com.sproutjar.data.models.PotArgType
import com.sproutjar.ui.screens.potScreen.PotScreen
import com.sproutjar.ui.screens.potsScreen.PotsScreen
import com.sproutjar.ui.screens.projectionsScreen.ProjectionsScreen
import com.sproutjar.ui.screens.settingsScreen.SettingsScreen
import com.sproutjar.ui.screens.simulationsScreen.SimulationsScreen
import com.sproutjar.ui.screens.splashScreen.SplashScreen
import com.sproutjar.utils.ConnectionState


@Composable
internal fun ConnectionReloadHandler(
    connection: ConnectionState,
    navController: NavHostController,
    route: String
) {
    var wasOffline by remember { mutableStateOf(false) }

    LaunchedEffect(connection) {
        if (connection == ConnectionState.Unavailable) {
            wasOffline = true
        } else if (connection == ConnectionState.Available && wasOffline) {
            wasOffline = false
            navController.navigate(route) {
                popUpTo(route) { inclusive = true }
            }
        }
    }
}

@Composable
fun Navigation(
    connection: ConnectionState,
    extras: Bundle?,
    navController: NavHostController,
    appSettings: AppSettings,
    saveAppSettings: (appSettings: AppSettings) -> Unit,
    showGlobalDialog: (globalDialogState: GlobalDialogState) -> Unit,
) {

    NavHost(
        navController = navController,
        startDestination = Screens.SplashScreen.name
    ) {

        composable(Screens.SplashScreen.name) {
            ConnectionReloadHandler(
                connection,
                navController,
                Screens.SplashScreen.name
            )
            SplashScreen(
                connection,
                navController,
                appSettings,
                saveAppSettings,
                showGlobalDialog,
            )
        }

        composable(Screens.SettingsScreen.name) {
            ConnectionReloadHandler(
                connection,
                navController,
                Screens.SettingsScreen.name
            )
            SettingsScreen(
                connection,
                navController,
                appSettings,
                saveAppSettings,
                showGlobalDialog,
            )
        }

        composable(Screens.PotsScreen.name) {
            ConnectionReloadHandler(
                connection,
                navController,
                Screens.PotsScreen.name
            )
            PotsScreen(
                connection,
                navController,
                appSettings,
                saveAppSettings,
                showGlobalDialog
            )
        }

        composable(
            Screens.PotScreen.name + "?pot={pot}",
            arguments = listOf(navArgument("pot") {
                type = PotArgType.potArgType
            })
        ) { navBackStackEntry ->
            val pot = navBackStackEntry.arguments?.getString("pot")
                ?.let { Gson().fromJson(it, Pot::class.java) }
            ConnectionReloadHandler(
                connection,
                navController,
                Screens.PotScreen.name
            )
            PotScreen(
                connection,
                navController,
                appSettings,
                pot,
                saveAppSettings,
                showGlobalDialog
            )
        }

        composable(Screens.SimulationsScreen.name) {
            ConnectionReloadHandler(
                connection,
                navController,
                Screens.SimulationsScreen.name
            )
            SimulationsScreen(
                connection,
                navController,
                appSettings,
                saveAppSettings,
                showGlobalDialog
            )
        }

        composable(Screens.ProjectionsScreen.name) {
            ConnectionReloadHandler(
                connection,
                navController,
                Screens.ProjectionsScreen.name
            )
            ProjectionsScreen(
                connection,
                navController,
                appSettings,
                saveAppSettings,
                showGlobalDialog
            )
        }
    }
}