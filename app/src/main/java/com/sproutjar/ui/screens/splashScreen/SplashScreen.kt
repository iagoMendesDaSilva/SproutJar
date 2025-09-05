package com.sproutjar.ui.screens.splashScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.sproutjar.data.models.AppSettings
import com.sproutjar.data.models.GlobalDialogState
import com.sproutjar.utils.ConnectionState

@Composable
fun SplashScreen(
    connection: ConnectionState,
    navController: NavHostController,
    appSettings: AppSettings,
    saveAppSettings: (AppSettings) -> Unit,
    showGlobalDialog: (GlobalDialogState) -> Unit
) {}