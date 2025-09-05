package com.sproutjar.navigation


import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sproutjar.data.models.AppSettings
import com.sproutjar.data.models.GlobalDialogState
import com.sproutjar.ui.screens.boxScreen.BoxScreen
import com.sproutjar.ui.screens.boxesScreen.BoxesScreen
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
        startDestination = Screens.BoxesScreen.name
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

        composable(Screens.BoxesScreen.name) {
            ConnectionReloadHandler(
                connection,
                navController,
                Screens.BoxesScreen.name
            )
            BoxesScreen(
                connection,
                navController,
                appSettings,
                saveAppSettings,
                showGlobalDialog
            )
        }

        composable(
            Screens.BoxScreen.name + "?id={id}",
            arguments = listOf(navArgument("id") {
                type = NavType.IntType
                defaultValue = 0
            })
        ) {
            ConnectionReloadHandler(
                connection,
                navController,
                Screens.BoxScreen.name
            )
            BoxScreen(
                connection,
                navController,
                appSettings,
                it.arguments?.getInt("id") ?: 0,
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