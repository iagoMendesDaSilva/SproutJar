package com.sproutjar.ui.screens.potScreen

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.sproutjar.data.models.AppSettings
import com.sproutjar.data.models.GlobalDialogState
import com.sproutjar.utils.ConnectionState

@Composable
fun PotScreen(
    connection: ConnectionState,
    navController: NavHostController,
    appSettings: AppSettings,
    id: Int,
    saveAppSettings: (AppSettings) -> Unit,
    showGlobalDialog: (GlobalDialogState) -> Unit
) {}